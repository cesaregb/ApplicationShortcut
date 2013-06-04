package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.il.appshortcut.R;
import com.il.appshortcut.actions.CommonActions;
import com.il.appshortcut.actions.FacebookActions;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ActionHelper {
	
	public static final String FACEBOOK_APP_NAME = "facebook";
	public static final String SEPARATOR = "-";
	
	public static String idPrefFile; 
	
	public static void assignIdPrefFile(Resources r){
		idPrefFile = r.getString(R.string.idPrefFile);
	}
	
	public static Intent getIntent(String pattern,
			SharedPreferences sharedPref, PackageManager pm)
			throws Exception {
		Intent resultIntent = null;
		
		String id = idPrefFile + "-" + pattern;
		String tmp = sharedPref.getString(id, null);
		if (tmp != null) {
			String[] split = tmp.split("-");
			
			String appName = split[0]; 
			String appPackage = split[1];
			String action = "";
			if (split.length > 2){
				action = split[2]; 
			}
			
			CommonActions actions = null;
			if (appName.equalsIgnoreCase(FACEBOOK_APP_NAME)) {
				
				if (action.equalsIgnoreCase(FacebookActions.ACTION_OPEN)) {
					actions = new CommonActions(appPackage, pm);
					resultIntent = actions.getOpenApplicationIntent();
				} else if (action
						.equalsIgnoreCase(FacebookActions.ACTION_NEW_MESSAGE)) {
					actions = new FacebookActions();
//					((FacebookActions) actions).getNewPostIntent():
				} else {
					actions = new CommonActions(appPackage, pm);
					resultIntent = actions.getOpenApplicationIntent();
				}
			}else{
				actions = new CommonActions(appPackage, pm);
				resultIntent = actions.getOpenApplicationIntent();
			}
		}
		return resultIntent;
		
	}
	
	public static List<ActionVo> getApplicationSelectedActions(List<ActionVo> list, ApplicationVo appSelected, SharedPreferences sharedPref, Resources r) throws Exception{
		String idPrefFile = r.getString(R.string.idPrefFile);
		List<ActionVo> result = new ArrayList<ActionVo>();
		int i = 0;
		for (ActionVo item : list){
			String appId = idPrefFile + "-" + appSelected.getName() + "-" + item.getApplicationPackage();
			String tmp = sharedPref.getString(appId, null);
			if (tmp != null){
				result.add(item);
				list.remove(i);
			}
			i++;
		}
		return result;
	}

	public static List<ActionVo> getApplicationPossibledActions(
			ApplicationVo appSelected) throws Exception {
		List<ActionVo> result = null;
		if (ActionHelper.FACEBOOK_APP_NAME
				.trim()
				.toLowerCase()
				.equalsIgnoreCase(
						appSelected.getName().trim().toLowerCase())) {
			result = new FacebookActions().getActions();
		}
		if (result == null) {
			result = new CommonActions().getActions();
		}
		return result;
	}
	
	
	public static boolean isAssignedByApplication(ApplicationVo app, SharedPreferences sharedPref, Resources r, CommonActions actions){
		String appId = getAppId( app.getName() );
		String pattern = sharedPref.getString(appId, null);
		return (pattern != null);
	}
	
	
	public static String getActionId(String appInfo, String actionInfo){
		return idPrefFile + SEPARATOR + appInfo + SEPARATOR + actionInfo;
	}
	public static String getAppId(String appInfo){
		return idPrefFile + SEPARATOR + appInfo;
	}
	public static String getPatternId(String pattern){
		return idPrefFile + SEPARATOR + pattern;
	}
	
	
}



