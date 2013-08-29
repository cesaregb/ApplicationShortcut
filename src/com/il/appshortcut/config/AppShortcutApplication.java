package com.il.appshortcut.config;

import java.util.List;

import android.app.Application;

import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.AllAppsList;
import com.il.appshortcut.views.ApplicationVo;

public class AppShortcutApplication extends Application{
	private ApplicationVo appSelected;
	private List<ApplicationVo> currentListApplications;
	private List<ActionVo> currentDBActions;
	private ActivityVo currentActivity;
	private int typeSelectAppReturn = AppManager.ACTIVITY_ACTION_FROM_MAIN; //0=main; 1=Activities;
	
	private AllAppsList allAppsList;
	

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

	public ActivityVo getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(ActivityVo currentActivity) {
		this.currentActivity = currentActivity;
	}

	public int getTypeSelectAppReturn() {
		return typeSelectAppReturn;
	}

	public void setTypeSelectAppReturn(int typeSelectAppReturn) {
		this.typeSelectAppReturn = typeSelectAppReturn;
	}

	public AllAppsList getAllAppsList() {
		return allAppsList;
	}

	public void setAllAppsList(AllAppsList allAppsList) {
		this.allAppsList = allAppsList;
	}

}
