package com.downloadmanager.bean;

import android.content.Context;  
import android.database.sqlite.SQLiteDatabase;  
import android.database.sqlite.SQLiteOpenHelper;  
import android.util.Log;
public class DBHelper extends SQLiteOpenHelper {  
  
	private static final String TAG ="dbHelper";
    public DBHelper(Context context) {  
        super(context, "download.db", null, 1); 
       // Log.v(TAG,"hhhhh");
    }  
  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
    	Log.v(TAG,"create table");
    	db.execSQL("CREATE TABLE fileDownloading(_id integer primary key autoincrement,name varchar(500), " +
        		"downloadUrl varchar(100), savePath varchar(100), fileName varchar(100), " +
        		"downloadBytes INTEGER, totalBytes INTEGER, downloadStatus INTEGER)");  
    	//Log.v(TAG,"create table");
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
  
    }  
  
}
