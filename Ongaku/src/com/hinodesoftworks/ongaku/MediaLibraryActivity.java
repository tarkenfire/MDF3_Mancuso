/* 
 * Date: Nov 5, 2013
 * Project: Ongaku
 * Package: com.hinodesoftworks.ongaku
 * @author Michael Mancuso
 *
 */
package com.hinodesoftworks.ongaku;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class MediaLibraryActivity.
 */
public class MediaLibraryActivity extends Activity implements OnItemClickListener
{
	public static MimeTypeMap mtm = MimeTypeMap.getSingleton();
	private ArrayList<File> musicFileList;
	
	//ui handles
	ListView mediaList;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_library);
		
		//grab ui handles
		mediaList = (ListView)findViewById(R.id.library_album_list);
		
		//init variables
		musicFileList = new ArrayList<File>();
		
		
		//get music file contents
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				searchDirectoryRecursive(new File("/mnt"));
				Log.i("Total track count", String.valueOf(musicFileList.size()));
				
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						onFileSearchComplete();
						
						
					}
				});
			}
			
		}).start();
	
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.media_library, menu);
		return true;
	}
	
	//interface methods
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		
	}

	
	//private methods
	private void onFileSearchComplete()
	{
		AudioFileArrayAdapter adapter = new AudioFileArrayAdapter(this, R.layout.media_list_item, musicFileList);
		mediaList.setAdapter(adapter);
	}
	
	
	//file system stuff
	/**
	 * Search the directory (in File form) provided, as well as all of it's children.
	 *
	 * @param dir the File object that represents a file directory.
	 */
	private void searchDirectoryRecursive(File dir)
	{
		//note to self: should probably thread this; it will likely take time.
		File[] filesInDir = dir.listFiles();
		
		//all directories will at least have ".", the ref to the folder itself
		//as well as ".." in most cases, the parent folder (except if root)
		if (filesInDir != null)
		{
			File holder;
			for (int i = 0; i < filesInDir.length; i++)
			{
				holder = filesInDir[i];
				
				if (holder.isDirectory()) //if directory, search sub folders
				{
					searchDirectoryRecursive(filesInDir[i]);
				}
				else //is file, check for music file
				{
					String mimeType = this.getFileMimeType(holder);
					
					
					//only care about media file types, front-loading the common ones
					//to short-circuit
					if (mimeType != null && (mimeType.equals("audio/mpeg") 
							|| mimeType.equals("audio/mp4") || mimeType.equals("audio/ogg") 
							|| mimeType.equals("application/flac")))
					{
						musicFileList.add(holder);
					}
					
				}
			}
		}	
	}
	
	/**
	 * Gets the extension of a string-form-uri/url
	 *
	 * @param url the string version of the uri or url
	 * @return the extension of the file string
	 */
	private String getExtension(String url)
	{
		if (url != null && url.length() > 0)
		{
			int query = url.lastIndexOf('?');
			if (query > 0)
			{
				url = url.substring(0, query);
			}
			int filenamePos = url.lastIndexOf('/');
			String filename = 0 <= filenamePos ? url.substring(filenamePos + 1)
					: url;

			if (filename.length() > 0)
			{
				int dotPos = filename.lastIndexOf('.');
				if (0 <= dotPos)
				{
					return filename.substring(dotPos + 1);
				}
			}
		}

		return "";
	}
	
	/**
	 * Gets the file mime type.
	 *
	 * @param file the file
	 * @return the file mime type
	 */
	private String getFileMimeType(File file)
	{
		Uri fileUri = Uri.fromFile(file);
		String ext = this.getExtension(fileUri.toString());
		return mtm.getMimeTypeFromExtension(ext);
	}




}
