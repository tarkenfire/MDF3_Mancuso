package com.hinodesoftworks.ki;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Stack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class BrowserActivity extends Activity implements OnTouchListener, OnClickListener
{	
	//ui handles
	RelativeLayout container;
	WebView browserWindow;
	EditText navigationField;
	ImageButton backButton;
	ImageButton forwardButton;
	ImageButton historyButton;
	ProgressBar progressBar;
	
	//touch Ui handles
	ImageButton touchModeButton;
	Button touchCancelButton;
	RelativeLayout touchCanvas;
	TextView backSlide;
	TextView forwardSlide;
	
	//general variables
	Stack<String> history;
	
	//variables needed for touch events
	GestureDetector gestureDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_browser);
		
		history = new Stack<String>();
		gestureDetector = new GestureDetector(this, new BrowserGestureDetector());
		
		//grab UI handles
		container = (RelativeLayout)findViewById(R.id.browser_activity_container);
		browserWindow = (WebView)findViewById(R.id.browser_web_view);
		navigationField = (EditText)findViewById(R.id.navigation_field);
		backButton = (ImageButton)findViewById(R.id.nav_back_button);
		forwardButton = (ImageButton)findViewById(R.id.nav_forward_button);
		historyButton = (ImageButton)findViewById(R.id.nav_history_button);
		progressBar = (ProgressBar)findViewById(R.id.progress_bar);
		touchModeButton = (ImageButton)findViewById(R.id.touch_mode_button);
		touchCancelButton = (Button)findViewById(R.id.touch_mode_cancel_button);
		touchCanvas = (RelativeLayout)findViewById(R.id.touch_canvas);
		backSlide = (TextView)findViewById(R.id.back_slide_bar);
		forwardSlide = (TextView)findViewById(R.id.forward_slide_bar);
		
		
		//set listeners
	
		touchCanvas.setOnTouchListener(this);
		backButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		historyButton.setOnClickListener(this);
		touchModeButton.setOnClickListener(this);
		touchCancelButton.setOnClickListener(this);
		
		//set handlers
		navigationField.setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                            KeyEvent event)
            {
            		createAndLoadURL(v.getText().toString());
                    return false;
            }
        });
		
		
		browserWindow.setWebViewClient(new BrowserViewClient());
		browserWindow.setWebChromeClient(new BrowserChromeClient());
		
		//check if this was sent as an implicit intent
		Intent i = this.getIntent();
		if (i.getAction().equals(Intent.ACTION_VIEW))
		{
			createAndLoadURL(i.getStringExtra("url"));
		}
		
		//set state of buttons
		checkHistories();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		Log.i("result", resultCode == RESULT_OK ? "Ok" : "Not Ok");
		
		if (resultCode == RESULT_OK)
		{
			createAndLoadURL(data.getStringExtra("selected_history"));
		}
	}
	
	
	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.nav_back_button:
			browserWindow.goBack();
			if (!history.empty())
				history.pop();
			break;
		case R.id.nav_forward_button:
			browserWindow.goForward();
			break;
		case R.id.nav_history_button:
		{
			if (history.empty())
			{
				Toast.makeText(this, R.string.error_no_history, Toast.LENGTH_SHORT).show();
			}
			else
			{
				//I use a stack then pop them into an array to maintain the correct
				//chronological order
				ArrayList<String> historyItems = new ArrayList<String>();
				
				Log.i("array", history.toString());
				
				do 
				{
					historyItems.add(history.pop());
				} while(!history.empty());
				
				//this is a hacky way to deal with a bug where all of 
				//my history is duplicated in the stack for some reason
				//(sets don't allow duplicates)
				LinkedHashSet<String> duplicateFilter = new LinkedHashSet<String>();
				duplicateFilter.addAll(historyItems);
				historyItems.clear();
				historyItems.addAll(duplicateFilter);
				
				Intent i = new Intent(this, HistoryActivity.class);
				i.putStringArrayListExtra("history", historyItems);
				
				this.startActivityForResult(i, 0);
			}
			
			
			break;
		}
		case R.id.touch_mode_button:
			touchCanvas.setVisibility(View.VISIBLE);
			
			//close keyboard.
			InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(navigationField.getWindowToken(), 0);
			
			break;
		case R.id.touch_mode_cancel_button:
			touchCanvas.setVisibility(View.GONE);
			break;
			
		}
		
		checkHistories();
	}
	
	//private methods
	private void createAndLoadURL(String input)
	{	
		String urlRegex = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$";
		//                 http/s  :// (or none) any # of letters . any letters . /any sub-dirs/tlds
		// matches: google.com, www.google.com, http://www.google.com, http://google.com, play.google.com
		// non-matches ftp://www.google.com, dog, muffins.
		
		if (input.matches(urlRegex))
		{
			Log.i("regex", "match");
			//check for protocol and add if missing.
			if (!input.startsWith("http://") && !input.startsWith("https://"))
			{
				browserWindow.loadUrl("http://" + input);
				history.push("http://" + input);
			}
			else
			{
				browserWindow.loadUrl(input);
				history.push(input);
			}
			
			
		}
		else
		{
			Log.i("regex", "no match");
			//google parses + signs into whitespace sent to their search url.
			//so I don't need to do it in my code.
			browserWindow.loadUrl("http://www.google.com/search?q=" + input);
			
			history.push("http://www.google.com/search?q=" + input);
		}
		
		checkHistories();
	}
	
	private void refreshPage()
	{
		browserWindow.reload();
	}
	
	private void closePage()
	{
		browserWindow.loadUrl("about:blank");
	}
	
	private void checkHistories()
	{
		//check forward history.
		if (browserWindow.canGoForward())
			forwardButton.setEnabled(true);
		else
			forwardButton.setEnabled(false);
		
		//check backward history
		if (browserWindow.canGoBack())
			backButton.setEnabled(true);
		else
			backButton.setEnabled(false);
	}
	
	//private classes / async tasks
	private class BrowserChromeClient extends WebChromeClient
	{
		@Override
		public void onProgressChanged(WebView view, int progress) 
		{
			progressBar.setProgress(progress);
			
			
			//set to 0 if done
			if (progress == 100)
			{
				progressBar.setProgress(0);
			}
		}
		
		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			setTitle("Ki - " + title);
		}
		
	}
	
	private class BrowserViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView v, String url)
		{
			//this is a full web browser; and all non http urls will not be loaded
			//therefore, never override URL loading
			
			return false;
		}
		
		@Override
		public void onPageFinished(WebView view, String url)
		{
			navigationField.setText(url);
		}
	}
	
	private class BrowserGestureDetector extends SimpleOnGestureListener
	{	
		@Override
		public boolean onDown(MotionEvent e)
		{
			return true;
		}
		
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
        {
			if (velocityX > 0)
			{
				Log.i("fling", "right");
				refreshPage();
				touchCanvas.setVisibility(View.GONE);
				
			}
			else
			{
				Log.i("fling", "left");
				closePage();
				touchCanvas.setVisibility(View.GONE);
			}        	
			
        	return true;
        }

	}
	
	
	
}
