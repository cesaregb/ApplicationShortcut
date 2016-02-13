package com.il.easyclick.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.il.easyclick.R;
import com.il.easyclick.actions.ActionsFactory;
import com.il.easyclick.android.adapters.EventItemAdapter;
import com.il.easyclick.android.views.Utilities;
import com.il.easyclick.config.AppManager;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.dao.ActionsDAO;
import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.helpers.ActivityIconHelper;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.ApplicationVo;
import com.il.easyclick.views.EventIconVo;
import com.il.easyclick.views.EventWrapper;

public class ViewAllSelectionsActivity extends Activity {
	
	private ArrayList<EventWrapper> eventItems = new ArrayList<EventWrapper>();
	private EventItemAdapter aa;
	ListView listView = null;
	AppshortcutDAO appshortcutDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_selections);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		
		listView = (ListView) findViewById( R.id.list_possible_actions);
		
		int resID = R.layout.comp_action_list_item;
		aa = new EventItemAdapter(getApplicationContext(), resID, eventItems);
		listView.setAdapter(aa);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventWrapper event = (EventWrapper) listView.getItemAtPosition(position);
				
				fireEvent(event);
			}
		});
		appshortcutDao = new AppshortcutDAO();
		refreshList();
		
	}
	
	public void fireEvent(EventWrapper event){
		Log.d(AppManager.LOG_DEBUGGIN, "into fireEvent: " + event.getType());
		
		if(AppshortcutDAO.TYPE_ACTIVITY == event.getType()){
			EasyClickApplication appState = ((EasyClickApplication)getApplicationContext());
			appState.setCurrentActivity(((ActivityVo) event.getObject()));
			
			Intent i = new Intent(ViewAllSelectionsActivity.this, ManageActivityActivity.class);
			startActivity(i);
		}else if(AppshortcutDAO.TYPE_ACTION == event.getType()){
			
			try{
				EasyClickApplication appState = (EasyClickApplication) getApplicationContext();
				ActionVo action = ((ActionVo) event.getObject());
				
				ApplicationInfo info = getApplicationContext().getPackageManager()
						.getApplicationInfo(action.getParentPackage(),
								0);
				PackageManager manager = getPackageManager();
				ApplicationVo item = new ApplicationVo(info.loadLabel(manager).toString());
				item.setIcon(info.loadIcon(manager));
				item.setIcon(Utilities.createIconThumbnail(item.getIcon(), getApplicationContext()));
				item.setComponentName(new ComponentName(info.packageName, action.getClassName()));
				item.setApplicationInfo(info);
				item.setApplicationPackage(info.packageName);
				item.setAssigned(true);
				item.setCommonActions(ActionsFactory.create(item));
				
				appState.setAppSelected(item);
				
				Intent intent = new Intent(ViewAllSelectionsActivity.this,
						ManageActionActivity.class);
				startActivity(intent);
				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Error opening Applicatoin", Toast.LENGTH_SHORT).show();
				//TODO handle exception
			}
			
		}
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
		getMenuInflater().inflate(R.menu.view_all_selections, menu);
		return true;
	}

}
