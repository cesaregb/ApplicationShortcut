package com.il.appshortcut.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.impl.ActionsDAO;
import com.il.appshortcut.dao.impl.AppshortcutDAO;
import com.il.appshortcut.views.ActionVo;

import static com.il.appshortcut.helpers.ActionHelper.getPatternIntent;


public class MainActivity extends Activity implements
		LuncherPatternView.LuncherPatternListener {

	LuncherPatternView luncherWidget;
	AppshortcutDAO dao = new AppshortcutDAO();
	ActionsDAO actionsDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		luncherWidget = (LuncherPatternView) findViewById(R.id.luncher_widget);
		actionsDao = new ActionsDAO(getApplicationContext());
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
	public void openManagePatterns(View view){
		Intent intent = new Intent(MainActivity.this, ManageActionListActivity.class);
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
		try {
			int typePattern = dao.getTypePatternAssigned(currentSelection, getApplicationContext());
			if (typePattern > 0){
				Toast.makeText(getApplicationContext(), "it is assigned", Toast.LENGTH_SHORT).show();
				if (typePattern == AppshortcutDAO.PREF_TYPE_ACTION){
					ActionVo action = actionsDao.getActionByPattern(currentSelection);
					Intent i = getPatternIntent(action, getPackageManager());
					if (i != null){
						startActivity(i);
					}else{ Toast.makeText(getApplicationContext(), "Exception.. so bad right? ", Toast.LENGTH_SHORT).show(); }
				}
				if (typePattern == AppshortcutDAO.PREF_TYPE_ACTIVITY){
				}
			}else{
				Toast.makeText(getApplicationContext(), "Pattern not assigned", Toast.LENGTH_SHORT).show();
			}
			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Exception.. so bad right? ", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void registerSelection(String currentSelection) {
		// Not Used
	}
	
}
