package com.il.appshortcut.android.widgets;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.dao.ActivityDetailsDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.helpers.ActivityIconHelper;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityDetailListVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.EventIconVo;
import com.il.appshortcut.views.EventWrapper;

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
				AppManager.getInstance().getListEvents().clear();
				updateWidget = true;
				
			}
		}catch(Exception e){/*TODO add action */}
		
		if (updateWidget) {
			EventWrapper eventWrapper = new EventWrapper();
			EventIconVo eventIconVo = new EventIconVo();
			eventWrapper.setEventIconVo(eventIconVo);
			try {
				KeyguardManager mKeyguardManager = (KeyguardManager) context
							.getSystemService(Context.KEYGUARD_SERVICE);
				
				AppshortcutDAO dao = new AppshortcutDAO();
				ActionsDAO actionsDao = new ActionsDAO(context);
				int typePattern = dao.getTypePatternAssigned(currentSelection, context);
				if (typePattern > 0) {
					if (typePattern == AppshortcutDAO.TYPE_ACTION) {
						ActionVo action = actionsDao
								.getActionByPattern(currentSelection);
						ApplicationInfo app = context.getPackageManager()
								.getApplicationInfo(action.getParentPackage(),
										0);        
				        Drawable icon = context.getPackageManager().getApplicationIcon(app);
				        String name = context.getPackageManager().getApplicationLabel(app).toString();
				        eventWrapper.getEventIconVo().setName(name);
				        eventWrapper.getEventIconVo().setDrawable(icon);
				        eventWrapper.setObject(action);
				        eventWrapper.setType(AppshortcutDAO.TYPE_ACTION);
					}
					if (typePattern == AppshortcutDAO.TYPE_ACTIVITY) {
						ActivitiesDAO activitiesDao = new ActivitiesDAO(context);
						ActivityVo activity = activitiesDao
								.getActivityByPattern(currentSelection);
						if (activity != null) {
							ActivityDetailsDAO activitiesDetailsDao = new ActivityDetailsDAO(
									context);
							ActivityDetailListVo mActivityDetailListVo = new ActivityDetailListVo();
							mActivityDetailListVo.data = activitiesDetailsDao
									.getAllActivityDetailsByActivity(String
											.valueOf(activity.getIdActivity()),
											context);

							activity.setActivityDetailListVo(mActivityDetailListVo);

							eventWrapper.getEventIconVo().setName(activity.getName());
							Drawable icon = context.getResources().getDrawable(ActivityIconHelper.getDrawableResource(activity.getIdIcon()));
							eventWrapper.getEventIconVo().setDrawable(icon);
							eventWrapper.setObject(activity);
							eventWrapper.setType(AppshortcutDAO.TYPE_ACTIVITY);
						}
					}
				}
				
				if (eventWrapper.getType() > 0){
					isMatchFound = true;
					AppManager.getInstance().addEvent(eventWrapper);
//					AppManager.getInstance().getListEvents().add(eventWrapper);
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
			Log.d(AppManager.LOG_WIDGET, "applicatoin List Size: " + AppManager.getInstance().getListEvents().size());
			int i = 0;
			for (EventWrapper event : AppManager.getInstance().getListEvents()){
				if (i == 0){
//					remoteViews.setImageViewResource(R.id.option1, R.drawable.content_new);
					int iconResource = R.drawable.ic_launcher;
					Log.d(AppManager.LOG_DEBUGGIN, "resource as: " + iconResource);
					Drawable icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					try{
						icon = event.getEventIconVo().getDrawable();
					}catch (Exception e){
						icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					}
					Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
					remoteViews.setImageViewBitmap(R.id.option1, bitmap);
					remoteViews.setOnClickPendingIntent(R.id.option1,
							AppShortcutLauncherWidgetProvider
							.buildLunchEventBtnPendingIntent(context, event.getPatter()));
				}
				if (i == 1){
					int iconResource = R.drawable.ic_launcher;
					Log.d(AppManager.LOG_DEBUGGIN, "resource as: " + iconResource);
					Drawable icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					try{
						icon = event.getEventIconVo().getDrawable();
					}catch (Exception e){
						icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					}
					Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
					remoteViews.setImageViewBitmap(R.id.option2, bitmap);
					remoteViews.setOnClickPendingIntent(R.id.option2,
							AppShortcutLauncherWidgetProvider
							.buildLunchEventBtnPendingIntent(context, event.getPatter()));
					
					
//					remoteViews.setInt(R.id.option1, "setImageResource", R.drawable.content_new);
//					remoteViews.setOnClickPendingIntent(R.id.option2,
//							AppShortcutLauncherWidgetProvider
//							.buildLunchEventBtnPendingIntent(context, event.getPatter()));
				}
				if (i == 2){
					int iconResource = R.drawable.ic_launcher;
					Log.d(AppManager.LOG_DEBUGGIN, "resource as: " + iconResource);
					Drawable icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					try{
						icon = event.getEventIconVo().getDrawable();
					}catch (Exception e){
						icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					}
					Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
					remoteViews.setImageViewBitmap(R.id.option3, bitmap);
					remoteViews.setOnClickPendingIntent(R.id.option3,
							AppShortcutLauncherWidgetProvider
							.buildLunchEventBtnPendingIntent(context, event.getPatter()));
					
//					remoteViews.setInt(R.id.option1, "setImageResource", R.drawable.content_new);
//					remoteViews.setOnClickPendingIntent(R.id.option3,
//							AppShortcutLauncherWidgetProvider
//							.buildLunchEventBtnPendingIntent(context, event.getPatter()));
				}
				
				Log.d(AppManager.LOG_WIDGET, i + "; Application: " + event.getType() + " ++ " + event.getPatter());
				i++;
			}
		}else{
			Intent i = new Intent();
			PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.option1, pi);
			remoteViews.setOnClickPendingIntent(R.id.option2, pi);
			remoteViews.setOnClickPendingIntent(R.id.option3, pi);
			remoteViews.setImageViewResource(R.id.option1, R.drawable.action_search);
			remoteViews.setImageViewResource(R.id.option2, R.drawable.action_search);
			remoteViews.setImageViewResource(R.id.option3, R.drawable.action_search);
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
