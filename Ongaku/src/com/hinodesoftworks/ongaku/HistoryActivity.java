/* 
 * Date: Nov 7, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The Class HistoryActivity.
 */
public class HistoryActivity extends Activity implements OnItemClickListener
{
	ArrayList<File> historyFiles;
	ListView historyList;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		historyList = (ListView)findViewById(R.id.history_list);
		
		historyFiles = new ArrayList<File>();
		
		File history = new File(Environment.getExternalStorageDirectory(), "ongaku_history.txt");
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(history));
			String filePath;
			while ((filePath = reader.readLine()) != null)
			{
				File currSongFile = new File(filePath);
				historyFiles.add(currSongFile);
			}
			reader.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		//grab actionbar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); 
		actionBar.setHomeButtonEnabled(true);
		
		AudioFileArrayAdapter adapter = new AudioFileArrayAdapter(this, R.layout.media_list_item, historyFiles);
		historyList.setAdapter(adapter);
		historyList.setOnItemClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent i = new Intent(this, MusicPlayerActivity.class);
		i.putExtra("file", historyFiles.get(position));
		startActivity(i);
		
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {       
		switch (menuItem.getItemId())
		{
		case android.R.id.home:
			onBackPressed();
			break;
		
		}
        return true;
    }
	

}
