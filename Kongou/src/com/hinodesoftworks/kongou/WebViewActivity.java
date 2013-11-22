/* 
 * Date: Nov 21, 2013
 * Project: Kongou
 * Package: com.hinodesoftworks.kongou
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.kongou;

import android.os.Bundle;
import android.app.Activity;
/**
 * The Class WebViewActivity.
 */
public class WebViewActivity extends Activity
{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
