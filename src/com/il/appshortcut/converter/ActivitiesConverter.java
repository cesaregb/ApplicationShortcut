package com.il.appshortcut.converter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.helpers.ActivityIconHelper;
import com.il.appshortcut.helpers.ServicesHelper;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.sqlite.ActivityDetailsOpenHelper;
import com.il.appshortcut.sqlite.ActivityOpenHelper;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.AllAppsList;
import com.il.appshortcut.views.ApplicationVo;
import com.il.appshortcut.views.SelectPatternInfoVo;

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
	    values.put(ActivityOpenHelper.FIELD_ID_ICON, activity.getIdIcon());
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
				item.setIdIcon(cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ID_ICON)));
				result.add(item);
			} while (cursor.moveToNext());
		}
		return result;
	}
	
	public static ActivityVo convertCursor2Activity(Cursor cursor){
		ActivityVo item = null;
		if (cursor.moveToFirst()) {
			item = new ActivityVo();
			do {
				item.setIdActivity(cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ID)));
				item.setName(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_NAME)));
				item.setDescription(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_DESCRIPTION)));
				int assigned = cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ASSIGNED));
				item.setAssigned((assigned == 1));
				item.setPattern(cursor.getString(cursor.getColumnIndex(ActivityOpenHelper.FIELD_PATTERN)));
				item.setIdIcon(cursor.getInt(cursor.getColumnIndex(ActivityOpenHelper.FIELD_ID_ICON)));
			} while (cursor.moveToNext());
		}
		return item;
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
				int typeApp = cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TYPE));
				item.setType(typeApp);
				item.setOrder(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ORDER)));
				item.setTop(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TOP)));
				item.setIdAction(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID_ACTION)));
				result.add(item);
				if (typeApp == ActivitiesDAO.TYPE_ACTION){
					
				}else{
					
				}
			} while (cursor.moveToNext());
		}
		return result;
	}
	public static List<ActivityDetailVo> convertCursor2ListActivityDetails(Cursor cursor, AllAppsList allAppsList, Context context){
		List<ActivityDetailVo> result = null;
		if (cursor.moveToFirst()) {
			result = new ArrayList<ActivityDetailVo>();
			ServicesHelper servicesHelper = new ServicesHelper(context);
			ActionsDAO actionsDao = new ActionsDAO(context);
			do {
				ActivityDetailVo item = new ActivityDetailVo();
				item.setIdActivityDetail(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID)));
				item.setIdActivity(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID_ACTIVITY)));
				int typeApp = cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TYPE));
				item.setType(typeApp);
				item.setOrder(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ORDER)));
				item.setTop(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_TOP)));
				item.setIdAction(cursor.getInt(cursor.getColumnIndex(ActivityDetailsOpenHelper.FIELD_ID_ACTION)));
				result.add(item);
				if (typeApp == ActivitiesDAO.TYPE_ACTION){
					ActionVo action = actionsDao.getActionById(item
							.getIdAction());
					for (ApplicationVo application : allAppsList.data) {
						boolean addItem = false;
						if (application.getComponentName().getClassName() != null) {
							
							addItem = application.getComponentName().getClassName()
									.equalsIgnoreCase(action.getClassName());
						} else {
							addItem = application
									.getApplicationPackage()
									.equalsIgnoreCase(action.getActionPackage());
						}
						if (addItem) {
							application.setCurrentAction(action);
							//add application to item 
							item.setIcon(application.getIcon());
							item.setApplication(application);
							break;
						}
					}
				}else{
					ServiceVo service = servicesHelper.getServiceById(item
							.getIdAction());
					item.setService(service);
				}
			} while (cursor.moveToNext());
		}
		return result;
	}
	
	public static SelectPatternInfoVo convertActivity2SelectPatternInfoView(ActivityVo activity, Resources r){
		SelectPatternInfoVo selectPatternInfoVo = new SelectPatternInfoVo();
		selectPatternInfoVo.setType(SelectPatternInfoVo.TYPE_ACTIVITY);
		selectPatternInfoVo.setName(activity.getName());
		selectPatternInfoVo.setDescription(activity.getDescription());
		
		if (activity.getIdIcon() > 0){
			Drawable d = r.getDrawable(ActivityIconHelper.getIconById( activity.getIdIcon()).getIdResource());
			selectPatternInfoVo.setIcon(d);
		}
		
		selectPatternInfoVo.setPattern(activity.getPattern());
		return selectPatternInfoVo;
	}
}
