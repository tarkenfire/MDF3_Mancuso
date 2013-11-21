package com.hinodesoftworks.kongou;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class WebJSInterface
{
	private Context _ctx = null;
	
	
	public WebJSInterface(Context ctx)
	{
		_ctx = ctx;
	}
	
	@JavascriptInterface
	public void getData()
	{
		
	}
}
