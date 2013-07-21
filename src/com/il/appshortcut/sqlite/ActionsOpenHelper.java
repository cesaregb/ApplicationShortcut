package com.il.appshortcut.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class ActionsOpenHelper extends SQLiteOpenHelper {

private static final int DATABASE_VERSION = 1;
	
	private static final String ACTIONS_TABLE_NAME = "activity_options";
	
	private static final String FIELD_ID = "id_action";
	private static final String FIELD_ACTION_PACKAGE = "action_package";
	private static final String FIELD_ACTION_NAME = "action_name";
	private static final String FIELD_APPLICATION_PACKAGE = "application_package";
	private static final String FIELD_PATTERN = "pattern";
	private static final String FIELD_ASSIGNED = "assigned";
	private static final String FIELD_TYPE = "type";
	
	private static final String ACTIONS_TABLE_CREATE = "CREATE TABLE "
			+ ACTIONS_TABLE_NAME + " ( " + FIELD_ID + " integer primay key autoincrement," 
			+ FIELD_ACTION_PACKAGE + " text, "
			+ FIELD_ACTION_NAME + " text, "
			+ FIELD_APPLICATION_PACKAGE + " text, "
			+ FIELD_PATTERN + " text, "
			+ FIELD_ASSIGNED + " int, "
			+ FIELD_TYPE + " int);";
			
	public ActionsOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, ACTIONS_TABLE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ACTIONS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ActivityOpenHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + ACTIONS_TABLE_NAME);
	    onCreate(db);
	}

}
