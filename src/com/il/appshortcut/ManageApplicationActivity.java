package com.il.appshortcut;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.IAppshortcutDAO;
import com.il.appshortcut.dao.impl.AppshortcutDAO;
import com.il.appshortcut.fragments.ApplicationInfoFragment;
import com.il.appshortcut.fragments.ApplicationSelectPatternFragment;
import com.il.appshortcut.helpers.ActionHelper;
import com.il.appshortcut.views.ApplicationActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ManageApplicationActivity extends FragmentActivity
		implements
		ApplicationInfoFragment.ApplicationInfoListener,
		ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener,
		LuncherPatternView.LuncherPatternListener {
	
	private String selectedPattern = "";
	private int numberOfActionByApplication = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_application);
		
		if (findViewById(R.id.fragment_container_application) != null) {
			if (savedInstanceState != null) {
				return;
			}

			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			ApplicationInfoFragment newFragment = new ApplicationInfoFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_application, newFragment).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, ManageAppListActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_application, menu);
		return true;
	}

	@Override
	public void onApplicationActionItem(ApplicationActionVo item) {
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		if (item.getActionPackage() == null) {
			item.setActionPackage(appSelected.getApplicationPackage());
		}
		appSelected.setCurrentAction(item);
//		appSelected.getActions().addAction(item);
		appState.setAppSelected(appSelected);
		
		
		ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container_application, newFragment)
				.addToBackStack(null).commit();

		Toast.makeText(this, "Action S: " + item.getActionPackage(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onApplicationActionItemSelected(ApplicationActionVo item) {
		Log.d(AppManager.LOG_APPLICATION_INFO_FRAGMENT, "pattern: " + item.getPatter());
		ApplicationVo appSelected = ((AppShortcutApplication)getApplicationContext()).getAppSelected();
		if (item.getActionPackage() == null) {
			item.setActionPackage(appSelected.getApplicationPackage());
		}
		appSelected.setCurrentAction(item);
		if (appSelected != null 
				&& item != null) {
			
			SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
					AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();

				
			String actionPackage = "";
			if (appSelected.getCurrentAction() != null){
				actionPackage = appSelected.getCurrentAction().getActionPackage();
			}
			String appInfo = ActionHelper.getApplicationInfo(appSelected.getComponentName());
			String actionId = ActionHelper.getActionId(appInfo, actionPackage);
			String appId = ActionHelper.getAppId(appInfo);
			String appIdPatt = ActionHelper.getPatternId(selectedPattern);

			editor.remove(actionId);
			editor.remove(appIdPatt);
			if (numberOfActionByApplication-- == 0)
				editor.remove(appId);
			
			editor.commit();
			
			
			ApplicationInfoFragment fragment = (ApplicationInfoFragment)
	                getSupportFragmentManager().findFragmentById(R.id.fragment_container_application);

	        if (fragment != null) {
	        	fragment.updateApplicationView(appSelected);
	        }
			
		}
	}

	@Override
	public void onSomething(String something) {
	}

	@Override
	public void fireApplication(String currentSelection) {
		// Not used
	}

	@Override
	public void registerSelection(String currentSelection) {
		this.selectedPattern = currentSelection;
	}

	/**
	 * @param view
	 *            Click save info on the select pattern fragment Save
	 *            information for a pattern...
	 */
	public void assignPattern(View view) {
		ApplicationVo appSelected = ((AppShortcutApplication)getApplicationContext()).getAppSelected();
		
		if (appSelected != null) {
			
			IAppshortcutDAO dao = new AppshortcutDAO();
			try{
				dao.savePattern(selectedPattern, getApplicationContext());
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Error Saving Information", Toast.LENGTH_SHORT);
			}
			
//			Context context = getApplicationContext();
//			SharedPreferences sharedPref = context.getSharedPreferences(
//					AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
//			SharedPreferences.Editor editor = sharedPref.edit();
//
//			String actionPackage = "";
//			if (appSelected.getCurrentAction() != null){
//				actionPackage = appSelected.getCurrentAction().getActionPackage();
//			}
//			String appInfo = ActionHelper.getApplicationInfo(appSelected.getComponentName());
//			String actionId = ActionHelper.getActionId(appInfo, actionPackage);
//			String appId = ActionHelper.getAppId(appInfo);
//			String appIdPatt = ActionHelper.getPatternId(selectedPattern);
// 
//			editor.putString(actionId, selectedPattern); // search by application  + action 
//			editor.putString(appId, selectedPattern); // search by application 
//			editor.putString(appIdPatt, appInfo + ActionHelper.SEPARATOR + actionPackage + ActionHelper.SEPARATOR + appSelected.getName()); //search by pattern
//
//			Toast.makeText(getApplicationContext(),
//					"Pattern: " + appIdPatt + " saved", Toast.LENGTH_SHORT)
//					.show();
//			editor.commit();

		}
		onBackPressed();
	}

	@Override
	public void updateNumberOfActionsByApplication(int number) {
		this.numberOfActionByApplication = number;
	}

}
