package com.il.appshortcut.android.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.views.ActionVo;
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
		Intent intent = new Intent();
		intent.setAction(WidgetUtils.WIDGET_CLEAR_ACTION);
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	public static PendingIntent buildLunchApplicationBtnPendingIntent(Context context, String currentSelection) {
		Intent i = null;
		try{
			AppshortcutDAO dao = new AppshortcutDAO();
			ActionsDAO actionsDao = new ActionsDAO(context);
			int typePattern = dao.getTypePatternAssigned(currentSelection,
					context);
			if (typePattern > 0) {
				if (typePattern == AppshortcutDAO.TYPE_ACTION) {
					ActionVo action = actionsDao
							.getActionByPattern(currentSelection);
					i = com.il.appshortcut.helpers.ActionHelper
							.getPatternIntent(action,
									context.getPackageManager());
				}
				if (typePattern == AppshortcutDAO.TYPE_ACTIVITY) {
				}
			}
		} catch (Exception e){}
		return PendingIntent.getActivity(context, 0, i, 0);
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
		ComponentName myWidget = new ComponentName(context, AppShortcutLauncherWidgetProvider.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(myWidget, remoteViews);
	}
}
