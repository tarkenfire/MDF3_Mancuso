/* 
 * Date: Nov 14, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class WidgetConfigActivity.
 */
public class WidgetConfigActivity extends Activity implements OnClickListener, OnSeekBarChangeListener
{
	private int widgetId;
	
	//UI handles
	SeekBar redBar;
	SeekBar greenBar;
	SeekBar blueBar;
	TextView previewView;
	Button createButton;

	public static int currentColor = Color.WHITE;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_config);
		
		this.setResult(RESULT_CANCELED);
		
		//get UI handles
		redBar = (SeekBar)findViewById(R.id.config_red_bar);
		blueBar = (SeekBar)findViewById(R.id.config_blue_bar);
		greenBar = (SeekBar)findViewById(R.id.config_green_bar);
		previewView = (TextView)findViewById(R.id.config_preview);
		createButton = (Button)findViewById(R.id.config_create_button);
		
		//set listeners
		redBar.setOnSeekBarChangeListener(this);
		blueBar.setOnSeekBarChangeListener(this);
		greenBar.setOnSeekBarChangeListener(this);
		createButton.setOnClickListener(this);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras != null) 
		{
		    widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view)
	{
		//there's only one view with this as a listener, so I can spare the switch
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		
		Bundle options = appWidgetManager.getAppWidgetOptions(widgetId);
		int height = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
		RemoteViews widgetViews =  WidgetProvider.getRemoteViews(this, height);
		
		//set color for text views
		widgetViews.setTextColor(R.id.widget_artist_title, currentColor);
		widgetViews.setTextColor(R.id.widget_track_title, currentColor);
		widgetViews.setTextColor(R.id.widget_album_title, currentColor);
		
		switch (WidgetProvider.numberOfCellsFromDP(height))
		{
		case 3:
			widgetViews.setTextColor(R.id.widget_genre_title, currentColor);
			widgetViews.setTextColor(R.id.widget_bitrate_label, currentColor);			
			break;
		case 4:
			widgetViews.setTextColor(R.id.widget_genre_title, currentColor);
			widgetViews.setTextColor(R.id.widget_bitrate_label, currentColor);
			widgetViews.setTextColor(R.id.widget_mimetype_label, currentColor);
			widgetViews.setTextColor(R.id.widget_date_label, currentColor);
			break;
			
		}
		
		//persist color to prefs for widget size changes
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.edit().putInt("pref_color", currentColor).commit();
		
		appWidgetManager.updateAppWidget(widgetId, widgetViews);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, resultValue);
		finish();

	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser)
	{
		if (fromUser)
		{
			currentColor = Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress());
			previewView.setTextColor(currentColor);
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{
		// do nothing
	}

	/* (non-Javadoc)
	 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		// do nothing
	}
	
	
	
}
