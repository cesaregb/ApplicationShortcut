package com.il.appshortcut.config;

import java.util.List;

import android.app.Application;

import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class AppShortcutApplication extends Application{
	private ApplicationVo appSelected;
	private List<ApplicationVo> currentListApplications;
	private List<ActionVo> currentDBActions;

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

	public List<ActionVo> getCurrentDBActions() {
		return currentDBActions;
	}

	public void setCurrentDBActions(List<ActionVo> currentDBActions) {
		this.currentDBActions = currentDBActions;
	}

}
