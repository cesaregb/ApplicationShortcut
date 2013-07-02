package com.il.appshortcut;

import static com.il.appshortcut.helpers.ActionHelper.getPatternIntent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.Toast;

import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.widgets.WidgetUtils;

public class ProxyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proxy);

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		String currentSelection = "";

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentSelection = extras
					.getString(WidgetUtils.TAG_CURRENT_SELECTION);
		}
		SharedPreferences sharedPref = getApplicationContext()
				.getSharedPreferences(AppManager.ID_PRE_FFILE,
						Context.MODE_PRIVATE);

		Log.d(AppManager.LOG_WIDGET,
				"into intent and onCreate method with selection of: "
						+ currentSelection);
		try {
			Intent i = getPatternIntent(currentSelection, sharedPref,
					getPackageManager());
			if (i != null) {

				WidgetUtils.clearSharedPref(getApplicationContext());
				Log.d(AppManager.LOG_WIDGET, "Launching applications");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_TASK_ON_HOME
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_FORWARD_RESULT);
				getApplicationContext().startActivity(i);
			}
		} catch (Exception e) {
			Log.d(AppManager.LOG_EXCEPTIONS, e.getMessage());
			Toast.makeText(getApplicationContext(),
					"Exception.. so bad right? ", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proxy, menu);
		return true;
	}

}
