package com.il.appshortcut.widgets;

import static com.il.appshortcut.helpers.ActionHelper.getPatternIntent;
import static com.il.appshortcut.helpers.ActionHelper.isPatternAssigned;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.appshortcut.ProxyActivity;
import com.il.appshortcut.R;
import com.il.appshortcut.config.AppManager;

public class AppShortcutLauncherWidgetReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private PendingIntent service = null;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {

		boolean updateWidget = false;
		String currentAction = intent.getAction();
		String currentSelection = null;

		if (currentAction.equals(WidgetUtils.WIDGET_UP_ACTION)) {
			currentSelection = WidgetUtils.updateSharedPref(context,
					WidgetUtils.WIDGET_UP_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_LEFT_ACTION)) {
			currentSelection = WidgetUtils.updateSharedPref(context,
					WidgetUtils.WIDGET_LEFT_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_RIGHT_ACTION)) {
			currentSelection = WidgetUtils.updateSharedPref(context,
					WidgetUtils.WIDGET_RIGHT_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_DOWN_ACTION)) {
			currentSelection = WidgetUtils.updateSharedPref(context,
					WidgetUtils.WIDGET_DOWN_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_CLEAR_ACTION)) {
			Log.d(AppManager.LOG_WIDGET, "Clear Selection");
			WidgetUtils.clearSharedPref(context);
			
			final AlarmManager m = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);

			final Calendar TIME = Calendar.getInstance();
			TIME.set(Calendar.MINUTE, 0);
			TIME.set(Calendar.SECOND, 0);
			TIME.set(Calendar.MILLISECOND, 0);

			final Intent intnt = new Intent(context, ProxyActivity.class);
			intnt.putExtra(WidgetUtils.TAG_CURRENT_SELECTION, "143");

			if (service == null) {
				service = PendingIntent.getActivity(context, 0, intnt, 0);
			}

			m.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 1000,
					service);
			
			updateWidget = true;
		}
		if (updateWidget) {
			try {
				
				SharedPreferences sharedPref = context.getApplicationContext()
						.getSharedPreferences(AppManager.ID_PRE_FFILE,
								Context.MODE_PRIVATE);
				if (currentSelection != null
						&& isPatternAssigned(currentSelection, sharedPref)) {
					
					WidgetUtils
							.clearSharedPref(context.getApplicationContext());
					
					KeyguardManager mKeyguardManager = (KeyguardManager) context
							.getSystemService(Context.KEYGUARD_SERVICE);
					
					if (mKeyguardManager.isKeyguardLocked()) {
						Log.d(AppManager.LOG_WIDGET,
								"Launching Proxy Application ");

						Intent i = new Intent(context,
								ProxyActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra(WidgetUtils.TAG_CURRENT_SELECTION,
								currentSelection);
						context.getApplicationContext().startActivity(i);

					} else {
						Intent i = getPatternIntent(currentSelection,
								sharedPref, context.getPackageManager());
						Log.d(AppManager.LOG_WIDGET, "Launching Application");
						if (i != null) {
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(i);
						}
					}

				}
				updateWidgetPictureAndButtonListener(context);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context.getApplicationContext(),
						"Exception.. so bad right? ", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_app_shortcut_launcher);
		remoteViews.setTextViewText(R.id.selected_patter_widget,
				getDesc(context));

		remoteViews.setOnClickPendingIntent(R.id.widget_up_button,
				AppShortcutLauncherWidgetProvider.buildUpButtonPendingIntent(
						context));

		remoteViews.setOnClickPendingIntent(R.id.widget_left_button,
				AppShortcutLauncherWidgetProvider.buildLeftButtonPendingIntent(
						context));

		remoteViews.setOnClickPendingIntent(R.id.widget_down_button,
				AppShortcutLauncherWidgetProvider.buildDownButtonPendingIntent(
						context));

		remoteViews.setOnClickPendingIntent(R.id.widget_right_button,
				AppShortcutLauncherWidgetProvider
						.buildRightButtonPendingIntent(context));

		remoteViews.setOnClickPendingIntent(R.id.clearSelection,
				AppShortcutLauncherWidgetProvider
						.buildClearButtonPendingIntent(context));

		AppShortcutLauncherWidgetProvider.pushWidgetUpdate(
				context.getApplicationContext(), remoteViews);
	}

	private String getDesc(Context context) {
		return WidgetUtils.getWidgetSelectionSharedPref(context);
	}

}
