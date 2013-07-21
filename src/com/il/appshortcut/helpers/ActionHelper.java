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
import com.il.appshortcut.views.ApplicationActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ActionHelper {
//	final PackageManager pm = getApplicationContext().getPackageManager();
//	ApplicationInfo ai;
//	try {
//	    ai = pm.getApplicationInfo( this.getPackageName(), 0);
//	} catch (final NameNotFoundException e) {
//	    ai = null;
//	}
//	final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
	
	public static final String SEPARATOR = "-";
	public static final String SEPARATOR_2 = ":";
	
	public static Intent getPatternIntent(String pattern,
			SharedPreferences sharedPref, PackageManager pm)
			throws Exception {
		Intent resultIntent = null;
		
		String id = AppManager.ID_PRE_FFILE + SEPARATOR + pattern;
		String tmp = sharedPref.getString(id, null);
		if (tmp != null) {
			
			String[] split = tmp.split(SEPARATOR);
			
			String appPackageParts = split[0]; 
			String actionPackage = split[1];
			
			String[] appInfoParts = appPackageParts.split(SEPARATOR_2);
			String appPackage = appInfoParts[0];
			String appClassName = appInfoParts[1];
			
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
		String id = AppManager.ID_PRE_FFILE + SEPARATOR + pattern;
		String tmp = sharedPref.getString(id, null);
		return (tmp != null);
	}
	
	public static String getApplicationNameByPattern(String pattern,
			SharedPreferences sharedPref)
					throws Exception {
		String result = null;
		String id = AppManager.ID_PRE_FFILE + SEPARATOR + pattern;
		String tmp = sharedPref.getString(id, null);
		if (tmp != null){
			String[] appInfoArray = tmp.split(SEPARATOR);
			if (appInfoArray.length == 3){
				result = appInfoArray[2];
			}else{
				result = appInfoArray[0];
			}
		}
		return result;
	}
	
	public static List<ApplicationActionVo> getApplicationSelectedActions(List<ApplicationActionVo> list, ApplicationVo appSelected, SharedPreferences sharedPref, Resources r) throws Exception{
		List<ApplicationActionVo> result = new ArrayList<ApplicationActionVo>();
		int i = 0;
		for (ApplicationActionVo item : list){
			String appId = AppManager.ID_PRE_FFILE + SEPARATOR + appSelected.getName() + SEPARATOR + item.getActionPackage();
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
	
	public static String isAssignedByAction(ApplicationVo app, ApplicationActionVo action, SharedPreferences sharedPref){
		String actionId = getActionId( getApplicationInfo(app.getComponentName()), action.getActionPackage() );
		String pattern = sharedPref.getString(actionId, null);
		return pattern;
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



