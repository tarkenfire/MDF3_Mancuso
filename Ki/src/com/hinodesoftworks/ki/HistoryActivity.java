/* 
 * Date: Oct 31, 2013
 * Project: Ki
 * Package: com.hinodesoftworks.ki
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ki;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * The Class HistoryActivity.
 */
public class HistoryActivity extends Activity implements OnClickListener, OnItemClickListener
{
	//ui handle
	ListView historyList;
	
	//other vars
	ArrayList<String> history;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		//grab UI handle.
		historyList = (ListView)findViewById(R.id.history_list);
		
		Intent i = getIntent();
		history = i.getStringArrayListExtra("history");
		Log.i("array", history.toString());
		
		historyList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history));
		
		historyList.setOnItemClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);        
		finish();
	}


	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent i = new Intent();
		i.putExtra("selected_history", history.get(position));
		setResult(RESULT_OK,i);
		finish();
		
	}



	
}
