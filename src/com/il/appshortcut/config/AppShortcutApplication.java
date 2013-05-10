package com.il.appshortcut.config;

import android.app.Application;

import com.il.appshortcut.views.ApplicationItem;

public class AppShortcutApplication extends Application{
	
	private ApplicationItem appSelected;

	public ApplicationItem getAppSelected() {
		return appSelected;
	}

	public void setAppSelected(ApplicationItem appSelected) {
		this.appSelected = appSelected;
	} 

}
