package com.il.appshortcut.android.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.il.appshortcut.R;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.helpers.WidgetHelper;
import com.il.appshortcut.views.EventWrapper;

public class AppShortcutWidgetProvider extends AppWidgetProvider {
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			Log.d(AppManager.LOG_WIDGET, "updated from single luncher.");
			int appWidgetId = appWidgetIds[i];
			
			EventWrapper event = AppManager.getInstance()
					.getShortcutEventWraper();
			
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_app_shortcut);
			
			if (event != null ){
				PendingIntent pendingIntent = WidgetHelper
						.buildLunchEventBtnPendingIntent(context, event.getPatter());
				
				views.setOnClickPendingIntent(R.id.option_launch, pendingIntent);
			}
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}

	}
	
	
}
