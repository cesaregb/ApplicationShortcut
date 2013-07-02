package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.il.appshortcut.actions.CommonActions;
import com.il.appshortcut.actions.FacebookActions;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ActionHelper {
	public static final String SEPARATOR = "-";
	public static final String SEPARATOR_2 = ":";
	
	
	public static Intent getPatternIntent(String pattern,
			SharedPreferences sharedPref, PackageManager pm)
			throws Exception {
		Intent resultIntent = null;
		
		String id = AppManager.ID_PRE_FFILE + "-" + pattern;
		String tmp = sharedPref.getString(id, null);
		if (tmp != null) {
			
			String[] split = tmp.split(SEPARATOR);
			
			String appPackageParts = split[0]; 
			String[] appInfoParts = appPackageParts.split(SEPARATOR_2);
			String appPackage = appInfoParts[0];
			String appClassName = appInfoParts[1];
			String actionPackage = split[1];
			
			
			CommonActions actions = null;
			if (appPackage.equalsIgnoreCase(FacebookActions.FACEBOOK_PACKAGE)) {
				if (actionPackage
						.equalsIgnoreCase(FacebookActions.ACTION_NEW_MESSAGE)) {
					actions = new FacebookActions();
					resultIntent = ((FacebookActions) actions).getNewMessageIntent();
				} else {
					actions = new CommonActions(actionPackage, pm);
					resultIntent = actions.getOpenApplicationIntent();
				}
			}else{
				actions = new CommonActions(actionPackage, pm);
				resultIntent = actions.getOpenApplicationIntent(appClassName);
			}
		}
		return resultIntent;
		
	}
	
	public static boolean isPatternAssigned(String pattern,
			SharedPreferences sharedPref)
					throws Exception {
		String id = AppManager.ID_PRE_FFILE + "-" + pattern;
		String tmp = sharedPref.getString(id, null);
		return (tmp != null);
	}
	
	public static List<ActionVo> getApplicationSelectedActions(List<ActionVo> list, ApplicationVo appSelected, SharedPreferences sharedPref, Resources r) throws Exception{
		List<ActionVo> result = new ArrayList<ActionVo>();
		int i = 0;
		for (ActionVo item : list){
			String appId = AppManager.ID_PRE_FFILE + "-" + appSelected.getName() + "-" + item.getActionPackage();
			String tmp = sharedPref.getString(appId, null);
			if (tmp != null){
				result.add(item);
				list.remove(i);
			}
			i++;
		}
		return result;
	}

	public static boolean isAssignedByApplication(ApplicationVo app, SharedPreferences sharedPref){
		String appId = getAppId(getApplicationInfo(app.getComponentName()) );
		String pattern = sharedPref.getString(appId, null);
		return (pattern != null);
	}
	
	public static boolean isAssignedByAction(ApplicationVo app, ActionVo action, SharedPreferences sharedPref){
		String actionId = getActionId( getApplicationInfo(app.getComponentName()), action.getActionPackage() );
		String pattern = sharedPref.getString(actionId, null);
		return (pattern != null);
	}
	
	
	/**
	 * @param appInfo
	 * @param actionInfo
	 * @return String containing appPakcaget + ":" + appClassName 
	 */
	public static String getApplicationInfo(ComponentName coponentName){
		return coponentName.getPackageName() + SEPARATOR_2 + coponentName.getClassName();
	}
	
	public static String getActionId(String appInfo, String actionInfo){
		return AppManager.ID_PRE_FFILE + SEPARATOR + appInfo + SEPARATOR + actionInfo;
	}
	public static String getAppId(String appInfo){
		return AppManager.ID_PRE_FFILE + SEPARATOR + appInfo;
	}
	public static String getPatternId(String pattern){
		return AppManager.ID_PRE_FFILE + SEPARATOR + pattern;
	}
	
	
}



