package com.il.easyclick.actions;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.il.easyclick.views.ActionVo;

public class CommonActions {

//com.joelapenna.foursquared foursquare
//com.google.android.apps.maps
//com.google.android.gm - gmail
	
	public static final String ACTION_OPEN = "open";
	
	protected List<ActionVo> actions; 
	
	private String appPackage; 
	private PackageManager pm;
	
	public List<ActionVo> getActions(){
		return actions;
	}
	
	public CommonActions(String actionPackage, String className){
		actions = new ArrayList<ActionVo>();
		ActionVo action = new ActionVo();
		action.setActionName(ACTION_OPEN);
		action.setAssigned(false);
		action.setActionPackage(actionPackage);
		action.setActionDescription("Open the application");
		action.setPattern("");
		action.setClassName(className);
		actions.add(action);
	}
	public CommonActions(String appPackage, PackageManager pm){
		this.appPackage = appPackage;
		this.pm = pm;
	}
	
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	public PackageManager getPm() {
		return pm;
	}
	public void setPm(PackageManager pm) {
		this.pm = pm;
	}
	
	public Intent getOpenApplicationIntent() throws Exception {
		Intent intent = null;
		String mainActivity = "";
		Intent mIntent = pm.getLaunchIntentForPackage(appPackage); 
		
		if (mIntent != null) {
		    if (mIntent.getComponent() != null) {
		    	mainActivity = mIntent.getComponent().getClassName();
		    }
		}
		
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(new ComponentName(appPackage, mainActivity));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return intent;
	}
	
	public Intent getOpenApplicationIntent(String className) throws Exception {
		Intent intent = null;
		intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(new ComponentName(appPackage, className));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
				Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		return intent;
	}

}
