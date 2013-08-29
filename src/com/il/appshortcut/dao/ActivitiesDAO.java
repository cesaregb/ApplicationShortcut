package com.il.appshortcut.dao;

import static com.il.appshortcut.converter.ActivitiesConverter.*;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.il.appshortcut.sqlite.ActivityOpenHelper;
import com.il.appshortcut.views.ActivityVo;

public class ActivitiesDAO {
	private SQLiteDatabase database;
	private ActivityOpenHelper dbHelper;
	
	public static final int TYPE_ACTION = 1;
	public static final int TYPE_SERVICE = 2;
	
	public String[] allColumns = { 
			ActivityOpenHelper.FIELD_ID,
			ActivityOpenHelper.FIELD_NAME,
			ActivityOpenHelper.FIELD_DESCRIPTION,
			ActivityOpenHelper.FIELD_PATTERN,
			ActivityOpenHelper.FIELD_ASSIGNED
			};

	public ActivitiesDAO(Context context) {
		dbHelper = new ActivityOpenHelper(context);
	}
	
	public void clearDatabase(){
		this.open();
		database.delete(ActivityOpenHelper.TABLE_NAME, null, null);
		this.close();
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Long addUpdateActivity(ActivityVo activity) {
		long insertId = 0;
		this.open();
		ContentValues values = convertActivity2ContentValues(activity);
		if (activity.getIdActivity() > 0) {
			database.update(ActivityOpenHelper.TABLE_NAME, values,
					ActivityOpenHelper.FIELD_ID + "=?",
					new String[] { String.valueOf(activity.getIdActivity()) });
			insertId = activity.getIdActivity();
		}else{
			insertId = database.insert(ActivityOpenHelper.TABLE_NAME, null, values);
		}
		
		database.close();
		this.close();
		return insertId;
	}
	
	public List<ActivityVo> getAllActivities() {
		List<ActivityVo> list = null;
		this.open();
		Cursor cursor = database.query(ActivityOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null, null);
		if (cursor != null) { list = convertCursor2ListActivity(cursor); }
		this.close();
		return list;
	}
	
	
}
