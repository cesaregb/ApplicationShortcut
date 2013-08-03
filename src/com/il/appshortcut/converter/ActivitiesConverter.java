package com.il.appshortcut.converter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.il.appshortcut.sqlite.ActivityOpenHelper;
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
}
