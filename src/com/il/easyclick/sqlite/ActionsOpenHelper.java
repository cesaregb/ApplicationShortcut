package com.il.easyclick.sqlite;

import com.il.easyclick.config.AppManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ActionsOpenHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 4;
	
	public static final String TABLE_NAME = "actions";
	
	public static final String FIELD_ID = "id_action";
	public static final String FIELD_ACTION_PACKAGE = "action_package";
	public static final String FIELD_ACTION_NAME = "action_name";
	public static final String FIELD_PARENT_PACKAGE = "application_package";
	public static final String FIELD_PATTERN = "pattern";
	public static final String FIELD_ASSIGNED = "assigned";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_ACTION_DESCRIPTION = "action_description";
	public static final String FIELD_ACTION_CLASS_NAME = "app_class_name";
	
	private static final String ACTIONS_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + " ( " + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," 
			+ FIELD_ACTION_PACKAGE + " text, "
			+ FIELD_ACTION_NAME + " text, "
			+ FIELD_PARENT_PACKAGE + " text, "
			+ FIELD_PATTERN + " text, "
			+ FIELD_ASSIGNED + " int, "
			+ FIELD_TYPE + " int, "
			+ FIELD_ACTION_DESCRIPTION + " text, "
			+ FIELD_ACTION_CLASS_NAME + " text);";
			
	public ActionsOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, TABLE_NAME, factory, DATABASE_VERSION);
	}

	public ActionsOpenHelper(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ACTIONS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(AppManager.LOG_SQL,
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(db);
	}

}
