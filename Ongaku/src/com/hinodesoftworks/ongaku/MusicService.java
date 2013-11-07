/* 
 * Date: Nov 4, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class MusicService extends Service implements OnPreparedListener
{
	private final IBinder _binder = new MusicBinder();
	protected MediaPlayer mediaPlayer;
	MusicPlayerActivity _contextRef;
	boolean isPlayerPrepared = false;
	
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
			_contextRef.onPlaybackStarted(mediaPlayer.getDuration());
			timeHandler.postDelayed(musicTimerRunnable, 1000);
		}
	}
	
	public void pauseTrack()
	{
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
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
	
	public void setPlayerInstanceReference(MusicPlayerActivity activity)
	{
		_contextRef = activity;
	}
}
