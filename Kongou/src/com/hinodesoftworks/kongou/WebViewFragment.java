package com.hinodesoftworks.kongou;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewFragment extends Fragment
{
	WebView webView;


	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		Activity activity = getActivity();
		webView = (WebView)activity.findViewById(R.id.web_view);
		
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		
		webView.loadUrl("file:///android_asset/htdocs/index.html");  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.web_view_fragment, container, false);
	}
	
}