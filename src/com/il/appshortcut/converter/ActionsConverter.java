package com.il.appshortcut.converter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;

import com.il.appshortcut.sqlite.ActionsOpenHelper;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;
import com.il.appshortcut.views.SelectPatternInfoVo;

public class ActionsConverter {
	
	public static ContentValues convertActions2ContentValues(ActionVo action){
		ContentValues values = new ContentValues();
		if (action.getIdAction() > 0){
			values.put(ActionsOpenHelper.FIELD_ID, action.getIdAction());
		}
		values.put(ActionsOpenHelper.FIELD_ACTION_PACKAGE, action.getActionPackage());
	    values.put(ActionsOpenHelper.FIELD_ACTION_NAME, action.getActionName());
	    values.put(ActionsOpenHelper.FIELD_PARENT_PACKAGE, action.getParentPackage());
	    values.put(ActionsOpenHelper.FIELD_PATTERN, action.getPattern());
	    int assigned = (action.isAssigned())?1:0;
	    values.put(ActionsOpenHelper.FIELD_ASSIGNED, assigned);
	    values.put(ActionsOpenHelper.FIELD_TYPE, action.getType());
	    values.put(ActionsOpenHelper.FIELD_ACTION_DESCRIPTION, action.getActionDescription());
	    values.put(ActionsOpenHelper.FIELD_ACTION_CLASS_NAME, action.getClassName());
	    return values;
	}
	
	public static ActionVo convertCursor2Action(Cursor cursor){
		ActionVo item = null;
		
		if (cursor.moveToFirst()) {
			item = new ActionVo();
			do {
				item.setIdAction(cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ID)));
				item.setActionName(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_NAME)));
				item.setActionPackage(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_PACKAGE)));
				int assigned = cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ASSIGNED));
				item.setAssigned((assigned == 1));
				item.setParentPackage(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_PARENT_PACKAGE)));
				item.setPattern(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_PATTERN)));
				item.setType(cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_TYPE)));
				item.setActionDescription(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_DESCRIPTION)));
				item.setClassName(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_CLASS_NAME)));
			} while (cursor.moveToNext());
		}
		return item;
	}
	
	public static List<ActionVo> convertCursor2ListAction(Cursor cursor){
		List<ActionVo> result = null;
		if (cursor.moveToFirst()) {
			result = new ArrayList<ActionVo>();
			do {
				ActionVo item = new ActionVo();
				item.setIdAction(cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ID)));
				item.setActionName(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_NAME)));
				item.setActionPackage(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_PACKAGE)));
				int assigned = cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ASSIGNED));
				item.setAssigned((assigned == 1));
				item.setParentPackage(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_PARENT_PACKAGE)));
				item.setPattern(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_PATTERN)));
				item.setType(cursor.getInt(cursor.getColumnIndex(ActionsOpenHelper.FIELD_TYPE)));
				item.setActionDescription(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_DESCRIPTION)));
				item.setClassName(cursor.getString(cursor.getColumnIndex(ActionsOpenHelper.FIELD_ACTION_CLASS_NAME)));
				result.add(item);
			} while (cursor.moveToNext());
		}
		
		return result;
	}
	
	
	public static SelectPatternInfoVo convertApplication2SelectPatternInfoView(ApplicationVo app, Context ctx){
		SelectPatternInfoVo selectPatternInfoVo = new SelectPatternInfoVo();
		ApplicationInfo appInfo = app.getApplicationInfo();
		selectPatternInfoVo.setName(app.getCurrentAction().getActionName());
		selectPatternInfoVo.setDescription(app.getCurrentAction().getActionDescription());
		selectPatternInfoVo.setIcon(appInfo.loadIcon(ctx.getPackageManager()));
		selectPatternInfoVo.setType(SelectPatternInfoVo.TYPE_APPLICATAION);
		selectPatternInfoVo.setPattern(app.getCurrentAction().getPattern());
		return selectPatternInfoVo;
	}
}
