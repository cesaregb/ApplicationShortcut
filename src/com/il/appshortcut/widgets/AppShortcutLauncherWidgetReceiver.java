package com.il.appshortcut.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.il.appshortcut.R;

public class AppShortcutLauncherWidgetReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean updateWidget = false;
		String currentAction = intent.getAction(); 
		if (currentAction.equals(WidgetUtils.WIDGET_UP_ACTION)) {
			WidgetUtils.updateSharedPref(context, WidgetUtils.WIDGET_UP_VALUE);
			updateWidget = true;
        }
		if (currentAction.equals(WidgetUtils.WIDGET_LEFT_ACTION)) {
			WidgetUtils.updateSharedPref(context, WidgetUtils.WIDGET_LEFT_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_RIGHT_ACTION) ) {
			WidgetUtils.updateSharedPref(context, WidgetUtils.WIDGET_RIGHT_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_DOWN_ACTION)) {
			WidgetUtils.updateSharedPref(context, WidgetUtils.WIDGET_DOWN_VALUE);
			updateWidget = true;
		}
		if (currentAction.equals(WidgetUtils.WIDGET_CLEAR_ACTION)) {
			WidgetUtils.clearSharedPref(context);
			updateWidget = true;
		}
		if (updateWidget){
			updateWidgetPictureAndButtonListener(context);
		}
	}
	
	private void updateWidgetPictureAndButtonListener(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_app_shortcut_launcher);
        remoteViews.setTextViewText(R.id.selected_patter_widget, getDesc(context));
        
        remoteViews.setOnClickPendingIntent(R.id.widget_up_button,
                AppShortcutLauncherWidgetProvider.buildUpButtonPendingIntent(context));
        
        remoteViews.setOnClickPendingIntent(R.id.widget_left_button,
        		AppShortcutLauncherWidgetProvider.buildLeftButtonPendingIntent(context));
        
        remoteViews.setOnClickPendingIntent(R.id.widget_down_button,
        		AppShortcutLauncherWidgetProvider.buildDownButtonPendingIntent(context));
        
        remoteViews.setOnClickPendingIntent(R.id.widget_right_button,
        		AppShortcutLauncherWidgetProvider.buildRightButtonPendingIntent(context));
        
        remoteViews.setOnClickPendingIntent(R.id.clearSelection,
        		AppShortcutLauncherWidgetProvider.buildClearButtonPendingIntent(context));
        
        AppShortcutLauncherWidgetProvider.pushWidgetUpdate(context.getApplicationContext(),
                remoteViews);
    }
 
    private String getDesc(Context context) {
        return WidgetUtils.getWidgetSelectionSharedPref(context);
    }
 
}
