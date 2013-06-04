package com.il.appshortcut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.il.appshortcut.android.views.LuncherPatternView;


public class MainActivity extends Activity implements
		LuncherPatternView.LuncherPatternListener {

	LuncherPatternView luncherWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		luncherWidget = (LuncherPatternView) findViewById(R.id.luncher_widget);
		
		Resources r = getResources(); 
		com.il.appshortcut.helpers.ActionHelper.assignIdPrefFile(r);
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
				.getSharedPreferences(String.valueOf(R.string.idPrefFile),
						Context.MODE_PRIVATE);
		try {
			Intent i = com.il.appshortcut.helpers.ActionHelper.getIntent(currentSelection, sharedPref, getPackageManager());
			if (i != null){
				startActivity(i);
			}else{ Toast.makeText(getApplicationContext(), "Exception.. so bad right? ", Toast.LENGTH_SHORT).show(); }
		} catch (Exception e) { Toast.makeText(getApplicationContext(), "Exception.. so bad right? ", Toast.LENGTH_SHORT).show(); }
	}

	@Override
	public void registerSelection(String currentSelection) {
		// Not Used
	}
	
}
