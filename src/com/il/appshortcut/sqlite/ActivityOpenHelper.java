package com.il.appshortcut.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActivityOpenHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_NAME = "activities";
	
	private static final String FIELD_ID = "id_activity";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_DESCRIPTION = "description";
	private static final String FIELD_PATTERN = "pattern";
	private static final String FIELD_ASSIGNED = "assigned";
	
	private static final String ACTIVITY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ FIELD_NAME + " TEXT, "
			+ FIELD_DESCRIPTION + " TEXT, "
			+ FIELD_PATTERN + " TEXT, "
			+ FIELD_ASSIGNED + " int);";
			
	public ActivityOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, TABLE_NAME, factory, DATABASE_VERSION);
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
