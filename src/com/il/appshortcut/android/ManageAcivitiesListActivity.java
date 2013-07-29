package com.il.appshortcut.android;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.adapters.ActivityItemAdapter;
import com.il.appshortcut.android.fragments.ActivityListFragment;
import com.il.appshortcut.views.ActivityItem;

public class ManageAcivitiesListActivity extends Activity {

	private ArrayList<ActivityItem> activityItems;
	private ActivityItemAdapter aa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_acivities_list);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		
		FragmentManager fm = getFragmentManager();
		
		ActivityListFragment listFragment = (ActivityListFragment) fm
				.findFragmentById(R.id.ActivityFragment);
		
		activityItems = new ArrayList<ActivityItem>();
		int resID = R.layout.activity_list_item;
		aa = new ActivityItemAdapter(this, resID, activityItems);
		listFragment.setListAdapter(aa);
		
		addDummyData();
	}

	public void addDummyData(){
		for (int i = 0; i <= 15; i++){
			onNewItemAdded("item: " + i);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_acivities_list, menu);
		return true;
	}

	public void onNewItemAdded(String newItem) {
		ActivityItem newTodoItem = new ActivityItem(newItem);
		activityItems.add(0, newTodoItem);
		aa.notifyDataSetChanged();
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
			case R.id.action_new:
				loadNewActivityFragment();
			break; 
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void loadNewActivityFragment(){
		Toast.makeText(getApplicationContext(), "new activity", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(ManageAcivitiesListActivity.this, ManageActivityActivity.class);
		startActivity(i);
	}

}
