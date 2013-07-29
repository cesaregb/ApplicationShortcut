package com.il.appshortcut.android;

import android.app.ActionBar;
import android.content.Intent;
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

import com.il.appshortcut.R;
import com.il.appshortcut.android.fragments.ApplicationInfoFragment;
import com.il.appshortcut.android.fragments.ApplicationSelectPatternFragment;
import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.impl.ActionsDAO;
import com.il.appshortcut.dao.impl.AppshortcutDAO;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ManageActionActivity extends FragmentActivity
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
			Intent upIntent = new Intent(this, ManageActionListActivity.class);
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
	public void onApplicationActionItem(ActionVo item) {
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		//regardless action set the common information for this type of applications.
		item.setParentPackage(appSelected.getApplicationPackage());
		item.setType(ActionVo.TYPE_APPLICATION);
		item.setAssigned(true);
		if (item.getActionPackage() == null) {
			item.setActionPackage(appSelected.getApplicationPackage());
		}
		appSelected.setCurrentAction(item);
		appState.setAppSelected(appSelected);
		
		ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container_application, newFragment)
				.addToBackStack(null).commit();
		
		Log.d(AppManager.LOG_MANAGE_APPLICATIONS, "Application seleted.");
	}

	@Override
	public void onApplicationActionItemSelected(ActionVo item) {
		Log.d(AppManager.LOG_APPLICATION_INFO_FRAGMENT, "pattern: " + item.getPattern());
		ApplicationVo appSelected = ((AppShortcutApplication)getApplicationContext()).getAppSelected();
		if (item.getActionPackage() == null) {
			item.setActionPackage(appSelected.getApplicationPackage());
		}
		appSelected.setCurrentAction(item);
//		if (appSelected != null 
//				&& item != null) {
//			
//			SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
//					AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
//			SharedPreferences.Editor editor = sharedPref.edit();
//
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
//			editor.remove(actionId);
//			editor.remove(appIdPatt);
//			if (numberOfActionByApplication-- == 0)
//				editor.remove(appId);
//			
//			editor.commit();
//			
//			
//			ApplicationInfoFragment fragment = (ApplicationInfoFragment)
//	                getSupportFragmentManager().findFragmentById(R.id.fragment_container_application);
//
//	        if (fragment != null) {
//	        	fragment.updateApplicationView(appSelected);
//	        }
//			
//		}
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
			
			AppshortcutDAO dao = new AppshortcutDAO();
			ActionsDAO actionsDao = new ActionsDAO(getApplicationContext());
			try{
				Log.d(AppManager.LOG_MANAGE_APPLICATIONS, "saving applicatoin...");
				if(dao.isPatternAssigned(selectedPattern, getApplicationContext())){
					Toast.makeText(getApplicationContext(), "Pattern assigned Show confirmation ", Toast.LENGTH_SHORT).show();
				}else{
					dao.savePattern(selectedPattern, getApplicationContext(), AppshortcutDAO.PREF_TYPE_ACTION);
					appSelected.getCurrentAction().setPattern(selectedPattern);
					actionsDao.addAction(appSelected.getCurrentAction());
					dao.refreshDataDb(getApplicationContext());
				}
			}catch(Exception e){
				//TODO Add String...
				Toast.makeText(getApplicationContext(), "Error Saving Information", Toast.LENGTH_SHORT).show();
			}
		}
		onBackPressed();
	}

	@Override
	public void updateNumberOfActionsByApplication(int number) {
		this.numberOfActionByApplication = number;
	}

}
