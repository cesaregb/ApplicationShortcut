package com.il.appshortcut.android;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.actions.ActionsFactory;
import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.android.views.Utilities;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.helpers.RunnableHelper;
import com.il.appshortcut.views.AllAppsList;
import com.il.appshortcut.views.ApplicationVo;

public class MainActivity extends Activity implements
		LuncherPatternView.LuncherPatternListener {

	LuncherPatternView luncherWidget;
	AppshortcutDAO dao = new AppshortcutDAO();
	ActionsDAO actionsDao;
	
	
	private AllAppsList allAppsList;
	int activityActionParam = AppManager.ACTIVITY_ACTION_FROM_MAIN;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		luncherWidget = (LuncherPatternView) findViewById(R.id.luncher_widget);
		actionsDao = new ActionsDAO(getApplicationContext());
		
		new LoadApplication().execute();
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
		case R.id.action_clear:
			SharedPreferences sharedPref = getApplicationContext()
			.getSharedPreferences(AppManager.ID_PRE_FFILE,
					Context.MODE_PRIVATE);
			sharedPref.edit().clear().commit();
			
			ActionsDAO actionsDao = new ActionsDAO(getApplicationContext());
			actionsDao.clearDatabase();
			
			Toast.makeText(getApplicationContext(), "The patters had been deleted", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * @param view
	 * this fires the Manage Applications  
	 */
	public void openManageActions(View view){
		Intent intent = new Intent(MainActivity.this, ManageActionListActivity.class);
		intent.putExtra(AppManager.ACTIVITY_ACTION_PARAM, AppManager.ACTIVITY_ACTION_FROM_MAIN);
		startActivity(intent);
	}
	
	/**
	 * @param view
	 * this fires the Manage Activities  
	 */
	public void openManageActivities(View view){
		Intent intent = new Intent(MainActivity.this, ManageAcivitiesListActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void fireApplication(String currentSelection) {
		RunnableHelper runnableHelper = new RunnableHelper(getApplicationContext());
		runnableHelper.setAllAppsList(allAppsList);
		runnableHelper.setActionsDao(actionsDao);
		List<Intent> lIntent = runnableHelper.getIntentList(currentSelection, getApplicationContext());
		new RunnableHelper(getApplication(), lIntent).execute();
	}

	public void showProcessRuning(){
		ActivityManager manager = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = manager
				.getRunningAppProcesses();

		for (RunningAppProcessInfo process : processes) {
			Log.d(AppManager.LOG_DEBUGGIN, "process Name: " + process.processName);
		}
	}
	
	@Override
	public void registerSelection(String currentSelection) {
		// Not Used
	}

	/**
	 * @author cesaregb progress dialog loads application list
	 */
	private ProgressDialog progressDialog;

	public class LoadApplication extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("Loading Applications...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			allAppsList = new AllAppsList(); // Application list helper 
			List<ResolveInfo> applicationList = null; // android object list... 
			
			final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			
			PackageManager manager = getPackageManager();
			applicationList = manager.queryIntentActivities(mainIntent, 0);

			Collections.sort(applicationList, new ResolveInfo.DisplayNameComparator(manager));
			
			if (applicationList.size() > 0 ){
				for (ResolveInfo info : applicationList) {
					ApplicationInfo applicationInfo = info.activityInfo.applicationInfo;
					ApplicationVo item = new ApplicationVo(info.loadLabel(manager).toString());
					item.setIcon(info.loadIcon(manager));
					item.setIcon(Utilities.createIconThumbnail(item.getIcon(), getApplicationContext()));
					item.setComponentName(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
					item.setApplicationInfo(applicationInfo);
					item.setApplicationPackage(applicationInfo.packageName);
					item.setAssigned(false);
					item.setCommonActions(ActionsFactory.create(item));
					allAppsList.add(item);
				}
			}
			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		protected void onPostExecute(String params) {
			addApplicationsToContext();
			progressDialog.dismiss();
		}
		
	}
	public void addApplicationsToContext(){
		AppShortcutApplication appState = (AppShortcutApplication) getApplicationContext();
		appState.setAllAppsList(allAppsList);
	}
}
