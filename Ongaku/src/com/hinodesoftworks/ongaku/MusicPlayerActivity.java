package com.hinodesoftworks.ongaku;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MusicPlayerActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);


		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_library, menu);
		return true;
	}

}
