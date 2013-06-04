package com.il.appshortcut.views;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.il.appshortcut.actions.CommonActions;

public class ApplicationVo{
	
	private String name;
	private String applicationPackage;
	private boolean assigned = false;
	private String patter;
	private ApplicationInfo applicationInfo;
	private Drawable icon;
	private CommonActions actions;
	private ActionVo currentAction;
	
	public ApplicationVo(String applicationName){
		this.name = applicationName;
	}
	public ApplicationVo(String applicationName, ApplicationInfo appInfo){
		this.name = applicationName;
		this.applicationInfo = appInfo;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	public String getPatter() {
		return patter;
	}
	public void setPatter(String patter) {
		this.patter = patter;
	}
	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}
	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApplicationPackage() {
		return applicationPackage;
	}
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}
	public CommonActions getActions() {
		return actions;
	}
	public void setActions(CommonActions actions) {
		this.actions = actions;
	}


	@Override
	public String toString() {
		return name;
	}
	public ActionVo getCurrentAction() {
		return currentAction;
	}
	public void setCurrentAction(ActionVo currentAction) {
		this.currentAction = currentAction;
	}
}
