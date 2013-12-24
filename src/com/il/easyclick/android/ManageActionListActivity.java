package com.il.easyclick.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.il.easyclick.R;
import com.il.easyclick.android.adapters.GridPagerAdapter;
import com.il.easyclick.android.fragments.ApplicationListFragment;
import com.il.easyclick.android.fragments.FilterApplicationsDialogFragment;
import com.il.easyclick.config.AppManager;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.dao.ActionsDAO;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.AllAppsList;
import com.il.easyclick.views.ApplicationVo;

public class ManageActionListActivity extends FragmentActivity implements
		FilterApplicationsDialogFragment.FilterDialogListener,
		ApplicationListFragment.ApplicationListListener, ActionBar.TabListener {

	public ActionBar actionBar;
	GridPagerAdapter mGridPagerAdapter;
	ViewPager mViewPager;
	boolean isOrderSelected = false;

	private String filterAppName;
	private int filterType;
	private ArrayList<ApplicationVo> applicationItems;
	private List<ApplicationVo> listApplications;
	int activityActionParam = AppManager.ACTIVITY_ACTION_FROM_MAIN;
	

	boolean removeFilter = false;
	
	ActionsDAO actionsDao = null;
	List<ActionVo> list = null;
	
	public void init() {
		actionsDao = new ActionsDAO(getApplicationContext());
		filterAppName = "";
		filterType = 0;
		listApplications = new ArrayList<ApplicationVo>();
		applicationItems = new ArrayList<ApplicationVo>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_patterns);

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		init();

		//validate if the call is from main or from activities 
		activityActionParam = getIntent().getIntExtra(AppManager.ACTIVITY_ACTION_PARAM,0);
		
		EasyClickApplication appState = (EasyClickApplication) getApplicationContext();
		appState.setTypeSelectAppReturn(activityActionParam);
		
		mGridPagerAdapter = new GridPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mGridPagerAdapter);
		if (isOrderSelected) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
		}
		
		//set information from db into context. 
		if (activityActionParam == AppManager.ACTIVITY_ACTION_FROM_MAIN){
			AppshortcutDAO appDao = new AppshortcutDAO();
			appDao.refreshDataDb(getApplicationContext());
		}
	}

	public void refrestTabs() {
		actionBar.removeAllTabs();
		for (int i = 0; i < mGridPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mGridPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	public void refresList() {
		List<ApplicationVo> tmpList = filterList(listApplications);
		applicationItems.clear();
		int tmp = 1;
		if ((tmpList.size() > ApplicationListFragment.GRID_SIZE)
				&& tmpList.size() % ApplicationListFragment.GRID_SIZE == 1) {
			tmp = 0;
		}
		int pages = tmpList.size() / ApplicationListFragment.GRID_SIZE + tmp;
		mGridPagerAdapter.setmViews(pages);
		applicationItems.addAll(tmpList);

		EasyClickApplication appState = ((EasyClickApplication) getApplicationContext());
		appState.setCurrentListApplications(applicationItems);

		mGridPagerAdapter.notifyDataSetChanged();
		
		if (isOrderSelected) {
			refrestTabs();
		}
		
		if (tmpList.isEmpty()) {
			Toast.makeText(getApplicationContext(), "No application found.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public List<ApplicationVo> filterList(List<ApplicationVo> list) {
		boolean someNameSearch = (filterAppName != null && !(filterAppName
				.equalsIgnoreCase("")));
		List<ApplicationVo> returnList = new ArrayList<ApplicationVo>();
		if (someNameSearch || (filterType > 0) && list != null) {
			for (ApplicationVo item : list) {
				boolean type = (filterType == 0)
						|| (filterType == 1 && item.isAssigned())
						|| (filterType == 2 && !item.isAssigned());

				boolean name = (filterAppName != null
						&& !filterAppName.equalsIgnoreCase("") && item
						.getName().toLowerCase()
						.contains(filterAppName.toLowerCase()));

				boolean add = (someNameSearch)?name && type:type;

				if (add) {
					returnList.add(item);
				}
			}
		} else {
			returnList = listApplications;
		}
		return returnList;
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
			FilterApplicationsDialogFragment fa = new FilterApplicationsDialogFragment();
			fa.show(getSupportFragmentManager(), "Filter Applications");
			break;
		case R.id.action_filter_delete:
			filterAppName = "";
			filterType = 0;
			refresList();
			removeFilter = false;
			invalidateOptionsMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_patterns, menu);
		MenuItem acRemoveFilter = menu.findItem(R.id.action_filter_delete);
		MenuItem acSearch = menu.findItem(R.id.action_search);
		acRemoveFilter.setVisible(removeFilter);
		acSearch.setVisible(!removeFilter);
		return true;
	}

	@Override
	public void onApplicationSelected(ApplicationVo _appSelected) {
		EasyClickApplication appState = (EasyClickApplication) getApplicationContext();
		appState.setAppSelected(_appSelected);
		if (activityActionParam == AppManager.ACTIVITY_ACTION_FROM_MAIN){
			Intent intent = new Intent(ManageActionListActivity.this,
					ManageActionActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(ManageActionListActivity.this, ManageActionActivity.class);
			intent.putExtra(AppManager.ACTIVITY_ACTION_PARAM, AppManager.ACTIVITY_ACTION_FROM_ACTIVITIES);
			startActivityForResult(intent, 1);
		}
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra(AppManager.ACTIVITY_ACTION_RESULT_PARAM, 1);
				if (getParent() == null) {
					setResult(RESULT_OK, returnIntent);
				} else {
					getParent().setResult(RESULT_OK, returnIntent);
				}
				finish();
			}
			if (resultCode == RESULT_CANCELED) {
				finish();
				Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		FilterApplicationsDialogFragment filterDialog = (FilterApplicationsDialogFragment) dialog;
		filterType = filterDialog.filterCheckbox;
		Dialog d = filterDialog.getDialog();
		EditText et = (EditText) d.findViewById(R.id.search_criteria);
		filterAppName = et.getText().toString();
		refresList();
		removeFilter = true;
		invalidateOptionsMenu();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) { }

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) { }

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) { }
	

	/**
	 * @author cesaregb progress dialog loads application list
	 */
	private ProgressDialog progressDialog;

	public class LoadApplication extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ManageActionListActivity.this);
			progressDialog.setMessage("Loading Applications...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			EasyClickApplication appState = (EasyClickApplication) getApplicationContext();
			AllAppsList allApp = appState.getAllAppsList();
			listApplications.clear();
			listApplications.addAll(allApp.data);
			if (allApp != null
					&& allApp.data != null
					&& allApp.data.size() > 0
					&& (activityActionParam == AppManager.ACTIVITY_ACTION_FROM_MAIN)) {
				for (ApplicationVo item : listApplications) {
					try {
						for (ActionVo action : list) { //iterate db values
							if (item.getApplicationPackage().equalsIgnoreCase(action.getParentPackage())) {
								item.setAssigned(true);
								if (item.getCommonActions() != null 
										&& item.getCommonActions().getActions() != null) {
									int i = 0;
									for (ActionVo a : item.getCommonActions().getActions()) {
										if (a.equals(action)) {
											
											item.getCommonActions().getActions()
													.get(i).setAssigned(true);
											item.getCommonActions()
													.getActions()
													.get(i)
													.setIdAction(action.getIdAction());
											item.getCommonActions()
												.getActions()
												.get(i)
												.setPattern(action.getPattern());
										}
										i++;
									}
								}
							}
						}
					} catch (Exception ase) {
					}
				}
			}
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
		EasyClickApplication appState = ((EasyClickApplication) getApplicationContext());
		list = appState.getCurrentDBActions();
		new LoadApplication().execute();
	}
}
