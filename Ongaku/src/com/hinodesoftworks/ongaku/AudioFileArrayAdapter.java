/* 
 * Date: Nov 5, 2013
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
	
	
	public AudioFileArrayAdapter(Context context, int resourceId, ArrayList<File> data)
	{
		super(context, resourceId, data);
		
		this.resourceId = resourceId;
		this.data = data;
		this.ctx = context;
		
	}

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
	public String getDurationStringFromMilSeconds(int duration)
	{
		StringBuilder sb = new StringBuilder();
		int milSeconds = duration;
		int hours = 0, minutes = 0, seconds = 0;
		
		//get seconds - 1000ms to a second.
		while (milSeconds > 1000)
		{
			seconds++;
			milSeconds -= 1000;
		}
		
		//get minutes 1 to 60 seconds
		while (seconds > 60)
		{
			minutes++;
			seconds -= 60;
		}
		
		//get hours
		while (minutes > 60)
		{
			hours++;
			minutes -= 60;
		}
		
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
