package com.hinodesoftworks.ki;

import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class BrowserActivity extends Activity implements OnTouchListener, OnClickListener
{
	//current histories
	Stack<String> backHistory;
	Stack<String> forwardHistory;
	
	//histories to persist
	ArrayList<String> historyToSave;
	
	//ui handles
	RelativeLayout container;
	WebView browserWindow;
	EditText navigationField;
	ImageButton backButton;
	ImageButton forwardButton;
	ImageButton historyButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		
		//grab UI handles
		container = (RelativeLayout)findViewById(R.id.browser_activity_container);
		browserWindow = (WebView)findViewById(R.id.browser_web_view);
		navigationField = (EditText)findViewById(R.id.navigation_field);
		backButton = (ImageButton)findViewById(R.id.nav_back_button);
		forwardButton = (ImageButton)findViewById(R.id.nav_forward_button);
		historyButton = (ImageButton)findViewById(R.id.nav_history_button);
		
		//set listeners
	
		//container.setOnTouchListener(this);
		backButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		historyButton.setOnClickListener(this);
		
		//set handlers
		navigationField.setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                            KeyEvent event)
            {
            		browseToUrl(createUrl(v.getText().toString()));
                    return true;
            }
        });
		
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

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
	}
	
	//private methods
	private URL createUrl(String input)
	{
		String urlRegex = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";
		//                 http/s  :// (or none) any # of letters . any letters . /any sub-dirs/tlds
		
		
		//TODO TESTING CODE. REPLACE
		browserWindow.setWebViewClient(new WebViewClient(){@Override public boolean shouldOverrideUrlLoading (WebView view, String url){return false;}});
		
		if (input.matches(urlRegex))
		{
			Log.i("regex", "match");
			browserWindow.loadUrl("http://www.google.com");
		}
		else
		{
			Log.i("regex", "no match");
			browserWindow.loadUrl("http://yahoo.com");
		}
		
		return null;
	}
	
	private void browseToUrl(URL url)
	{
		
	}

}
