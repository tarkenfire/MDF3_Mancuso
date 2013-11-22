/* 
 * Date: Nov 21, 2013
 * Project: Kongou
 * Package: com.hinodesoftworks.kongou
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.kongou;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class WebJSInterface.
 */
public class WebJSInterface
{
	private Context _ctx = null;
	
	
	/**
	 * Instantiates a new web js interface.
	 *
	 * @param ctx the ctx
	 */
	public WebJSInterface(Context ctx)
	{
		_ctx = ctx;
	}
	
	/**
	 * Gets the JSON data from the JS, parses it, and sends an intent with the data
	 *
	 * @param jsonString the json string
	 * @return the and send data
	 */
	@JavascriptInterface
	public void getAndSendData(String jsonString)
	{
		Intent senderIntent = new Intent(Intent.ACTION_SEND);
		String subject = "", body ="";
		String[] sendto = new String[1];
		String[] ccList = new String[3];
		
		try
		{
			JSONObject jsonObject = new JSONObject(jsonString);
			Log.i("JSON", jsonObject.toString());
			
			//set objects from 
			subject = jsonObject.getString("subject");
			sendto[0] = jsonObject.getString("sendTo");
			body = jsonObject.getString("body");
			
			//only @ means that there was no input in the CC fields
			if (!jsonObject.getString("carbon1").equals("@"))
				ccList[0] = jsonObject.getString("carbon1");

			if (!jsonObject.getString("carbon2").equals("@"))
				ccList[1] = jsonObject.getString("carbon2");
			
			if (!jsonObject.getString("carbon1").equals("@"))
				ccList[2] = jsonObject.getString("carbon3");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		//even in exception state, the intent can still just launch a blank email app.
		senderIntent.putExtra(android.content.Intent.EXTRA_EMAIL, sendto);  
		senderIntent.putExtra(android.content.Intent.EXTRA_CC, ccList);    
		senderIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);  
		senderIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);  
		
		senderIntent.setType("plain/text");
		
		//fire intent chooser
		_ctx.startActivity(Intent.createChooser(senderIntent, "Send your email using:"));
	}
	
	/**
	 * Display a toast originating from the Javascript.
	 *
	 * @param string the string
	 */
	@JavascriptInterface
	public void displayToast(String string)
	{
		Toast.makeText(_ctx, string, Toast.LENGTH_SHORT).show();
	}
}
