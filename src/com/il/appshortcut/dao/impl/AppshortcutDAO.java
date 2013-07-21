package com.il.appshortcut.dao.impl;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.IAppshortcutDAO;
import com.il.appshortcut.exception.AppShortcutException;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.ApplicationActionVo;
import com.il.appshortcut.widgets.WidgetUtils;

public class AppshortcutDAO implements IAppshortcutDAO {

	@Override
	public String updateWidgetSelection(Context context, String currentSelection)
			throws AppShortcutException {
		
		String updatedSelection = getWidgetSelection(context) + currentSelection;
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, updatedSelection); // search by application  + action 
		editor.commit();
		return updatedSelection;
	}

	@Override
	public void clearWidgetSelection(Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, "");  
		editor.commit();
	}

	@Override
	public String getWidgetSelection(Context context)
			throws AppShortcutException {
		
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		String result = sharedPref.getString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, null);
		if (result == null){ result = ""; }
		return result; 
	}

	@Override
	public void savePattern(String currentSelection, Context context)
			throws AppShortcutException {
		
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(currentSelection, 1); 
		editor.commit();
	}

	@Override
	public void saveAction(ActionVo application) throws AppShortcutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveActivity(ActivityVo activity) throws AppShortcutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveActivityDetail(ActivityDetailVo detail)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePattern(String pattern) throws AppShortcutException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ActivityVo getActivityByPattern(String pattern)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityDetailVo getActivityDetailsByActivity(String activityId)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionVo getActionByPattern(String pattern)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ApplicationActionVo> getActionsByApplication(
			ActionVo application) throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllPatternAssigned() throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActivityVo> getAllActivitiesAssigned()
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionVo> getAllActionsAssigned() throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ActionVo> getApplicationActionsByApplication(String app_package)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPatternAssigned(String pattern)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActivityActive(String activityId)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isActionActive(String actionId) throws AppShortcutException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPackageActive(String app_package)
			throws AppShortcutException {
		// TODO Auto-generated method stub
		return false;
	}

	

}
