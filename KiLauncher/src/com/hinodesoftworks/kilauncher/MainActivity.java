package com.hinodesoftworks.kilauncher;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener
{
	EditText input;
	Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		input = (EditText)findViewById(R.id.input);
		button = (Button)findViewById(R.id.test_button);
		
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.putExtra("url", input.getText().toString());
		i.setAction(Intent.ACTION_VIEW);
		startActivity(i); 
	}

}
