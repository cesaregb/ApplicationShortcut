package com.il.appshortcut.config;

import java.util.List;

import android.app.Application;

import com.il.appshortcut.views.ApplicationVo;

public class AppShortcutApplication extends Application{
	
	private ApplicationVo appSelected;
	
	private List<ApplicationVo> currentListApplications;

	public ApplicationVo getAppSelected() {
		return appSelected;
	}

	public void setAppSelected(ApplicationVo appSelected) {
		this.appSelected = appSelected;
	}

	public List<ApplicationVo> getCurrentListApplications() {
		return currentListApplications;
	}

	public void setCurrentListApplications(
			List<ApplicationVo> currentListApplications) {
		this.currentListApplications = currentListApplications;
	}

}
