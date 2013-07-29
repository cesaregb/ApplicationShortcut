package com.il.appshortcut.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActivityDetailsOpenHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_NAME = "activity_options";
	
	private static final String FIELD_ID = "id_activity_option";
	private static final String FIELD_TYPE = "type";
	private static final String FIELD_ORDER = "order";
	private static final String FIELD_TOP = "top";
	private static final String FIELD_ACTION_TYPE = "action_type";
	private static final String FIELD_ID_ACTIVITY = "id_activity";
	private static final String FIELD_ID_ACTION = "id_action";
	
	private static final String ACTIVITY_OPTIONS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ FIELD_TYPE + " int, "
			+ FIELD_ORDER + " int, "
			+ FIELD_TOP + " int, "
			+ FIELD_ACTION_TYPE + " int, "
			+ FIELD_ID_ACTIVITY + " int, "
			+ FIELD_ID_ACTION + " int);";
			
	public ActivityDetailsOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, TABLE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ACTIVITY_OPTIONS_TABLE_CREATE);
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
