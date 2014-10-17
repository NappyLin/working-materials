package com.downloadmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.ProgressBar;


public final class DownloadConsts{	
	/** 用来存放每一个正在进行的下载任务 */
	
	public static List<String> listUrl = new ArrayList<String>();
	public static Map<String, Integer> mapPosition = new HashMap<String, Integer>();
	
	public static Map<String, DownloadTask> mapTask = new HashMap<String, DownloadTask>();
	
	//public static String osVersion = Build.VERSION.RELEASE;
}

