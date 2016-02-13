package com.il.easyclick.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.easyclick.R;
import com.il.easyclick.android.adapters.ActivityItemAdapter;
import com.il.easyclick.android.fragments.ActivityListFragment;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.views.ActivityVo;

public class ManageAcivitiesListActivity extends FragmentActivity implements
		ActivityItemAdapter.ActivityListListener, 
		ActivityListFragment.ActivityListFragmentListener {

	private ArrayList<ActivityVo> activityItems;
	private ActivityItemAdapter aa;
	private List<ActivityVo> listActivities;
	ActivitiesDAO activitiesDao;
	ActivityListFragment listFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_acivities_list);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		FragmentManager fm = getSupportFragmentManager();
		listFragment = (ActivityListFragment) fm
				.findFragmentById(R.id.ActivityFragment);
		listFragment.setmCallback(this);

		activityItems = new ArrayList<ActivityVo>();
		int resID = R.layout.comp_activities_list_item;
		aa = new ActivityItemAdapter(this, resID, activityItems);
		aa.setCallback(this);
		listFragment.setListAdapter(aa);

		listActivities = new ArrayList<ActivityVo>();
		activitiesDao = new ActivitiesDAO(getApplicationContext());
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
	
	public void loadNewActivityFragment() {
		EasyClickApplication appState = ((EasyClickApplication) getApplicationContext());
		appState.setCurrentActivity(null);
		Intent i = new Intent(ManageAcivitiesListActivity.this,
				ManageActivityActivity.class);
		startActivity(i);
	}
	
	public void refresList() {
		if (listActivities != null){
			aa.setItems(listActivities);
			for (ActivityVo item : listActivities){
				onNewItemAdded(item);
			}
		}else{
			activityItems.clear();
			aa.notifyDataSetChanged();
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
			if (listActivities == null){
				listActivities = new ArrayList<ActivityVo>();
			}
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

	public void itemSelected(ActivityVo activity) {
		//remove menu
		if (mActionMode != null) {((ActionMode) mActionMode).finish();}
		aa.notifyDataSetChanged();
		listFragment.setPrev(null);
		
		EasyClickApplication appState = ((EasyClickApplication)getApplicationContext());
		appState.setCurrentActivity(activity);
		Intent i = new Intent(ManageAcivitiesListActivity.this, ManageActivityActivity.class);
		startActivity(i);
	}

	protected Object mActionMode;
	private ActivityVo mActivityLongOver;
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.manage_common_list_context, menu);
	        return true;
	    }

	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.action_deselect:
	                removeSelectedLongOverItem();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	        }
	    }

	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	mActivityLongOver = null;
	        mActionMode = null;
	        aa.notifyDataSetChanged();
	        listFragment.setPrev(null);
	    }
	};

	
	public boolean longPressApplicationActionItem(ActivityVo item, View eventView) {
		return false;
	}
	
	public void removeSelectedLongOverItem(){
		if (mActivityLongOver != null){
			try{
				AppshortcutDAO dao = new AppshortcutDAO();
				dao.removeActivity(mActivityLongOver, getApplicationContext(),
						mActivityLongOver.getPattern());
				new LoadActivities().execute();
			}catch(Exception e){
				Toast.makeText(getApplicationContext(),
						"error deleting activity", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public boolean onItemLongClickListener(Object object, int position, View eventView) {
		ActivityVo activity = (ActivityVo)object;
		if (mActionMode == null && activity.getIdActivity() > 0) {
			mActivityLongOver = activity;
			mActionMode = ManageAcivitiesListActivity.this
					.startActionMode(mActionModeCallback);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean onItemClickListener(Object object, int position,
			View eventView) {
		ActivityVo activity = (ActivityVo)object;
		itemSelected(activity);
		return false;
	}
	
}
