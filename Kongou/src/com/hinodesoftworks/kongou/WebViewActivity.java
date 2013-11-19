package com.hinodesoftworks.kongou;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WebViewActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//I am fragmenting the app as a best practice thing
		//despite this app being so simple as to not really 
		//need a fragment, especially since the UI is handled
		//in a web view.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
	}

}
