package com.il.appshortcut.dao.impl;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.il.appshortcut.android.widgets.WidgetUtils;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.exception.AppShortcutException;

public class AppshortcutDAO {
	
	public static int PREF_TYPE_ACTION = 1;
	public static int PREF_TYPE_ACTIVITY = 2;

	public String updateWidgetSelection(Context context, String currentSelection)
			throws AppShortcutException {
		
		String updatedSelection = getWidgetSelection(context) + currentSelection;
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, updatedSelection); // search by application  + action 
		editor.commit();
		return updatedSelection;
	}

	public void clearWidgetSelection(Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, "");  
		editor.commit();
	}

	public String getWidgetSelection(Context context)
			throws AppShortcutException {
		
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		String result = sharedPref.getString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, null);
		if (result == null){ result = ""; }
		return result; 
	}

	public void savePattern(String pattern, Context context, int type)
			throws AppShortcutException {
		
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(pattern, type); 
		editor.commit();
	}

	public void removePattern(String pattern) throws AppShortcutException {
		// TODO Auto-generated method stub
		
	}

	public List<String> getAllPatternAssigned() throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isPatternAssigned(String pattern, Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		
		int tmpVal = sharedPref.getInt(pattern, 0);
		return (tmpVal > 0);
	}
	
	public int getTypePatternAssigned(String pattern, Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		
		int tmpVal = sharedPref.getInt(pattern, 0);
		return tmpVal;
	}

	
	public void refreshDataDb(Context context){
		ActionsDAO actionsDao = new ActionsDAO(context);
		AppShortcutApplication appState = ((AppShortcutApplication) context);
		appState.setCurrentDBActions(actionsDao.getAllActions());
	}

}
