package com.il.appshortcut.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.il.appshortcut.R;

public class AppShortcutLauncherWidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_app_shortcut_launcher);
			
			views.setOnClickPendingIntent(R.id.widget_up_button,
					buildUpButtonPendingIntent(context));
			
			views.setOnClickPendingIntent(R.id.widget_left_button,
					buildLeftButtonPendingIntent(context));
			
			views.setOnClickPendingIntent(R.id.widget_down_button,
					buildDownButtonPendingIntent(context));
			
			views.setOnClickPendingIntent(R.id.widget_right_button,
					buildRightButtonPendingIntent(context));
			
			views.setOnClickPendingIntent(R.id.clearSelection,
					buildClearButtonPendingIntent(context));
			
			views.setTextViewText(R.id.selected_patter_widget, getDesc(context));
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
			
		}
	}

	public static PendingIntent buildUpButtonPendingIntent(Context context) {
		++AppShortcutLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_UP_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildLeftButtonPendingIntent(Context context) {
		++AppShortcutLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_LEFT_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildDownButtonPendingIntent(Context context) {
		++AppShortcutLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_DOWN_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildRightButtonPendingIntent(Context context) {
		++AppShortcutLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_RIGHT_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildClearButtonPendingIntent(Context context) {
//		Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//		return pendingIntent;
		
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_CLEAR_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
//		
//		SharedPreferences sharedPref = context.getApplicationContext()
//				.getSharedPreferences(AppManager.ID_PRE_FFILE,
//						Context.MODE_PRIVATE);
//		Intent i = null;
//		try{
//			i = getPatternIntent("143",
//					sharedPref, context.getPackageManager());
//		}catch(Exception e){}
//		
//		return PendingIntent.getActivity(context, 0, i, 0);
	}

	private static CharSequence getDesc(Context context) {
		return WidgetUtils.getWidgetSelectionSharedPref(context);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, AppShortcutLauncherWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
