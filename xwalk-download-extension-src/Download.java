package org.xwalk.core.internal.extension.api.download;

import android.app.Activity;
import android.net.Uri;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.internal.extension.api.messaging.DownloadManager;
import org.xwalk.core.internal.extension.XWalkExtension;
import org.xwalk.core.internal.extension.XWalkExtensionContext;

interface Command {
	void runCommand(int instanceID, JSONObject jsonMsg);
}

public class Download extends XWalkExtension {
	public static final String JS_API_PATH = "jsapi/download_api.js";

	private static final String NAME ="xwalk.experimental.download";

	private static HashMap<String , Command> sMethodMap = new HashMap<String , Command> ();

	private DownloadManager  mDownloadManager;

	public Download (String jsApiContent, XWalkExtensionContext context){
		super(NAME, jsApiContent, context);
		mDownloadManager = new DownloadManager (mExtensionContext.getActivity(), this);
		initMethodMap();
	}

	private void initMethodMap(){

		sMethodMap.put("download_start", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.onStart(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_pause", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.onPause(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_resume", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.onResume(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_cancel", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.onCancel(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_getDownloadRequest", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.getDownloadRequest(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_getNetworkType", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.getNetworkType(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_getMIMEType", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.getMIMEType(instanceID, jsonMsg);
			}
		});

		sMethodMap.put("download_getState", new Command(){
			public void runCommand(int instanceID, JSONObject jsonMsg){
				mDownloadManager.getState(instanceID, jsonMsg);
			}
		});

	}

	private String getCommandString (String message){
		if (message.isEmpty()){
			return "";
		}

		try{
			return new JSONObject(message).getString("cmd");
		} catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	@override
	public void onMessage(int instanceID, String message){
		Command command = sMethodMap.get(getCommandString(message));

		if(null != command){
			try{
				command.runCommand(instanceID, new JSONObject(message));
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
} 