package com.il.appshortcut.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActivityOpenHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 2;
	
	public static final String TABLE_NAME = "activities";
	
	public static final String FIELD_ID = "id_activity";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_PATTERN = "pattern";
	public static final String FIELD_ASSIGNED = "assigned";
	public static final String FIELD_ID_ICON = "id_icon";
	
	private static final String ACTIVITY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ FIELD_NAME + " TEXT, "
			+ FIELD_DESCRIPTION + " TEXT, "
			+ FIELD_PATTERN + " TEXT, "
			+ FIELD_ASSIGNED + " int, "
			+ FIELD_ID_ICON + " int);";
			
	public ActivityOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, TABLE_NAME, factory, DATABASE_VERSION);
	}

	public ActivityOpenHelper(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ACTIVITY_TABLE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ActivityOpenHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}

}
