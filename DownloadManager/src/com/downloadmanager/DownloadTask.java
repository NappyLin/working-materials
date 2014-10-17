package com.downloadmanager;

//import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.downloadmanager.bean.DBHelper;

public class DownloadTask {
	 /* 用于查询数据库 */  
    private DBHelper dbHelper = null;
    
    private static final String TAG="downloadTask";
	private Context context;
	private long id;
	private String sourceUrl;
	private String fileName;
	private String fileTitle;
	private boolean finished = false;
	private boolean paused = false;
	private boolean canceled = false;
	
	private int curSize = 0;
	private int totalSize = 0;
	private int startPosition =0;
	
	public DownloadTask(String strUrl, String title,Context ctext){
		context = ctext;
		dbHelper = new DBHelper(context);
		
		sourceUrl = strUrl;
		this.fileTitle = title;
		startPosition = 0;
		
		String path = Environment.getExternalStorageDirectory().getPath();
		fileName = sourceUrl.substring(sourceUrl.lastIndexOf('/'));
		
		totalSize = getDownloadTotalSize(sourceUrl);
		//Log.v(TAG, "hi here"+ totalSize);
		boolean isHas = false;
		for (String urlString : DownloadConsts.listUrl) {
			if (sourceUrl.equalsIgnoreCase(urlString)) {
				isHas = true;
				break;
				}
			}
		if (false == isHas) {
			saveDownloading(fileTitle,sourceUrl, path, fileName, startPosition, totalSize, 0);
			Log.v(TAG, "hi here false");
			}
		else if (true == isHas) {
			startPosition = getDownloadedSize(sourceUrl, fileTitle);
			Log.v(TAG, "hi here true");
			updateDownloading(curSize, fileTitle, sourceUrl);
			}
		for(int i=0;i<DownloadConsts.listUrl.size();i++) {
			if (sourceUrl.equalsIgnoreCase(DownloadConsts.listUrl.get(i))) {
					id = i;
				}
			}
	}
	public void start(){
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		RandomAccessFile outputStream = null;
		try {
			String path = Environment.getExternalStorageDirectory().getPath();
			// 设置断点续传的开始位置
			url = new URL(sourceUrl);
			httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.setRequestProperty("User-Agent","NetFox");
			httpURLConnection.setRequestProperty("Range", "bytes=" + startPosition + "-");
			Log.v(TAG, "http finished");
			inputStream = httpURLConnection.getInputStream();
			
//			String fileName = strUrl.substring(strUrl.lastIndexOf('/'));
			
			File outFile = new File(path,fileName);
			
			// 使用java中的RandomAccessFile 对文件进行随机读写操作
			outputStream = new RandomAccessFile(outFile,"rw");
			//Log.v(TAG, "hhhhh");
			// 设置开始写文件的位置
			outputStream.seek(startPosition);
			
			byte[] buf = new byte[1024*100];
			int read = 0;
			curSize = startPosition;
			while(false == finished) {
				while(true == paused) {
					Thread.sleep(1000);	
				}
				while(true == canceled){
					finished = true;
					//delete from the list
				}
				read = inputStream.read(buf);
				if(read==-1) {
					break;
				}
				outputStream.write(buf,0,read);
				curSize = curSize+read;
				Log.v(TAG, "curSize = "+curSize);
				// 当调用这个方法的时候会自动去调用onProgressUpdate方法，传递下载进度
				//publishProgress((int)(curSize*100.0f/length),urlPosition);
				if(curSize == totalSize) {
					break;
				}
				Thread.sleep(1000);
				updateDownloading(curSize, fileTitle, sourceUrl);
				Log.v(TAG, "paused " + paused);
			}
			if (false == finished) {
				finished = true;
				Log.v(TAG, "finished now");
				//deleteDownloading(sourceUrl, fileTitle);
//				if (AppConstants.mapPbPercent.containsKey(url)) {
//					AppConstants.mapPbPercent.remove(url);
//				}
			}
			inputStream.close();
			outputStream.close();
			httpURLConnection.disconnect();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			finished = true;
			//deleteDownloading(sourceUrl, fileTitle);
			if(inputStream!=null) {
				try {
					inputStream.close();
					if(outputStream!=null) {
						outputStream.close();
					}
					if(httpURLConnection!=null) {
						httpURLConnection.disconnect();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.v(TAG, "startnownownownow" + finished);
	}
	public void pause(){
		paused = true;
	}
	public void resume(){
		paused = false;
	}
	public void cancel(){
		canceled = true;
	}
	public long getDownloadId(){
		return id;
	}
	public int getCursize(){
		return curSize;
	}
	public int getTotalSize(){
		return totalSize;
	}
	public boolean isPaused(){
		return paused;
	}
	public boolean isFinished(){
		return finished;
	}
	public boolean isCanceled(){
		return canceled;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public Context getContext() {
		return context;
	}
	public int getState(){
		//0 for running, 1 for paused, 2 for canceled, 3 for failed, 4 for successful
		int state = 0;//running
		
		return state;
	}
	/**
	 * 获取要下载内容的长度
	 */
	private int getDownloadTotalSize(String urlString){
		
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			return connection.getContentLength();
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 从数据库获取已经下载的长度
	 */
	private int getDownloadedSize(String url, String name) {
		int downloadedLength = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase(); 
        Log.v(TAG, "hi here");
        String sql = "SELECT downloadBytes FROM fileDownloading WHERE downloadUrl=? AND name=?";  
        Cursor cursor = db.rawQuery(sql, new String[] { url, name });  
        while (cursor.moveToNext()) {  
        	downloadedLength = cursor.getInt(0);   
        }  
        db.close();  
        return downloadedLength;  
    }
	/**
	 * 保存下载的数据
	 */
	private void saveDownloading(String name, String url, String savePath, String fileName, int downloadBytes, int totalBytes, int status) {  
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        try {  
            db.beginTransaction();  
            String sql = "INSERT INTO fileDownloading(name, downloadUrl, savePath, fileName, downloadBytes, totalBytes, downloadStatus) " +
            		"values(?,?,?,?,?,?,?)";  
            db.execSQL(sql, new Object[]{ name,url, savePath, fileName, downloadBytes, totalBytes, status});  
            db.setTransactionSuccessful();
            boolean isHas = false;
			// 判断当前要下载的这个连接是否已经正在进行，如果正在进行就阻止在此启动一个下载任务
			for (String urlString : DownloadConsts.listUrl) {
				if (url.equalsIgnoreCase(urlString)) {
					isHas = true;
					break;
				}
			}
			if (false == isHas) {
				DownloadConsts.listUrl.add(url);
			}
        } 
        finally {  
            db.endTransaction();  
            db.close();  
        }  
    }
	/**
	 * 更新下载数据
	 */
	private void updateDownloading(int cursize, String name, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();  
        try {  
            db.beginTransaction();  
            String sql = "UPDATE fileDownloading SET downloadBytes=? WHERE name=? AND downloadUrl=?";  
            db.execSQL(sql, new String[] { cursize + "", name, url });  
            db.setTransactionSuccessful();  
        } finally {  
            db.endTransaction();  
            
            db.close();  
        }  
    }
	/**
	 * 删除下载数据
	 
	private void deleteDownloading(String url, String name) {
		if (true == finished) {
			for (int i = 0; i < DownloadConsts.listUrl.size(); i++) {
				if (url.equalsIgnoreCase(DownloadConsts.listUrl.get(i))) {
					DownloadConsts.listUrl.remove(i);
				}
			}
			if (DownloadConsts.mapTask.containsKey(url)) {
				DownloadConsts.mapTask.remove(url);
			}
			if (DownloadConsts.mapPbPercent.containsKey(url)) {
				DownloadConsts.mapPbPercent.remove(url);
			}
		}
        SQLiteDatabase db = dbHelper.getWritableDatabase();  
        String sql = "DELETE FROM fileDownloading WHERE downloadUrl=? AND name=?";  
        db.execSQL(sql, new Object[] { url, name });  
        db.close();  
    } */
}
