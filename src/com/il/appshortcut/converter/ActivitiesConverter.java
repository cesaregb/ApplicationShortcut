package com.il.appshortcut.converter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.il.appshortcut.sqlite.ActivityDetailsOpenHelper;
import com.il.appshortcut.sqlite.ActivityOpenHelper;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityVo;

public class ActivitiesConverter {
	
	public static ContentValues convertActivity2ContentValues(ActivityVo activity){
		ContentValues values = new ContentValues();
		if (activity.getIdActivity() > 0){
			values.put(ActivityOpenHelper.FIELD_ID, activity.getIdActivity());
		}
		values.put(ActivityOpenHelper.FIELD_NAME, activity.getName());
	    values.put(ActivityOpenHelper.FIELD_DESCRIPTION, activity.getDescription());
	    values.put(ActivityOpenHelper.FIELD_PATTERN, activity.getPattern());
	    int assigned = (activity.isAssigned())?1:0;
	    values.put(ActivityOpenHelper.FIELD_ASSIGNED, assigned);
	    return values;
	}
	
	
	public static List<ActivityVo> convertCursor2ListActivity(Cursor cursor){
		List<ActivityVo> result = null;
		if (cursor.moveToFirst()) {
			result = new ArrayList<ActivityVo>();
			do {
				ActivityVo item = new ActivityVo();
				item.setIdActivity(cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_NAME)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_DESCRIPTION)));
				int assigned = cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ASSIGNED));
				item.setAssigned((assigned == 1));
				item.setPattern(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_PATTERN)));
				result.add(item);
			} while (cursor.moveToNext());
		}
		return result;
	}
	
	/*Details*/
	public static ContentValues convertActivityDetail2ContentValues(ActivityDetailVo activityDetail){
		ContentValues values = new ContentValues();
		if (activityDetail.getIdActivityDetail() > 0){
			values.put(ActivityDetailsOpenHelper.FIELD_ID, activityDetail.getIdActivityDetail());
		}
		
		values.put(ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY, activityDetail.getIdActivity());
	    values.put(ActivityDetailsOpenHelper.FIELD_TYPE, activityDetail.getType());
	    values.put(ActivityDetailsOpenHelper.FIELD_ORDER, activityDetail.getOrder());
	    values.put(ActivityDetailsOpenHelper.FIELD_TOP, activityDetail.getTop());
	    values.put(ActivityDetailsOpenHelper.FIELD_ID_ACTION, activityDetail.getIdAction());
	    return values;
	}
	
	
	public static List<ActivityDetailVo> convertCursor2ListActivityDetails(Cursor cursor){
		List<ActivityDetailVo> result = null;
		if (cursor.moveToFirst()) {
			result = new ArrayList<ActivityDetailVo>();
			do {
				ActivityDetailVo item = new ActivityDetailVo();
				item.setIdActivityDetail(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID)));
				item.setIdActivity(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY)));
				item.setType(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TYPE)));
				item.setOrder(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ORDER)));
				item.setTop(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TOP)));
				item.setIdAction(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID_ACTION)));
				result.add(item);
			} while (cursor.moveToNext());
		}
		return result;
	}
}
