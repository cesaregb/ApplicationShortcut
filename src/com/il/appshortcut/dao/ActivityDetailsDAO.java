package com.il.appshortcut.dao;

import static com.il.appshortcut.converter.ActivitiesConverter.convertActivityDetail2ContentValues;
import static com.il.appshortcut.converter.ActivitiesConverter.convertCursor2ListActivityDetails;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.il.appshortcut.sqlite.ActivityDetailsOpenHelper;
import com.il.appshortcut.views.ActivityDetailVo;

public class ActivityDetailsDAO {
	private SQLiteDatabase database;
	
	public static final int DETAIL_TYPE_ACTION = 1;
	public static final int DETAIL_TYPE_ACTIVITY = 2;

	private ActivityDetailsOpenHelper dbHelper;

	public String[] allColumns = { 
			ActivityDetailsOpenHelper.FIELD_ID,
			ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY,
			ActivityDetailsOpenHelper.FIELD_TYPE,
			ActivityDetailsOpenHelper.FIELD_ORDER,
			ActivityDetailsOpenHelper.FIELD_TOP, 
			ActivityDetailsOpenHelper.FIELD_ID_ACTION
	};

	public ActivityDetailsDAO(Context context) {
		dbHelper = new ActivityDetailsOpenHelper(context);
	}
	
	public void clearDatabase(){
		this.open();
		database.delete(ActivityDetailsOpenHelper.TABLE_NAME, null, null);
		this.close();
	}
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Long addUpdateActivityDetail(ActivityDetailVo activityDetail) {
		long insertId = 0;
		this.open();
		ContentValues values = convertActivityDetail2ContentValues(activityDetail);
		if (activityDetail.getIdActivityDetail() > 0){
			database.update(ActivityDetailsOpenHelper.TABLE_NAME, values,
					ActivityDetailsOpenHelper.FIELD_ID + "=?",
					new String[] { String.valueOf(activityDetail
							.getIdActivityDetail()) });
			
			insertId = activityDetail.getIdActivityDetail();
		}else{
			insertId = database.insert(ActivityDetailsOpenHelper.TABLE_NAME, null, values);
		}
		
		database.close();
		this.close();
		return insertId;
	}
	
	public ActivityDetailVo removeActivityDetailById(String id) {
		this.open();
		ActivityDetailVo result = null;
		if (id != null) {
			database.delete(ActivityDetailsOpenHelper.TABLE_NAME,
					ActivityDetailsOpenHelper.FIELD_ID + "=?", new String[] { id });
		}
		this.close();
		return result;
	}
	
	public ActivityDetailVo removeActivityDetailsByActivity(int activityId) {
		this.open();
		ActivityDetailVo result = null;
		if (activityId > 0) {
			database.delete(ActivityDetailsOpenHelper.TABLE_NAME,
					ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY + "=?",
					new String[] { String.valueOf(activityId) });
		}
		this.close();
		return result;
	}
	
	public List<ActivityDetailVo> getAllActivityDetail() {
		List<ActivityDetailVo> list = null;
		this.open();
		Cursor cursor = database.query(ActivityDetailsOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null, null);
		if (cursor != null) { list = convertCursor2ListActivityDetails(cursor); }
		this.close();
		return list;
	}

	public List<ActivityDetailVo> getAllActivityDetailsByActivity(
			String activityId) {
		List<ActivityDetailVo> list = null;
		this.open();
		Cursor cursor = database.query(ActivityDetailsOpenHelper.TABLE_NAME,
				allColumns, ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY + "=?",
				new String[] { activityId }, null, null, null, null);
		if (cursor != null) {
			list = convertCursor2ListActivityDetails(cursor);
		}
		this.close();
		return list;
	}
}
