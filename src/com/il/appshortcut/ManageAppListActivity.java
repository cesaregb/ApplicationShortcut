package com.il.appshortcut;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.fragments.ApplicationListFragment;
import com.il.appshortcut.fragments.FilterApplications;
import com.il.appshortcut.views.ApplicationItem;

public class ManageAppListActivity extends FragmentActivity
		implements
		ApplicationListFragment.ApplicationListListener,
		FilterApplications.FilterDialogListener{

	public final String ACTIVITY_MANAGE_PATTERNS = "ActivityManagePatterns";
	ApplicationListFragment appGridFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_patterns);
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			appGridFragment = new ApplicationListFragment();
			appGridFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, appGridFragment).commit();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent upIntent = new Intent(this, MainActivity.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
					TaskStackBuilder.create(this).addNextIntent(upIntent)
							.startActivities();
					finish();
				} else {
					NavUtils.navigateUpTo(this, upIntent);
				}
			break; 
			case R.id.action_search:
				FilterApplications fa = new FilterApplications();
				fa.show(getSupportFragmentManager(), "Filter Applications");
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_patterns, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.il.appshortcut.fragments.ApplicationListFragment.ApplicationListListener#onApplicationSelected(com.il.appshortcut.views.ApplicationItem)
	 * This is when the user select an applicatoin from the list
	 */
	@Override
	public void onApplicationSelected(ApplicationItem _appSelected) {
		AppShortcutApplication appState = (AppShortcutApplication) getApplicationContext();
		appState.setAppSelected(_appSelected);
		Intent intent = new Intent(ManageAppListActivity.this, ManageApplicationActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		FilterApplications filterDialog = (FilterApplications) dialog; 
		int filterType = filterDialog.filterCheckbox;
		Dialog d = filterDialog.getDialog();
		EditText et = (EditText) d.findViewById(R.id.search_criteria);
		String appName = et.getText().toString();
		
		appGridFragment.setFilterValues(appName, filterType);
		appGridFragment.refresList();
	}
	

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		if (fragmentLoaded == 1) {
//			fragmentLoaded = 0;
//			appSelected = null;
//			appGridFragment.refresAppList();
//		} else if (fragmentLoaded == 2) {
//			fragmentLoaded = 1;
//			// TODO pending
//		}
//	}

	

}
