package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.il.appshortcut.R;
import com.il.appshortcut.actions.ICommonActions;
import com.il.appshortcut.actions.imp.CommonActions;
import com.il.appshortcut.actions.imp.FacebookActions;
import com.il.appshortcut.views.ApplicationActionItem;
import com.il.appshortcut.views.ApplicationItem;

public class ActionHelper {
	
	public static final String FACEBOOK_APP_NAME = "facebook";

	public static Intent getIntent(String pattern,
			SharedPreferences sharedPref, Resources r, PackageManager pm)
			throws Exception {
		Intent resultIntent = null;
		String idPrefFile = r.getString(R.string.idPrefFile);
		String id = idPrefFile + "-" + pattern;
		String tmp = sharedPref.getString(id, null);
		if (tmp != null) {
			String[] split = tmp.split("-");
			
			String appName = split[0]; 
			String appPackage = split[1]; 
			String action = split[2]; 
			
			ICommonActions actions = null;
			if (appName.equalsIgnoreCase(FACEBOOK_APP_NAME)) {
				if (action.equalsIgnoreCase(FacebookActions.ACTION_OPEN)) {
					actions = new CommonActions(appPackage, pm);
					resultIntent = actions.getOpenApplicationIntent();
				} else if (action
						.equalsIgnoreCase(FacebookActions.ACTION_NEW_MESSAGE)) {
					actions = new FacebookActions();
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
	
	public static List<ApplicationActionItem> getApplicationSelectedActions(List<ApplicationActionItem> list, ApplicationItem appSelected, SharedPreferences sharedPref, Resources r) throws Exception{
		String idPrefFile = r.getString(R.string.idPrefFile);
		List<ApplicationActionItem> result = new ArrayList<ApplicationActionItem>();
		int i = 0;
		for (ApplicationActionItem item : list){
			String appId = idPrefFile + "-" + appSelected.getApplicationName() + "-" + item.getAction();
			String tmp = sharedPref.getString(appId, null);
			if (tmp != null){
				result.add(item);
				list.remove(i);
			}
			i++;
		}
		return result;
	}

	public static List<ApplicationActionItem> getApplicationPossibledActions(
			ApplicationItem appSelected) throws Exception {
		List<ApplicationActionItem> result = null;
		if (ActionHelper.FACEBOOK_APP_NAME
				.trim()
				.toLowerCase()
				.equalsIgnoreCase(
						appSelected.getApplicationName().trim().toLowerCase())) {
			result = FacebookActions.getActions();
		}
		if (result == null) {
			result = CommonActions.getActions();
		}
		return result;
	}
	
	
	public static boolean isAssignedByApplication(ApplicationItem app, SharedPreferences sharedPref, Resources r){
		String idPrefFile = r.getString(R.string.idPrefFile);
		String appId = idPrefFile + "-" + app.getApplicationName();
		String pattern = sharedPref.getString(appId, null);
		return (pattern != null);
	}
	
	
}



