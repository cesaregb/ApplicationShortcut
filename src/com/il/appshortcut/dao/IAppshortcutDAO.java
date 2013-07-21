package com.il.appshortcut.dao;

import java.util.List;

import android.content.Context;

import com.il.appshortcut.exception.AppShortcutException;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.ApplicationActionVo;

public interface IAppshortcutDAO {
	/*SETTERS*/
	/**
	 * Save the selection with the widget interaction 
	 * @param currentSelection
	 * @throws AppShortcutException
	 */
	public String updateWidgetSelection(Context context, String currentSelection) throws AppShortcutException;
	
	public void clearWidgetSelection(Context context) throws AppShortcutException;
	
	public String getWidgetSelection(Context context) throws AppShortcutException;
	/**
	 * save the pattern in the shared preferences 
	 * @param currentSelection
	 * @throws AppShortcutException
	 */
	public void savePattern(String currentSelection, Context context) throws AppShortcutException; 
	
	public void saveAction(ActionVo application) throws AppShortcutException;
	
	public void saveActivity(ActivityVo activity) throws AppShortcutException;
	
	public void saveActivityDetail(ActivityDetailVo detail) throws AppShortcutException;
	
	/**
	 * remove the pattern from shared preferences
	 * @param pattern
	 * @throws AppShortcutException
	 */
	public void removePattern(String pattern) throws AppShortcutException;
	
	/*GETTERS*/
	public ActivityVo getActivityByPattern(String pattern) throws AppShortcutException; 
	
	public ActivityDetailVo getActivityDetailsByActivity(String activityId) throws AppShortcutException;
	
	public ActionVo getActionByPattern(String pattern) throws AppShortcutException;
	
	public List<ApplicationActionVo> getActionsByApplication (ActionVo application) throws AppShortcutException;
	
	public List<String> getAllPatternAssigned() throws AppShortcutException;
	
	public List<ActivityVo> getAllActivitiesAssigned() throws AppShortcutException;
	
	public List<ActionVo> getAllActionsAssigned() throws AppShortcutException;
	
	public List<ActionVo> getApplicationActionsByApplication(String app_package) throws AppShortcutException;
	
	/*BOOLEAN*/
	
	public boolean isPatternAssigned(String pattern) throws AppShortcutException;
	
	public boolean isActivityActive(String activityId) throws AppShortcutException;
	
	public boolean isActionActive(String actionId) throws AppShortcutException; 
	
	/**
	 * check if an application package is active
	 * @param app_package
	 * @return
	 * @throws AppShortcutException
	 */
	public boolean isPackageActive(String app_package) throws AppShortcutException;
	
	
	
}
