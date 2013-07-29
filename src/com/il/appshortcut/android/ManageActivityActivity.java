package com.il.appshortcut.android;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.il.appshortcut.R;
import com.il.appshortcut.android.fragments.ActivityFormFragment;

public class ManageActivityActivity extends FragmentActivity implements
		ActivityFormFragment.ActivityFormListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_activity);

		if (findViewById(R.id.fragment_container_activity) != null) {
			if (savedInstanceState != null) {
				return;
			}

			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			ActivityFormFragment newFragment = new ActivityFormFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_activity, newFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_activity, menu);
		return true;
	}

	@Override
	public void saveActivity() {
		// TODO Auto-generated method stub
		
	}

}
