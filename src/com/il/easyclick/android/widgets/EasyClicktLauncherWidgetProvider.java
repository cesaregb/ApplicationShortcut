package com.il.easyclick.android.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.easyclick.R;
public class EasyClicktLauncherWidgetProvider extends AppWidgetProvider {
	
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
		++EasyClickLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_UP_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildLeftButtonPendingIntent(Context context) {
		++EasyClickLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_LEFT_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildDownButtonPendingIntent(Context context) {
		++EasyClickLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_DOWN_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildRightButtonPendingIntent(Context context) {
		++EasyClickLauncherWidgetReceiver.clickCount;
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_RIGHT_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildClearButtonPendingIntent(Context context) {
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_CLEAR_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private static CharSequence getDesc(Context context) {
		try{
			return WidgetUtils.getWidgetSelectionSharedPref(context);
		}catch(Exception e){
			//TODO Add String...
			Toast.makeText(context, "Error retriving saved information", Toast.LENGTH_SHORT).show();
			return "";
		}
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		ComponentName myWidget = new ComponentName(context, EasyClicktLauncherWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
