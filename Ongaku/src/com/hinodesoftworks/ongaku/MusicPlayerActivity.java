/* 
 * Date: Nov 5, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class MusicPlayerActivity.
 */
public class MusicPlayerActivity extends Activity
{
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
		
		//get file for music track to play
		Intent i = this.getIntent();
		File musicFile = (File) i.getSerializableExtra("file");
		
		
		//get and set fields from track
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(musicFile.getAbsolutePath());
		
		trackNameView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
		artistNameView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
		albumNameView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
		
		totalTimeDisplay.setText(AudioFileArrayAdapter.getDurationStringFromMilSeconds(Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));
		
		byte[] imageBytes = mmr.getEmbeddedPicture();
		
		if (imageBytes != null)
		{
			albumArtView.setImageDrawable(new BitmapDrawable(this.getResources() ,BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length)));
		}
		
		
		
		
		
		
		//TODO start music playing
		
		
		//set action bar icon as backward navigation.
		//TODO doesn't work.
	    getActionBar().setDisplayHomeAsUpEnabled(true);		
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
	
	

}
