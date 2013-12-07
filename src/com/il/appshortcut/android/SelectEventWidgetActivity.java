package com.il.appshortcut.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.android.adapters.EventItemAdapter;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.helpers.ActivityIconHelper;
import com.il.appshortcut.helpers.WidgetHelper;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.EventIconVo;
import com.il.appshortcut.views.EventWrapper;

public class SelectEventWidgetActivity extends Activity {
	int mAppWidgetId;
	
	private ArrayList<EventWrapper> eventItems = new ArrayList<EventWrapper>();
	private EventItemAdapter aa;
	ListView listView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_event_widget);
		setResult(RESULT_CANCELED);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	     
		listView = (ListView) findViewById( R.id.list_widget_possible_actions);
		
		int resID = R.layout.comp_action_list_item;
		aa = new EventItemAdapter(getApplicationContext(), resID, eventItems);
		listView.setAdapter(aa);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventWrapper event = (EventWrapper) listView
						.getItemAtPosition(position);
				configureWithEvent(event);
			}
		});
		refreshList();
	}

	public void configureWithEvent(EventWrapper event){
		Log.d(AppManager.LOG_WIDGET, "event pattern: " + event.getPatter());
		
		AppManager.getInstance().setShortcutEventWraper(event);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getApplicationContext());
		RemoteViews views = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.widget_app_shortcut);
		
		//set the pendingIntent 
		PendingIntent pendingIntent = WidgetHelper
				.buildLunchEventBtnPendingIntent(getApplicationContext(), event.getPatter());
		views.setOnClickPendingIntent(R.id.option_launch, pendingIntent);
		
		//set the icon 
		Drawable icon = getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher);
		try{
			icon = event.getEventIconVo().getDrawable();
		}catch (Exception e){
			icon = getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher);
		}
		Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
		views.setImageViewBitmap(R.id.option_launch, bitmap);
		
		
		
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
		
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		finish();
	}
	
	public void refreshList(){
		eventItems.clear();
		try{
			ActivitiesDAO activitiesDao =  new ActivitiesDAO(getApplicationContext());
			List<ActivityVo> listActivities = activitiesDao.getAllActivities();
			ActionsDAO actionsDao = new ActionsDAO(getApplicationContext());
			List<ActionVo> listActions = actionsDao.getAllActionsByType(AppshortcutDAO.TYPE_ACTION);
			boolean none = true;
			if (listActivities != null ) {
				none = false;
				for (ActivityVo item : listActivities){
					EventWrapper eventWrapper = new EventWrapper();
					if (item != null) {
						eventWrapper.setEventIconVo(new EventIconVo());
						eventWrapper.getEventIconVo().setName(item.getName());
						Drawable icon = getApplicationContext().getResources().getDrawable(ActivityIconHelper.getDrawableResource(item.getIdIcon()));
						eventWrapper.getEventIconVo().setDrawable(icon);
						eventWrapper.setObject(item);
						eventWrapper.setType(AppshortcutDAO.TYPE_ACTIVITY);
						eventItems.add(eventWrapper);
					}
				}
			}
			
			if (listActions != null ) {
				none = false;
				for (ActionVo item : listActions){
					EventWrapper eventWrapper = new EventWrapper();
					eventWrapper.setEventIconVo(new EventIconVo());
					ApplicationInfo app = getApplicationContext().getPackageManager()
							.getApplicationInfo(item.getParentPackage(),
									0);        
					Drawable icon = getApplicationContext().getPackageManager().getApplicationIcon(app);
					String name = getApplicationContext().getPackageManager().getApplicationLabel(app).toString();
					eventWrapper.getEventIconVo().setName(name);
					eventWrapper.getEventIconVo().setDrawable(icon);
					eventWrapper.setObject(item);
					eventWrapper.setType(AppshortcutDAO.TYPE_ACTION);
					eventItems.add(eventWrapper);
				}
			}
			if (none){
				Toast.makeText(getApplicationContext(), "No Action to show",
						Toast.LENGTH_SHORT).show();
			}
		}catch(Exception e){
			Toast.makeText(getApplicationContext(),
					"Error getting information", Toast.LENGTH_SHORT).show();
		}
		aa.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_event_widget, menu);
		return true;
	}

}
