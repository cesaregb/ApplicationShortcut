package com.il.appshortcut.actions;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.il.appshortcut.views.ApplicationActionVo;

public class CommonActions {

//com.joelapenna.foursquared foursquare
//com.twitter.android twitter
//com.google.android.youtube
//com.google.android.apps.maps
//com.google.android.gm - gmail
//com.instagram.android
	
	public static final String ACTION_OPEN = "open";
	
	protected List<ApplicationActionVo> actions; 
	
	private String appPackage; 
	private PackageManager pm;
	
	public List<ApplicationActionVo> getActions(){
		return actions;
	}
	
	public CommonActions(){
		actions = new ArrayList<ApplicationActionVo>();
		ApplicationActionVo action = new ApplicationActionVo();
		action.setName(ACTION_OPEN);
		action.setAssigned(false);
		action.setActionPackage("");
		action.setDescription("Open the application");
		action.setPatter("");
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
