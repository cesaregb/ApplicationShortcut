package com.il.appshortcut.config;

import java.util.List;

import android.app.Application;

import com.il.appshortcut.views.ApplicationItem;

public class AppShortcutApplication extends Application{
	
	private ApplicationItem appSelected;
	
	private List<ApplicationItem> currentListApplications;

	public ApplicationItem getAppSelected() {
		return appSelected;
	}

	public void setAppSelected(ApplicationItem appSelected) {
		this.appSelected = appSelected;
	}

	public List<ApplicationItem> getCurrentListApplications() {
		return currentListApplications;
	}

	public void setCurrentListApplications(
			List<ApplicationItem> currentListApplications) {
		this.currentListApplications = currentListApplications;
	} 

}
