/* 
 * Date: Nov 14, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

// TODO: Auto-generated Javadoc
/**
 * The Class SettingsActivity.
 */
public class SettingsActivity extends Activity 
{
	//shell activity to host preference fragment
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		actionBar.setHomeButtonEnabled(true);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
		switch (menuItem.getItemId())
		{
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		
		}
        return true;
    }
	
}
