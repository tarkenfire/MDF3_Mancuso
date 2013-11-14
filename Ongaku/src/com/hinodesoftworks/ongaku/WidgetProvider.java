/* 
 * Date: Nov 14, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider
{
	String tempArtist = "Artist Name";
	String tempTrack = "Track Name";
	String tempAlbum = "Album Name";
	

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(MusicService.ACTION_UDPATE_WIDGET))
		{
			tempArtist = intent.getStringExtra("artist");
			tempTrack = intent.getStringExtra("track");
			//tempImage = intent.getParcelableExtra("image");
			
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
			
			Bundle options = appWidgetManager.getAppWidgetOptions(currentId);
			RemoteViews widgetViews =  getRemoteViews(context, options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT));
			
			widgetViews.setTextViewText(R.id.widget_artist_title, tempArtist);
			widgetViews.setTextViewText(R.id.widget_track_title, tempTrack);
			
			
			
			//set intent for button.
			Intent buttonIntent = new Intent(context, MediaLibraryActivity.class);
			PendingIntent buttonPI = PendingIntent.getActivity(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			widgetViews.setOnClickPendingIntent(R.id.widget_open_button, buttonPI);
			
			appWidgetManager.updateAppWidget(currentId, widgetViews);
		}
		
	}
	
	
	
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions)
	{
		int height = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		
		appWidgetManager.updateAppWidget(appWidgetId, getRemoteViews(context, height));

		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}


	private RemoteViews getRemoteViews(Context context, int minHeight)
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
