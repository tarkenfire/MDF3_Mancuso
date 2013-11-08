/* 
 * Date: Nov 4, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service implements OnPreparedListener
{
	private final IBinder _binder = new MusicBinder();
	protected MediaPlayer mediaPlayer;
	MusicPlayerActivity _contextRef;
	File _fileRef;
	boolean isPlayerPrepared = false;
	int currentTrackPosition = 0;
	
	Handler timeHandler = new Handler();
	
	//binder class for client activities
	public class MusicBinder extends Binder
	{
		MusicService getService()
		{
			return MusicService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return _binder;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onPrepared(MediaPlayer player)
	{
		isPlayerPrepared = true;
		
		//if returning from the notification intent, continue playing music.
		if (!_contextRef.isActivityNew)
		{
			Log.e("bool", "hit bool");
			playTrack();
		}
	}
	
	//private methods and runnables
	private Runnable musicTimerRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			if (mediaPlayer.isPlaying())
			{
				_contextRef.setCurrentDuration(mediaPlayer.getCurrentPosition());
				timeHandler.postDelayed(this, 1000);
			}
		}
	};
	
	
	//public methods
	public boolean prepareMediaPlayer(String path)
	{
		if (mediaPlayer != null)
		{
			currentTrackPosition = mediaPlayer.getCurrentPosition();
			mediaPlayer.release();
		}
		else
			currentTrackPosition = 0;
		
		mediaPlayer = new MediaPlayer();
		isPlayerPrepared = false;
		
		try
		{
			mediaPlayer.setDataSource(path);
		}
		catch (Exception e)
		{
			return false;
		}
		
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.prepareAsync();
		return true;
	}
	
	public void playTrack()
	{
		if (isPlayerPrepared)
		{
			mediaPlayer.start();
			mediaPlayer.seekTo(currentTrackPosition);
			_contextRef.onPlaybackStarted(mediaPlayer.getDuration());
			timeHandler.postDelayed(musicTimerRunnable, 100);
			
			Intent returnIntent = new Intent (getApplicationContext(), MusicPlayerActivity.class);
			
			returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			PendingIntent returnToPlayer = PendingIntent.getActivity(this.getApplicationContext(), 0, returnIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			MediaMetadataRetriever mmr = new MediaMetadataRetriever();
			mmr.setDataSource(_fileRef.getAbsolutePath());
			
			//to use .build() instead of .getNotification(), I would have 
			//had to bump up the min API to 16, which is 4.1. 
			//I want to keep 4.0 support lest I break anything else in my code.
			@SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(this)
				.setContentIntent(returnToPlayer)
				.setContentTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
				.setContentText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
				.setOngoing(true)
				.setTicker(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) + " - "
						+ mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
				.setSmallIcon(R.drawable.ic_launcher)
				.getNotification();
			
			this.startForeground(5, notification);
		}
	}
	
	public void pauseTrack()
	{
		if (mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			stopForeground(true);
		}
	}
	
	public void seekToPoint(int milSeconds)
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(milSeconds);
	}
	
	public void seekForwardFiveSeconds()
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
	}
	
	public void seekBackwardFiveSeconds()
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
	}
	
	public boolean isMusicPlaying() 
	{
		return mediaPlayer.isPlaying();
	}
	
	public void stopTrack()
	{
		mediaPlayer.stop();
	}
	
	public void setPlayerInstanceReference(MusicPlayerActivity activity)
	{
		_contextRef = activity;
	}
	
	public void setFileReference(File file)
	{
		_fileRef = file;
	}
}
