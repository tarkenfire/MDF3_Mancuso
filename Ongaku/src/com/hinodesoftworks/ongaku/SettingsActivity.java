/* 
 * Date: Nov 11, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceClickListener, OnSharedPreferenceChangeListener
{
	EditTextPreference defaultDirectoryPref;
	Preference clearHistoryPref;
	
	@SuppressWarnings("deprecation")
	//Fragmenting the entire application to handle a settings  didn't
	//seem like a good use of my time. I should have fragmented from the start, but
	//because I didn't, I simply made it work for now.
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		//get preference references
		defaultDirectoryPref = (EditTextPreference)findPreference("pref_default_directory");
		clearHistoryPref = (Preference)findPreference("pref_clear_history");

		//set listeners
		defaultDirectoryPref.setOnPreferenceClickListener(this);
		clearHistoryPref.setOnPreferenceClickListener(this);
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference preference)
	{
		if(preference.getKey().equals("pref_clear_history"))
		{
			File history = new File(Environment.getExternalStorageDirectory(), "ongaku_history.txt");
			history.delete(); //return value doesn't matter in this case
			Toast.makeText(this, R.string.toast_history_cleared, Toast.LENGTH_LONG).show();
		}
		
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key)
	{
		//change summary for directory pref
		if (key.equals("pref_default_directory"))
		{
			defaultDirectoryPref.setSummary("Currently set to: " + sharedPreferences.getString("pref_default_directory", "MUFFINS"));
		}
		
	}
}
