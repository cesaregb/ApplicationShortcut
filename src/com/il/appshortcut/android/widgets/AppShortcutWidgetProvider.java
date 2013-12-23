package com.il.appshortcut.android.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;

import com.il.appshortcut.R;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.helpers.WidgetHelper;
import com.il.appshortcut.views.EventWrapper;

public class AppShortcutWidgetProvider extends AppWidgetProvider {
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			Log.d(AppManager.LOG_WIDGET, "updated from single luncher:" + appWidgetId);
			AppshortcutDAO appShortcutDao = new AppshortcutDAO();
			EventWrapper event = appShortcutDao.getIdActionWidget(context, String.valueOf(appWidgetId));
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_app_shortcut);
			if (event != null ){
				PendingIntent pendingIntent = WidgetHelper
						.buildLunchEventBtnPendingIntent(context, event.getPatter());
				views.setOnClickPendingIntent(R.id.option_launch, pendingIntent);
				Drawable icon = context.getResources().getDrawable(R.drawable.ic_launcher);
				try{
					icon = event.getEventIconVo().getDrawable();
				}catch (Exception e){
					icon = context.getResources().getDrawable(R.drawable.ic_launcher);
				}
				Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
				views.setImageViewBitmap(R.id.option_launch, bitmap);
			}
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
//		String action = intent.getAction();
//		if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
//			Bundle extras = intent.getExtras();
//			if (extras != null
//					&& extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
//				final int appWidgetId = extras
//						.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
//				this.onDeleted(context, new int[] { appWidgetId });
//			}
//		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		AppshortcutDAO appShortcutDao = new AppshortcutDAO();
		for (int id : appWidgetIds){
			Log.d(AppManager.LOG_WIDGET, "onDeleted: " + id);
			appShortcutDao.removeWidgetId(context, String.valueOf(id));
		}
		
	}

}
