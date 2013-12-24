package com.il.easyclick.views;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.il.easyclick.actions.CommonActions;

public class ApplicationVo {
	
	private String name;
	private String applicationPackage;
	private boolean assigned = false;
	private String patter;
	private ApplicationInfo applicationInfo;
	private Drawable icon;
	private CommonActions commonActions;
	private ComponentName componentName;
	
	//selected action, this is for manage application helper 
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
	@Override
	public String toString() {
		return name;
	}
	public ComponentName getComponentName() {
		return componentName;
	}
	public void setComponentName(ComponentName componentName) {
		this.componentName = componentName;
	}
	public ActionVo getCurrentAction() {
		return currentAction;
	}
	public void setCurrentAction(ActionVo currentAction) {
		this.currentAction = currentAction;
	}
	@Override
	public boolean equals(Object o) {
		ApplicationVo app = (ApplicationVo) o;
		boolean result = false;
		if ((((app != null && this != null) 
				&& (app.getCurrentAction() != null && this.getCurrentAction() != null)) 
				&& (app.getCurrentAction().getActionPackage() != null && this.getCurrentAction().getActionPackage() != null))
				&& (app.getCurrentAction().getActionPackage().equalsIgnoreCase(this.getCurrentAction().getActionPackage()))) {
			result = true;
		}
		return result;
	}
	public CommonActions getCommonActions() {
		return commonActions;
	}
	public void setCommonActions(CommonActions commonActions) {
		this.commonActions = commonActions;
	}
}
