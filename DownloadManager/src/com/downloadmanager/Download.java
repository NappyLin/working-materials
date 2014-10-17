package com.downloadmanager;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.DownloadManager;  
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class Download {
	
	private static final String TAG = "Download";
	
	private DownloadManager downloadManager;
	private SharedPreferences prefs;
	
	private static final String DL_ID = "downloadId";
	private static final String DL_URL= "downloadUrl";
	private static final String DL_TargetFile = "downloadFile";
	private static final String DL_StartTime = "downloadTime";
	private static final String DL_State = "downlaodState";
	private static final String DL_MimeType = "downloadMimeType";
	private static final String DL_Error = "downloadError";
	private static final String DL_Bytes = "downloadTotalBytes";
	private final Activity mMainActivity;
	
	public Download(Activity activity){
		mMainActivity = activity;
		downloadManager = (DownloadManager) mMainActivity.getSystemService(android.content.Context.DOWNLOAD_SERVICE);
		 prefs = PreferenceManager.getDefaultSharedPreferences(mMainActivity);
	}
	
	public long createNewDownload(String url, String fileName, String fileTitle){
		Uri resource = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);

		//MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		//String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
		//request.setMimeType(mimeString);

		request.setVisibleInDownloadsUi(true);
		
		if(makeDir("downlaod")){
		request.setDestinationInExternalPublicDir("/download/",fileName);
		}
		request.setTitle(fileTitle);
		long id = downloadManager.enqueue(request);
		//String mimeType = downloadManager.getMimeTypeForDownloadedFile(id);
		Log.v(TAG,"download enqueue "+" "+id);
		return id;
	}
	public boolean makeDir(String name){
    	File folder = new File(name);
    	return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
	public void startDownload(long id){
		
	}
	public void pauseDownload(long id){
		
	}
	public void resumeDownload(long id){
		
	}
	public void cancelDownload(long id){
		
	}
	public void removeDownloads(long[] ids){
		
	}
	public void removeAll(){
		
	}
}
