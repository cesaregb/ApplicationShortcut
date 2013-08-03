package com.il.appshortcut.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.android.adapters.ActivityItemAdapter;
import com.il.appshortcut.android.fragments.ActivityListFragment;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.views.ActivityVo;

public class ManageAcivitiesListActivity extends Activity implements
		ActivityItemAdapter.ActivityListListener {

	private ArrayList<ActivityVo> activityItems;
	private ActivityItemAdapter aa;
	private List<ActivityVo> listActivities;
	ActivitiesDAO activitiesDao;

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
		
		activityItems = new ArrayList<ActivityVo>();
		int resID = R.layout.comp_activities_list_item;
		aa = new ActivityItemAdapter(this, resID, activityItems);
		aa.setCallback(this);
		listFragment.setListAdapter(aa);
		
		listActivities = new ArrayList<ActivityVo>();
		activitiesDao =  new ActivitiesDAO(getApplicationContext());
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_acivities_list, menu);
		return true;
	}

	public void onNewItemAdded(ActivityVo activity) {
		activityItems.add(0, activity);
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
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		appState.setCurrentActivity(null);
		Intent i = new Intent(ManageAcivitiesListActivity.this, ManageActivityActivity.class);
		startActivity(i);
	}
	
	public void refresList() {
		if (listActivities != null){
			aa.setItems(listActivities);
			for (ActivityVo item : listActivities){
				onNewItemAdded(item);
			}
		}else{
			Toast.makeText(getApplicationContext(), "No activities where found", Toast.LENGTH_SHORT).show();
		}
	}

	
	/**
	 * @author cesaregb progress dialog loads application list
	 */
	private ProgressDialog progressDialog;

	public class LoadActivities extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ManageAcivitiesListActivity.this);
			progressDialog.setMessage("Loading Actions...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			listActivities.clear();
			listActivities = activitiesDao.getAllActivities();
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		protected void onPostExecute(String params) {
			refresList();
			progressDialog.dismiss();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new LoadActivities().execute();
	}

	@Override
	public void itemSelected(ActivityVo activity) {
		AppShortcutApplication appState = ((AppShortcutApplication)getApplicationContext());
		appState.setCurrentActivity(activity);
		Intent i = new Intent(ManageAcivitiesListActivity.this, ManageActivityActivity.class);
		startActivity(i);
	}

}
