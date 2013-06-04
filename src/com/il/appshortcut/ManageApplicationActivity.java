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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.fragments.ApplicationInfoFragment;
import com.il.appshortcut.fragments.ApplicationSelectPatternFragment;
import com.il.appshortcut.helpers.ActionHelper;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class ManageApplicationActivity extends FragmentActivity
		implements
		ApplicationInfoFragment.ApplicationInfoListener,
		ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener,
		LuncherPatternView.LuncherPatternListener {
	
	private String selectedPattern = "";

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
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.il.appshortcut.fragments.ApplicationInfoFragment.ApplicationInfoListener
	 * #onApplicationActionItem(com.il.appshortcut.views.ApplicationActionItem)
	 * NEW This is called when the user select a list from the action [open, new
	 * message, etc...] list
	 */
	@Override
	public void onApplicationActionItem(ActionVo item) {
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		if (item.getApplicationPackage() == null) {
			item.setApplicationPackage(appSelected.getApplicationInfo().packageName);
		}
		appSelected.setCurrentAction(item);
//		appSelected.getActions().addAction(item);
		appState.setAppSelected(appSelected);
		
		
		ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fragment_container_application, newFragment)
				.addToBackStack(null).commit();

		Toast.makeText(this, "Action S: " + item.getApplicationPackage(),
				Toast.LENGTH_SHORT).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.il.appshortcut.fragments.ApplicationInfoFragment.ApplicationInfoListener
	 * #onApplicationActionItemSelected(com.il.appshortcut.views.
	 * ApplicationActionItem) EDIT This is called when the user select a list
	 * from the action [open, new message, etc...] list
	 */
	@Override
	public void onApplicationActionItemSelected(ActionVo item) {
		Toast.makeText(this,
				"onApplicationActionItemSelected : " + item.getApplicationPackage(),
				Toast.LENGTH_SHORT).show();
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
			Context context = getApplicationContext();
			SharedPreferences sharedPref = context.getSharedPreferences(
					String.valueOf(R.string.idPrefFile), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();

			String actionName = "";
			if (appSelected.getCurrentAction() != null){
				actionName = appSelected.getCurrentAction().getName();
			}
			String actionId = ActionHelper.getActionId( appSelected.getName(), actionName);
			String appId = ActionHelper.getAppId( appSelected.getName() );
			String appIdPatt = ActionHelper.getPatternId(selectedPattern);
 
			editor.putString(actionId, selectedPattern); // search by application  + action 
			editor.putString(appId, selectedPattern); // search by application 
			editor.putString(appIdPatt, appSelected.getName() + "-" + actionName); //search by pattern

			Toast.makeText(getApplicationContext(),
					"Pattern: " + appIdPatt + " saved", Toast.LENGTH_SHORT)
					.show();
			editor.commit();

		}
		onBackPressed();
	}

}
