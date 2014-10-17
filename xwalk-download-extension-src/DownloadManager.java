package org.xwalk.core.internal.extension.api.download;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;
import org.xwalk.core.internal.extension.api.messaging.Download;

public class DownloadManager {
	private final static String TAG = "DownloadManager";
	private final static String EXTRA_MSGID = "asyncCallId";
	private final static String EXTRA_MSGINSTANCEID = "instanceid";
	private final Activity mMainActivity;
	private final Download mDownloadHandler;

	public DownloadManager(Activity activity, Download download) {
		mMainActivity = activity;
		mDownloadHandler = download;
	}

	public void onStart(int instanceID, JSONObject jsonMsg){

	}

	public void onPause(int instanceID, JSONObject jsonMsg){

	}
	public void onResume(int instanceID, JSONObject jsonMsg){

	}
	public void onCancel(int instanceID, JSONObject jsonMsg){

	}

	public void getDownloadRequest(int instanceID, JSONObject jsonMsg){

	}

	public void getNetworkType(int instanceID, JSONObject jsonMsg){

	}

	public void getMIMEType(int instanceID, JSONObject jsonMsg){

	}

	public void getState(int instanceID, JSONObject jsonMsg){

	}
}