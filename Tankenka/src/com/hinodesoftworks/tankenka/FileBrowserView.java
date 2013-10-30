/* 
 * Date: Oct 28, 2013
 * Project: TankenkaFileBrowser
 * Package: com.hinodesoftworks.tankenka
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.tankenka;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FileBrowserView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_browser_view, menu);
		return true;
	}

}
