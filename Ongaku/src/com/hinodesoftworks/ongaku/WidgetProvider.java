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
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider
{
	String tempArtist = "";
	String tempTrack = "";
	boolean tempIsPlaying = false;
	

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(MusicService.ACTION_UDPATE_WIDGET))
		{
			tempArtist = intent.getStringExtra("artist");
			tempTrack = intent.getStringExtra("track");
			
			//onReceive will not fire onUpdate without an intent
			//passing an array of ids, which is why I call it manually
			//regardless of calling super.
			onUpdate(context);
		}
		
		
		super.onReceive(context, intent);
	}
	
	
	//underloaded convience method to call onUpdate easier.
	public void onUpdate(Context context)
	{
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
		onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		//capture array length to avoid multiple in loop calls
		final int idLength = appWidgetIds.length;
		
		for (int i = 0; i < idLength; i++)
		{
			int currentId = appWidgetIds[i];
			
			//TODO REPLACE WITH FLEXABLE LAYOUT CODE.
			RemoteViews widgetViews =  new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			
			widgetViews.setTextViewText(R.id.widget_artist_title, tempArtist);
			widgetViews.setTextViewText(R.id.widget_track_title, tempTrack);
			
			//set intent for button.
			Intent buttonIntent = new Intent(context, MediaLibraryActivity.class);
			PendingIntent buttonPI = PendingIntent.getActivity(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			widgetViews.setOnClickPendingIntent(R.id.widget_open_button, buttonPI);
			
			appWidgetManager.updateAppWidget(currentId, widgetViews);
		}
		
	}
	
	
	//utility methods
	private static int numberOfCellsFromDP(int dp)
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
