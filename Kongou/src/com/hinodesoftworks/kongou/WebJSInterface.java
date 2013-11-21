package com.hinodesoftworks.kongou;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebJSInterface
{
	private Context _ctx = null;
	
	
	public WebJSInterface(Context ctx)
	{
		_ctx = ctx;
	}
	
	@JavascriptInterface
	public void getAndSendData(String jsonString)
	{
		
	}
	
	@JavascriptInterface
	public void displayToast(String string)
	{
		Toast.makeText(_ctx, string, Toast.LENGTH_SHORT).show();
	}
}
