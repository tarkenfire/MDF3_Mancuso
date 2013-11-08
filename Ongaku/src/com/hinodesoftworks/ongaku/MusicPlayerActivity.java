/* 
 * Date: Nov 5, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;

import com.hinodesoftworks.ongaku.MusicService.MusicBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class MusicPlayerActivity.
 */
public class MusicPlayerActivity extends Activity implements OnClickListener, OnSeekBarChangeListener
{
	
	//general vars
	MusicService musicService;
	boolean isServiceBound = false;
	public boolean isActivityNew = false;
	
	//ui handles
	ImageView albumArtView;
	SeekBar progressBar;
	
	TextView currentTimeDisplay;
	TextView totalTimeDisplay;
	
	TextView trackNameView;
	TextView artistNameView;
	TextView albumNameView;
	
	ImageButton playerBackButton;
	ImageButton playerPlayPauseButton;
	ImageButton playerForwardButton;

	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);
		
		isActivityNew = (savedInstanceState == null);
		
		//grab ui handles
		albumArtView = (ImageView)findViewById(R.id.player_album_art_view);
		progressBar = (SeekBar)findViewById(R.id.player_progress_bar);
		currentTimeDisplay = (TextView)findViewById(R.id.player_current_time_display);
		totalTimeDisplay = (TextView)findViewById(R.id.player_total_time_display);
		trackNameView = (TextView)findViewById(R.id.player_track_name_view);
		artistNameView = (TextView)findViewById(R.id.player_artist_name_view);
		albumNameView = (TextView)findViewById(R.id.player_album_name_view);
		playerBackButton = (ImageButton)findViewById(R.id.player_back_button);
		playerPlayPauseButton = (ImageButton)findViewById(R.id.player_play_pause_button);
		playerForwardButton = (ImageButton)findViewById(R.id.player_forward_button);
		
		//set handlers
		playerPlayPauseButton.setOnClickListener(this);
		playerBackButton.setOnClickListener(this);
		playerForwardButton.setOnClickListener(this);
		progressBar.setOnSeekBarChangeListener(this);
		
		if (savedInstanceState == null)
		{
			//get file for music track to play
			Intent i = this.getIntent();
			File musicFile = (File) i.getSerializableExtra("file");
			
			//get and set fields from track
			MediaMetadataRetriever mmr = new MediaMetadataRetriever();
			mmr.setDataSource(musicFile.getAbsolutePath());
			
			trackNameView
					.setText(mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
			artistNameView
					.setText(mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
			albumNameView
					.setText(mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
			totalTimeDisplay
					.setText(AudioFileArrayAdapter.getDurationStringFromMilSeconds(Integer.valueOf(mmr
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));
			
			byte[] imageBytes = mmr.getEmbeddedPicture();
			if (imageBytes != null)
			{
				albumArtView.setImageDrawable(new BitmapDrawable(this
						.getResources(), BitmapFactory.decodeByteArray(
						imageBytes, 0, imageBytes.length)));
			}
			//service will be both started AND bound, to allow for
			//leaving the activity while still running the service
			//while still having a binder to access the service.
			Intent serviceIntent = new Intent(this, MusicService.class);
			this.startService(serviceIntent);
		}
		//set action bar icon as backward navigation.
		//TODO doesn't work.
	    getActionBar().setDisplayHomeAsUpEnabled(true);		
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		Intent serviceIntent = new Intent(this, MusicService.class);
		this.bindService(serviceIntent, serviceConnection, 0);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		if (isServiceBound)
		{
			unbindService(serviceConnection);
			isServiceBound = false;
		}
	}
	
	//on destroy may not always be called, but if it is, stop the service.
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Intent serviceIntent = new Intent(this, MusicService.class);
		this.stopService(serviceIntent);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.player_back_button:
			musicService.seekBackwardFiveSeconds();
			break;
		
		case R.id.player_play_pause_button:
			if (musicService.isMusicPlaying())
			{
				musicService.pauseTrack();
			}
			else
			{
				musicService.pauseTrack();
				musicService.playTrack();
			}
			break;
			
		case R.id.player_forward_button:
			musicService.seekForwardFiveSeconds();
			break;
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		if (fromUser)
			musicService.seekToPoint(progress);
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		//do nothing
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		//do nothing
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_library, menu);
		return true;
	}
	
	//public methods
	public void setCurrentDuration(int milSeconds)
	{
		currentTimeDisplay.setText(AudioFileArrayAdapter.getDurationStringFromMilSeconds(milSeconds));
		progressBar.setProgress(milSeconds);
	}
	
	public void onPlaybackStarted(int milSeconds)
	{
		totalTimeDisplay.setText(AudioFileArrayAdapter.getDurationStringFromMilSeconds(milSeconds));
		progressBar.setMax(milSeconds);
	}
	
	//private methods
	private void onServiceBound()
	{
		Intent i = this.getIntent();
		File musicFile = (File) i.getSerializableExtra("file");
		musicService.setPlayerInstanceReference(this);
		musicService.setFileReference(musicFile);
		musicService.prepareMediaPlayer(musicFile.getAbsolutePath());
	}
	
	//service connection class to connect with binder from service
	
	private ServiceConnection serviceConnection = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			MusicBinder binder = (MusicBinder)service;
			musicService = binder.getService();
			isServiceBound = true;
			onServiceBound();
			
		}

		@Override
		public void onServiceDisconnected(ComponentName className)
		{
			isServiceBound = false;	
		}
		
	};



	

}
