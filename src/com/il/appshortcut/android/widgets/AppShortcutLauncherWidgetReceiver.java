package com.il.appshortcut.android.widgets;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ApplicationVo;

public class AppShortcutLauncherWidgetReceiver extends BroadcastReceiver {
	public static int clickCount = 0;
	private boolean isMatchFound = false;
	private String currentSelection;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		boolean updateWidget = false;
		String currentAction = intent.getAction();
		try{
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
				isMatchFound = false;
				AppManager.getInstance().getListApplications().clear();
				updateWidget = true;
			}
			
		}catch(Exception e){/*TODO add action */}
		
		if (updateWidget) {
			try {
				KeyguardManager mKeyguardManager = (KeyguardManager) context
							.getSystemService(Context.KEYGUARD_SERVICE);
				
				String applicationName = null;
				AppshortcutDAO dao = new AppshortcutDAO();
				ActionsDAO actionsDao = new ActionsDAO(context);
				int typePattern = dao.getTypePatternAssigned(currentSelection,
						context);
				if (typePattern > 0) {
					//TODO assign more information as icon and stuff!!! 
					if (typePattern == AppshortcutDAO.PREF_TYPE_ACTION) {
						ActionVo action = actionsDao
								.getActionByPattern(currentSelection);
						applicationName = action.getActionName();
						
					}
					if (typePattern == AppshortcutDAO.PREF_TYPE_ACTIVITY) {
					}
				}
				
				
				if (applicationName != null){
					isMatchFound = true;
					ApplicationVo application = new ApplicationVo(applicationName);
					application.setPatter(currentSelection);
					AppManager.getInstance().getListApplications().add(application);
				}
				
				if (mKeyguardManager.isKeyguardLocked()) { } else { }
				
				updateWidgetPictureAndButtonListener(context);
			} catch (Exception e) {
				//TODO Add String...
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
		
		if( isMatchFound ){
			currentSelection = (currentSelection != null)?currentSelection:"";
			Log.d(AppManager.LOG_WIDGET, "applicatoin List Size: " + AppManager.getInstance().getListApplications().size());
			int i = 0;
			for (ApplicationVo app : AppManager.getInstance().getListApplications()){
				if (i == 0){
					remoteViews.setOnClickPendingIntent(R.id.option1,
							AppShortcutLauncherWidgetProvider
							.buildLunchApplicationBtnPendingIntent(context, app.getPatter()));
				}
				if (i == 1){
					remoteViews.setOnClickPendingIntent(R.id.option2,
							AppShortcutLauncherWidgetProvider
							.buildLunchApplicationBtnPendingIntent(context, app.getPatter()));
				}
				if (i == 2){
					remoteViews.setOnClickPendingIntent(R.id.option3,
							AppShortcutLauncherWidgetProvider
							.buildLunchApplicationBtnPendingIntent(context, app.getPatter()));
				}
				
				Log.d(AppManager.LOG_WIDGET, i + "; Application: " + app.getName() + " ++ " + app.getPatter());
				i++;
			}
		}else{
			Intent i = new Intent();
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.option1,pi);
			remoteViews.setOnClickPendingIntent(R.id.option2,pi);
			remoteViews.setOnClickPendingIntent(R.id.option3,pi);
		}

		AppShortcutLauncherWidgetProvider.pushWidgetUpdate(
				context.getApplicationContext(), remoteViews);
		
	}

	private String getDesc(Context context) {
		try{
			return WidgetUtils.getWidgetSelectionSharedPref(context);
		}catch(Exception e){
			//TODO Add String...
			Toast.makeText(context, "Erro retriving saved selection", Toast.LENGTH_SHORT).show();
			return "";
		}
	}
	

}
