package com.hinodesoftworks.ki;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BrowserActivity extends Activity implements OnTouchListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		
		return false;
	}

}
