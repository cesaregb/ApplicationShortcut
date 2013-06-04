package com.il.appshortcut;

import static com.il.appshortcut.helpers.ActionHelper.isAssignedByApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
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

import com.il.appshortcut.adapters.GridPagerAdapter;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.fragments.ApplicationListFragment;
import com.il.appshortcut.fragments.FilterApplications;
import com.il.appshortcut.views.AllAppsList;
import com.il.appshortcut.views.ApplicationVo;

public class ManageAppListActivity extends FragmentActivity implements
		FilterApplications.FilterDialogListener,
		ApplicationListFragment.ApplicationListListener, ActionBar.TabListener {

	public ActionBar actionBar;
	GridPagerAdapter mGridPagerAdapter;
	ViewPager mViewPager;
	boolean isOrderSelected = false;

	private String filterAppName;
	private int filterType;
	private ArrayList<ApplicationVo> applicationItems;
	private List<ApplicationVo> listApplications;

	boolean removeFilter = false;

	public void init() {
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
		new LoadApplication().execute();

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

		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
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
			FilterApplications fa = new FilterApplications();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.il.appshortcut.fragments.ApplicationListFragment.ApplicationListListener
	 * #onApplicationSelected(com.il.appshortcut.views.ApplicationItem) This is
	 * when the user select an applicatoin from the list
	 */
	@Override
	public void onApplicationSelected(ApplicationVo _appSelected) {
		AppShortcutApplication appState = (AppShortcutApplication) getApplicationContext();
		appState.setAppSelected(_appSelected);
		Intent intent = new Intent(ManageAppListActivity.this,
				ManageApplicationActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		FilterApplications filterDialog = (FilterApplications) dialog;
		filterType = filterDialog.filterCheckbox;
		Dialog d = filterDialog.getDialog();
		EditText et = (EditText) d.findViewById(R.id.search_criteria);
		filterAppName = et.getText().toString();
		refresList();
		removeFilter = true;
		invalidateOptionsMenu();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	

	/**
	 * @author cesaregb progress dialog loads application list
	 */
	private ProgressDialog progressDialog;

	public class LoadApplication extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(ManageAppListActivity.this);
			progressDialog.setMessage("Loading Applicationss...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			AllAppsList allApp = new AllAppsList();

			ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
			PackageManager manager = getPackageManager();

			Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

			List<ResolveInfo> resolveInfos = manager.queryIntentActivities(
					mainIntent, 0);

			Collections.sort(resolveInfos,
					new ResolveInfo.DisplayNameComparator(manager));
			int i = 0;
			for (ResolveInfo info : resolveInfos) {
				publishProgress((int) ((i / (float) resolveInfos.size()) * 100));
				ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
				applist.add(applicationInfo);
				ApplicationVo item = new ApplicationVo(
						(String) manager.getApplicationLabel(applicationInfo));
				item.setApplicationInfo(applicationInfo);

				SharedPreferences sharedPref = getApplicationContext()
						.getSharedPreferences(
								String.valueOf(R.string.idPrefFile),
								Context.MODE_PRIVATE);
				Resources r = getResources();
				boolean assigned = isAssignedByApplication(item, sharedPref, r, item.getActions());
				item.setAssigned(assigned);
				item.setApplicationPackage(info.activityInfo.applicationInfo.packageName);
				allApp.add(item);
			}
			listApplications.addAll(allApp.data);
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

}
