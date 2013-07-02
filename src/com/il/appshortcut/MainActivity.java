package com.il.appshortcut;

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

import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppManager;


public class MainActivity extends Activity implements
		LuncherPatternView.LuncherPatternListener {

	LuncherPatternView luncherWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		luncherWidget = (LuncherPatternView) findViewById(R.id.luncher_widget);
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
	
	public void openManagePatterns(View view){
		Intent intent = new Intent(MainActivity.this, ManageAppListActivity.class);
		startActivity(intent);
	}

	@Override
	public void fireApplication(String currentSelection) {

		SharedPreferences sharedPref = getApplicationContext()
				.getSharedPreferences(AppManager.ID_PRE_FFILE,
						Context.MODE_PRIVATE);
		try {
			Intent i = com.il.appshortcut.helpers.ActionHelper.getPatternIntent(currentSelection, sharedPref, getPackageManager());
			if (i != null){
				startActivity(i);
			}else{ Toast.makeText(getApplicationContext(), "Exception.. so bad right? ", Toast.LENGTH_SHORT).show(); }
		} catch (Exception e) {
			
			Toast.makeText(getApplicationContext(), "Exception.. so bad right? ", Toast.LENGTH_SHORT).show(); 
		}
	}

	@Override
	public void registerSelection(String currentSelection) {
		// Not Used
	}
	
}
