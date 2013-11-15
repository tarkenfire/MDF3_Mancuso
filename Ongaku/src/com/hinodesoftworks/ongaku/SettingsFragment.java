/* 
 * Date: Nov 14, 2013
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
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class SettingsFragment.
 */
public class SettingsFragment extends PreferenceFragment implements OnPreferenceClickListener, OnSharedPreferenceChangeListener
{
	EditTextPreference defaultDirectoryPref;
	Preference clearHistoryPref;
	
	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
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
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	
	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		//change summary for directory pref
		if (key.equals("pref_default_directory"))
		{
			defaultDirectoryPref.setSummary("Currently set to: " + sharedPreferences.getString("pref_default_directory", "MUFFINS"));
		}
		
	}

	/* (non-Javadoc)
	 * @see android.preference.Preference.OnPreferenceClickListener#onPreferenceClick(android.preference.Preference)
	 */
	@Override
	public boolean onPreferenceClick(Preference preference)
	{
		if(preference.getKey().equals("pref_clear_history"))
		{
			File history = new File(Environment.getExternalStorageDirectory(), "ongaku_history.txt");
			history.delete(); //return value doesn't matter in this case
			Toast.makeText(this.getActivity().getApplicationContext(), R.string.toast_history_cleared, Toast.LENGTH_LONG).show();
		}
		
		return true;
	}
	
}
