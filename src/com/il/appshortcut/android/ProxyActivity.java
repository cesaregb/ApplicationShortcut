package com.il.appshortcut.android;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.helpers.RunnableHelper;

public class ProxyActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			String currentSelection = getIntent().getStringExtra(AppManager.WIDGET_PROXY_SELECTION);
			Log.d(AppManager.LOG_DEBUGGIN, "currentSelection: " + currentSelection);
			if (currentSelection != null){
				RunnableHelper runnableHelper = new RunnableHelper(getApplicationContext());
				ActionsDAO actionsDao =  new ActionsDAO(getApplicationContext());
				runnableHelper.setActionsDao(actionsDao);
				List<Intent> lIntent = runnableHelper.getIntentList(currentSelection, getApplicationContext());
				new RunnableHelper(getApplication(), lIntent).execute();
			}
		}catch(Exception e){
			Log.d(AppManager.LOG_EXCEPTIONS, "Exception on running... ");
			//TODO handle exception...
		}
	}
	
}
