package com.il.appshortcut.actions.imp;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.il.appshortcut.actions.ICommonActions;
import com.il.appshortcut.views.ApplicationActionItem;

public class CommonActions implements ICommonActions {
	public static final String ACTION_OPEN = "open";
	
	
	private String appPackage; 
	private PackageManager pm;
	
	
	protected static List<ApplicationActionItem> actions; 
	
	public static List<ApplicationActionItem> getActions(){
		actions = new ArrayList<ApplicationActionItem>();
		ApplicationActionItem action = new ApplicationActionItem();
		action.setAction(null);
		action.setName("Open");
		actions.add(action);
		return actions;
	}
	
	public CommonActions(){ }
	
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
	
	@Override
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
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}
}
