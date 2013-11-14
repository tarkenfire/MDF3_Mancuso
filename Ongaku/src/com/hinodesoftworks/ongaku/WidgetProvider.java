package com.hinodesoftworks.ongaku;

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
			Log.i("intent", "intent hit");
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
			
			appWidgetManager.updateAppWidget(currentId, widgetViews);
		}
		
	}

}
