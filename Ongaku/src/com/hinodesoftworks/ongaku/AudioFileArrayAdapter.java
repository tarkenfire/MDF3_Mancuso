/* 
 * Date: Nov 7, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class AudioFileArrayAdapter.
 */
public class AudioFileArrayAdapter extends ArrayAdapter<File>
{
	int resourceId;
	ArrayList<File> data;
	Context ctx;
	
	//UI handles
	TextView trackNameView;
	TextView trackArtistView;
	TextView trackAlbumView;
	TextView trackDurationView;
	
	
	/**
	 * Instantiates a new audio file array adapter.
	 *
	 * @param context the context
	 * @param resourceId the resource id
	 * @param data the list of files
	 */
	public AudioFileArrayAdapter(Context context, int resourceId, ArrayList<File> data)
	{
		super(context, resourceId, data);
		
		this.resourceId = resourceId;
		this.data = data;
		this.ctx = context;
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		//only inflate when there is a need for a new cell
		if (convertView == null)
		{
			LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
			convertView = inflater.inflate(resourceId, parent, false);
		}
		
		File mediaHolder = data.get(position);
		
		trackNameView = (TextView)convertView.findViewById(R.id.media_list_item_track_name);
		trackArtistView = (TextView)convertView.findViewById(R.id.media_list_item_artist_name);
		trackAlbumView = (TextView)convertView.findViewById(R.id.media_list_item_album_name);
		trackDurationView = (TextView)convertView.findViewById(R.id.media_list_item_duration);
		
		
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(mediaHolder.getAbsolutePath());
		
		trackNameView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
		trackArtistView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
		trackAlbumView.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
		
		trackDurationView.setText(getDurationStringFromMilSeconds(Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))));
		
		
		
		return convertView;
	}
	
	
	//utility methods
	/**
	 * Converts milliseconds into a formated time string.
	 *
	 * @param duration the milliseconds
	 * @return the duration string formatted
	 */
	public static String getDurationStringFromMilSeconds(int duration)
	{
		StringBuilder sb = new StringBuilder();
		int milSeconds = duration,
		hours = ((milSeconds / (1000*60*60)) % 24), // mils / (mins calc below) 24 hours in a day 
		minutes = ((milSeconds / (1000*60)) % 60), //mils / (mils calc below).60 mins to an hour  
		seconds = (milSeconds / 1000) % 60; //1000 ms to a min, 60 secs to a min. 
		
		
		//finally, construct string - disregard millisecond precision.
		if (hours > 0)
		{
			sb.append(hours); 
			sb.append(":");
		}
		
		//normalize minutes if media file longer than 1 hour (podcast, dj mix)
		if (hours > 1 && minutes < 10)
			sb.append("0" + String.valueOf(minutes));
		else
			sb.append(minutes);
		
		sb.append(":");
		
		//normalize seconds
		if (seconds < 10)
			sb.append("0" + String.valueOf(seconds));
		else
			sb.append(seconds);
		
		return sb.toString();
	}


}
