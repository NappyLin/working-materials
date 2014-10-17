package com.downloadmanager;

//import java.io.File;
//import java.util.Date;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.app.DownloadManager;  
//import android.content.BroadcastReceiver;
import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.net.Uri;
//import android.preference.PreferenceManager;
import android.util.Log;
import android.os.StrictMode;
//import android.webkit.MimeTypeMap;

public class MainActivity extends Activity {
	
	private static final String TAG = "DldActivity";

	private DownloadTask downloadTask;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        
        
        String url= "http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk";
        //String fileName = "meilishuo.apk";
        String fileTitle =  "meilishuo";
        Log.v(TAG,"create successful");
        downloadTask = new DownloadTask(url,fileTitle,this);
        Log.v(TAG,"create successful");
        long id=downloadTask.getDownloadId();
        Log.v(TAG,"hah, new download has been created now"+ id);
        
        downloadTask.start();
        //downloadTask.pause();
    }
}
