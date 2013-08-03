package com.il.appshortcut.android;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.android.fragments.ActivityFormFragment;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.ApplicationVo;

public class ManageActivityActivity extends FragmentActivity implements
		ActivityFormFragment.ActivityFormListener {
	
	ActivityVo activity;
	ActivitiesDAO activitiesDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_activity);

		if (findViewById(R.id.fragment_container_activity) != null) {
			if (savedInstanceState != null) { return; }
			
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			ActivityFormFragment formFragment = new ActivityFormFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_activity, formFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_activity, menu);
		return true;
	}

	public void callSaveAction(View view){
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		activity = appState.getCurrentActivity();
		if (activity == null){
			activity = new ActivityVo();	
		}
		ActivitiesDAO activitiDao = new ActivitiesDAO(getApplicationContext());
		String textName = "";
		String textDescription = "";
		View formView = findViewById(R.id.fragment_container_activity);
		if (formView != null) {
			EditText editTextName = (EditText)formView.findViewById(R.id.activityName);
			textName =  editTextName.getText().toString();
			EditText editTextDescription = (EditText)formView.findViewById(R.id.acticityDescription);
			textDescription =  editTextDescription.getText().toString();
		}
		activity.setName(textName);
		activity.setDescription(textDescription);
		activitiDao.addUpdateActivity(activity);
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	public void addApplication(View view){
		Intent intent = new Intent(ManageActivityActivity.this, ManageActionListActivity.class);
		intent.putExtra(AppManager.ACTIVITY_ACTION_PARAM, AppManager.ACTIVITY_ACTION_FROM_ACTIVITIES);
		startActivityForResult(intent, 1);
	}
	
	public void addService(View view){
		//TODO show service list... 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				int result = data.getIntExtra(AppManager.ACTIVITY_ACTION_RESULT_PARAM, 0);
				AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
				ApplicationVo app = appState.getAppSelected();
				if (app != null){
					Toast.makeText(getApplicationContext(), "From response: " + result + " -->" + app.getApplicationPackage(), Toast.LENGTH_SHORT).show();
				}
			}
			if (resultCode == RESULT_CANCELED) {
				//TODO no application selected!!  
			}
		}
	}

}
