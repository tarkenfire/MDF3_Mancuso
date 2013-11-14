/* 
 * Date: Nov 7, 2013
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
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * The Class MusicService.
 */
public class MusicService extends Service implements OnPreparedListener
{
	private final IBinder _binder = new MusicBinder();
	protected MediaPlayer mediaPlayer;
	MusicPlayerActivity _contextRef;
	File _fileRef;
	boolean isPlayerPrepared = false;
	int currentTrackPosition = 0;
	
	//intent action for updating the widget
	public static final String ACTION_UDPATE_WIDGET = "com.hinodesoftworks.UPDATE_WIDGET";
	
	Handler timeHandler = new Handler();
	
	//binder class for client activities
	/**
	 * The Class MusicBinder.
	 */
	public class MusicBinder extends Binder
	{
		
		/**
		 * Gets the service.
		 *
		 * @return the service
		 */
		MusicService getService()
		{
			return MusicService.this;
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return _binder;
	}


	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	/* (non-Javadoc)
	 * @see android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media.MediaPlayer)
	 */
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
	/**
	 * Prepare media player.
	 *
	 * @param path the path of the file to play
	 * @return true, if successful
	 */
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
	
	/**
	 * Play track.
	 */
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
			
			//update any widgets to new track data.
			//Intent widgetIntent = new Intent(this, WidgetProvider.class);
			Intent widgetIntent = new Intent();
			widgetIntent.setAction(ACTION_UDPATE_WIDGET);
			
			widgetIntent.putExtra("track", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
			widgetIntent.putExtra("artist", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
			widgetIntent.putExtra("album", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
			widgetIntent.putExtra("genre", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
			widgetIntent.putExtra("bitrate", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
			widgetIntent.putExtra("duration", mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
			
			sendBroadcast(widgetIntent);
		}
	}
	
	/**
	 * Pause track.
	 */
	public void pauseTrack()
	{
		if (mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			stopForeground(true);
		}
	}
	
	/**
	 * Seek to specific point in a track.
	 *
	 * @param milSeconds the mil seconds
	 */
	public void seekToPoint(int milSeconds)
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(milSeconds);
	}
	
	/**
	 * Seek forward five seconds.
	 */
	public void seekForwardFiveSeconds()
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
	}
	
	/**
	 * Seek backward five seconds.
	 */
	public void seekBackwardFiveSeconds()
	{
		if (isPlayerPrepared)
			mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
	}
	
	/**
	 * Checks if is music playing.
	 *
	 * @return true, if music is playing
	 */
	public boolean isMusicPlaying() 
	{
		return mediaPlayer.isPlaying();
	}
	
	/**
	 * Stop track.
	 */
	public void stopTrack()
	{
		mediaPlayer.stop();
	}
	
	/**
	 * Sets a reference to the bound activity
	 *
	 * @param activity the new player instance reference
	 */
	public void setPlayerInstanceReference(MusicPlayerActivity activity)
	{
		_contextRef = activity;
	}
	
	/**
	 * Sets the file reference.
	 *
	 * @param file the new file reference
	 */
	public void setFileReference(File file)
	{
		_fileRef = file;
	}
	
}
