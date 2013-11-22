/* 
 * Date: Nov 21, 2013
 * Project: Kongou
 * Package: com.hinodesoftworks.kongou
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.kongou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * The Class WebViewFragment.
 */
public class WebViewFragment extends Fragment
{
	private final String uiUrl = "http://www.tarkenfire.com/personal/htdocs/index.html";
	
	WebView webView;


	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		Activity activity = getActivity();
		webView = (WebView)activity.findViewById(R.id.web_view);
		
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		
		webView.addJavascriptInterface(new WebJSInterface(this.getActivity()), "NativeInterface");
		
		//check connectivity
		ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		if (isConnected)
		{
			webView.loadUrl(uiUrl);
		}  
		else
		{
			Toast.makeText(getActivity(), R.string.no_net_error, Toast.LENGTH_SHORT).show();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.web_view_fragment, container, false);
	}
	
}
