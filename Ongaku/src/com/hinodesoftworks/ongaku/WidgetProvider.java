/* 
 * Date: Nov 14, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

// TODO: Auto-generated Javadoc
/**
 * The Class WidgetProvider.
 */
public class WidgetProvider extends AppWidgetProvider
{
	String tempArtist = "Artist Name";
	String tempTrack = "Track Name";
	String tempAlbum = "Album Name";
	
	String tempGenre = "Genre";
	String tempBitrate = "Fast";
	
	String tempMime = "audio/mp3";
	String tempDate = "1/1/12";

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(MusicService.ACTION_UDPATE_WIDGET))
		{
			tempArtist = intent.getStringExtra("artist");
			tempTrack = intent.getStringExtra("track");
			tempAlbum = intent.getStringExtra("album");
			
			tempGenre = intent.getStringExtra("genre");
			tempBitrate = intent.getStringExtra("bitrate");
			
			tempMime = intent.getStringExtra("mime");
			tempDate = intent.getStringExtra("date");
			
			//onReceive will not fire onUpdate without an intent
			//passing an array of ids, which is why I call it manually
			//regardless of calling super.
			onUpdate(context);
		}
		
		
		super.onReceive(context, intent);
	}
	
	

	/**
	 * 	Underloaded convenience method to call onUpdate easier.
	 *
	 * @param context the context
	 */
	public void onUpdate(Context context)
	{
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
		onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		//capture array length to avoid multiple in loop calls
		final int idLength = appWidgetIds.length;
		
		for (int i = 0; i < idLength; i++)
		{
			int currentId = appWidgetIds[i];
			
			Bundle options = appWidgetManager.getAppWidgetOptions(currentId);
			RemoteViews widgetViews =  getRemoteViews(context, options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
			
			//items on all layouts
			widgetViews.setTextViewText(R.id.widget_artist_title, tempArtist);
			widgetViews.setTextViewText(R.id.widget_track_title, tempTrack);
			widgetViews.setTextViewText(R.id.widget_album_title, tempAlbum);
			
			int numOfRows = numberOfCellsFromDP(options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
			
			switch (numOfRows)
			{
			case 3: //layouts one and two have the same fields in them.
				widgetViews.setTextViewText(R.id.widget_genre_title, tempGenre);
				widgetViews.setTextViewText(R.id.widget_bitrate_label, tempBitrate + "bps");
				break;
			case 4:
				widgetViews.setTextViewText(R.id.widget_genre_title, tempGenre);
				widgetViews.setTextViewText(R.id.widget_bitrate_label, tempBitrate + "bps");
				widgetViews.setTextViewText(R.id.widget_mimetype_label, tempMime);
				widgetViews.setTextViewText(R.id.widget_date_label, tempDate);
				break;
			}
			
			//colors
			//set color for text views
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			int textColor = prefs.getInt("pref_color", Color.WHITE);
			
			widgetViews.setTextColor(R.id.widget_artist_title, textColor);
			widgetViews.setTextColor(R.id.widget_track_title, textColor);
			widgetViews.setTextColor(R.id.widget_album_title, textColor);
			
			switch (numOfRows)
			{
			case 3:
				widgetViews.setTextColor(R.id.widget_genre_title, textColor);
				widgetViews.setTextColor(R.id.widget_bitrate_label, textColor);			
				break;
			case 4:
				widgetViews.setTextColor(R.id.widget_genre_title, textColor);
				widgetViews.setTextColor(R.id.widget_bitrate_label, textColor);
				widgetViews.setTextColor(R.id.widget_mimetype_label, textColor);
				widgetViews.setTextColor(R.id.widget_date_label, textColor);
				break;
				
			}
			
			
			//set intent for button.
			Intent buttonIntent = new Intent(context, MediaLibraryActivity.class);
			PendingIntent buttonPI = PendingIntent.getActivity(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			widgetViews.setOnClickPendingIntent(R.id.widget_open_button, buttonPI);
			
			appWidgetManager.updateAppWidget(currentId, widgetViews);
		}
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.appwidget.AppWidgetProvider#onAppWidgetOptionsChanged(android.content.Context, android.appwidget.AppWidgetManager, int, android.os.Bundle)
	 */
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions)
	{
		int height = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		
		appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, height));
		
		onUpdate(context);
		
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}


	/**
	 * Gets the remote views depending on number of cells in widget
	 *
	 * @param context the context
	 * @param minHeight the min height
	 * @return the remote views
	 */
	public static RemoteViews getRemoteViews(Context context, int minHeight)
	{
		int numOfRows = numberOfCellsFromDP(minHeight);
		
		int layoutID = 0;
		
		switch (numOfRows)
		{
		case 1:
			layoutID = R.layout.widget_layout;
			break;
		case 2:
			layoutID = R.layout.widget_layout_2_row;
			break;
		case 3:
			layoutID = R.layout.widget_layout_3_row;
			break;
		case 4:
			layoutID = R.layout.widget_layout_4_row;
			break;
		}
		
		if (layoutID != 0)
			return new RemoteViews(context.getPackageName(), layoutID);
		else
			return null;
	}
	
	
	//utility methods
	/**
	 * Calculates Number of cells from dp.
	 *
	 * @param dp the dp of each cell
	 * @return the int number of cells in widget
	 */
	public static int numberOfCellsFromDP(int dp)
	{
		//start at 2 to avoid trap in multiplying by 1.
		int numOfCells = 2;
		
		//formula from ADR for home screen cell size from
		//dp == 70 * n - 30, where n is number of cells
		while (70 * numOfCells - 30 < dp)
			++numOfCells;
		
		//result is one higher b/c of starting higher
		return numOfCells - 1;
		
	}

}
