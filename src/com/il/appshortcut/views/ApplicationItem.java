package com.il.appshortcut.views;

import android.content.pm.ApplicationInfo;

public class ApplicationItem {
	
	private String applicationName;
	private boolean assigned; 
	private String patter;
	private ApplicationInfo applicationInfo;
	private ApplicationActionItem applicationActionItem;
	
	public ApplicationItem(){
	}
	
	public ApplicationItem(String applicationName){
		this.applicationName = applicationName;
	}
	public ApplicationItem(String applicationName, ApplicationInfo appInfo){
		this.applicationName = applicationName;
		this.applicationInfo = appInfo;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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
	public ApplicationActionItem getApplicationActionItem() {
		return applicationActionItem;
	}
	public void setApplicationActionItem(ApplicationActionItem applicationActionItem) {
		this.applicationActionItem = applicationActionItem;
	}
	
	@Override
	public String toString() {
		return applicationName;
	}


}
