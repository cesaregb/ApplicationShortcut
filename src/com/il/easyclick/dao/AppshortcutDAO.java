package com.il.easyclick.dao;

import static com.il.easyclick.helpers.ApplicationHelper.safeLongToInt;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.il.easyclick.android.widgets.WidgetUtils;
import com.il.easyclick.config.AppManager;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.exception.AppShortcutException;
import com.il.easyclick.helpers.ActivityIconHelper;
import com.il.easyclick.helpers.ApplicationHelper;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ActivityDetailVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.ApplicationVo;
import com.il.easyclick.views.EventIconVo;
import com.il.easyclick.views.EventWrapper;

public class AppshortcutDAO {
	
	public static final int TYPE_ACTION = 1;
	public static final int TYPE_ACTIVITY = 2;
	
	public static final String SHARED_PREFERENCE_DELIMITER = "--";
	
	/*Shared preferences management */
	public String updateWidgetSelection(Context context, String currentSelection)
			throws AppShortcutException {
		
		String updatedSelection = getWidgetSelection(context) + currentSelection;
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, updatedSelection); // search by application  + action 
		editor.commit();
		return updatedSelection;
	}

	public void clearWidgetSelection(Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, "");  
		editor.commit();
	}

	public String getWidgetSelection(Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		String result = sharedPref.getString(WidgetUtils.WIDGET_CURRENT_SELECTION_PREF_ID, null);
		if (result == null){ result = ""; }
		return result; 
	}

	public void savePattern(String pattern, Context context, int type)
			throws AppShortcutException {
		overridePattern(pattern, context);
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(pattern, type); 
		editor.commit();
	}

	public void removePattern(String pattern, Context context) throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(pattern);
		editor.commit();
	}
	
	public boolean isPatternAssigned(String pattern, Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		int tmpVal = sharedPref.getInt(pattern, 0);
		return (tmpVal > 0);
	}
	
	public int getTypePatternAssigned(String pattern, Context context)
			throws AppShortcutException {
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		
		int tmpVal = sharedPref.getInt(pattern, 0);
		return tmpVal;
	}
	
	public void overridePattern(String pattern, Context context){
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		int tmpVal = sharedPref.getInt(pattern, 0);
		if (tmpVal == AppshortcutDAO.TYPE_ACTION){
			ActionsDAO actionsDao = new ActionsDAO(context);
			actionsDao.removeActionByPattern(pattern);
		}else{
			ActivitiesDAO activityDao = new ActivitiesDAO(context);
			activityDao.removeActivityByPattern(pattern);
		}
	}
	
	public ApplicationVo convertIntent2Applicatoin(Intent intent){
		ApplicationVo result = null;
		if (intent != null){ }
		return result;
	}
	
	public void saveWidgetId(Context context, String idWidget, String action){
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(idWidget, action); 
		editor.commit();
	}
	
	public void removeWidgetId(Context context, String idWidget){
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(idWidget);
		editor.commit();
	}
	/*Shared preferences management */
	
	public void refreshDataDb(Context context){
		ActionsDAO actionsDao = new ActionsDAO(context);
		EasyClickApplication appState = ((EasyClickApplication) context);
		appState.setCurrentDBActions(actionsDao.getAllActionsByType(AppshortcutDAO.TYPE_ACTION));
	}
	
	public EventWrapper getIdActionWidget(Context context, String idWidget){
		EventWrapper eventWrapper = null;
		SharedPreferences sharedPref = context.getSharedPreferences(AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		String savedValue = sharedPref.getString(idWidget, null);
		if (savedValue != null){
			try{
				String[] values = savedValue.split(SHARED_PREFERENCE_DELIMITER);
				if (Integer.valueOf(values[0]) == AppshortcutDAO.TYPE_ACTION){
					eventWrapper = new EventWrapper();
					ActionsDAO actionsDao = new ActionsDAO(context);
					ActionVo action = actionsDao.getActionById(Integer.valueOf(values[1]));
					eventWrapper.setType(AppshortcutDAO.TYPE_ACTION);
					eventWrapper.setObject(action);
					eventWrapper.setEventIconVo(new EventIconVo());
					ApplicationInfo app = context.getPackageManager()
							.getApplicationInfo(action.getParentPackage(),
									0);        
					Drawable icon = context.getPackageManager().getApplicationIcon(app);
					String name = context.getPackageManager().getApplicationLabel(app).toString();
					eventWrapper.getEventIconVo().setName(name);
					eventWrapper.getEventIconVo().setDrawable(icon);
					
				}else if (Integer.valueOf(values[0]) == AppshortcutDAO.TYPE_ACTIVITY){
					eventWrapper = new EventWrapper();
					ActivitiesDAO activityDao = new ActivitiesDAO(context);
					ActivityVo activity = activityDao.getActivityById(Integer.valueOf(values[1]));
					eventWrapper.setEventIconVo(new EventIconVo());
					eventWrapper.getEventIconVo().setName(activity.getName());
					Drawable icon = context.getResources().getDrawable(ActivityIconHelper.getDrawableResource(activity.getIdIcon()));
					eventWrapper.getEventIconVo().setDrawable(icon);
					eventWrapper.setObject(activity);
					eventWrapper.setType(AppshortcutDAO.TYPE_ACTIVITY);
				}
				
			}catch(Exception e){
				
			}
		}
		return eventWrapper;
	}
	
	/*Actions (Applications) management */
	public void saveAction(String selectedPattern, Context context) throws Exception{
		ApplicationVo appSelected = ((EasyClickApplication) context)
				.getAppSelected();
		
		ActionsDAO actionsDao = new ActionsDAO(context);
		savePattern(selectedPattern, context, AppshortcutDAO.TYPE_ACTION);
		appSelected.getCurrentAction().setPattern(selectedPattern);
		appSelected.getCurrentAction().setAssigned(true);
		appSelected.getCurrentAction().setIdAction(
				ApplicationHelper.safeLongToInt(actionsDao
						.addUpdateAction(appSelected.getCurrentAction())));
		refreshDataDb(context);
	}
	
	public void removeAction(ActionVo action, Context context) throws Exception{
		ApplicationVo appSelected = ((EasyClickApplication) context)
				.getAppSelected();
		ActionsDAO actionsDao = new ActionsDAO(context);
		removePattern(action.getPattern(), context);
		actionsDao.removeActionById(String.valueOf(action.getIdAction()));
		appSelected.setCurrentAction(action);
		appSelected.getCurrentAction().setAssigned(false);
		appSelected.getCurrentAction().setIdAction(0);
		refreshDataDb(context);
	}
	/*Actions (Applications) management */
	
	/*Activities  */
	public void saveActivity(Context context, String selectedPattern,
			ActivityVo mCurrentActivity, boolean detailsUpdated,
			List<ActivityDetailVo> activityList) throws Exception{
		
		ActivitiesDAO activitiDao = new ActivitiesDAO(context);
		if (selectedPattern != null && selectedPattern != "") {
			mCurrentActivity.setPattern(selectedPattern);
			AppshortcutDAO dao = new AppshortcutDAO();
			dao.savePattern(selectedPattern, context,
					AppshortcutDAO.TYPE_ACTIVITY);
		}
		
		long idActivity = activitiDao.addUpdateActivity(mCurrentActivity);
		if (detailsUpdated) {
			saveActivityLists(activityList, idActivity, context);
		}
	}
	
	
	public void saveActivityLists(List<ActivityDetailVo> listDetails, long activityId, Context context){
		// Delete Actions (applications)  
		ActivityDetailsDAO activitiesDetailsDao =  new ActivityDetailsDAO(context);
		ActionsDAO actionsDao = new ActionsDAO(context);
		
		List<ActivityDetailVo> lDetails = activitiesDetailsDao
					.getAllActivityDetailsByActivity(String.valueOf(activityId));
		
		if (lDetails != null){
			for (ActivityDetailVo detailItem : lDetails ){
				if (detailItem.getType() == ActivitiesDAO.TYPE_ACTION){
					actionsDao.removeActionById(String.valueOf(detailItem.getIdAction()));
				}
			}
		}
		
		// Delete * from ActivityDetails 
		activitiesDetailsDao
				.removeActivityDetailsByActivity(safeLongToInt(activityId));
		
		for (ActivityDetailVo detailItem : listDetails) {
			// if app save action in actions table. 
			detailItem.setIdActivity(safeLongToInt(activityId));
			if (detailItem.getType() == ActivitiesDAO.TYPE_ACTION) {
				detailItem.getApplication().getCurrentAction()
						.setType(ActivityDetailsDAO.DETAIL_TYPE_ACTIVITY);
				detailItem.getApplication().getCurrentAction().setIdAction(0);
				long idAction = actionsDao.addUpdateAction(detailItem
						.getApplication().getCurrentAction());
				detailItem.setIdAction(safeLongToInt(idAction));
			}
			// add item regardless app or serviece. 
			activitiesDetailsDao.addUpdateActivityDetail(detailItem);
		}
		
		actionsDao = null;
		activitiesDetailsDao = null;
	}
	
	public void removeActivity(ActivityVo activity, Context context, String pattern) throws Exception{
		removePattern(pattern, context);
		
		ActivitiesDAO activitiesDao = new ActivitiesDAO(context);
		activitiesDao.removeActivityByActivity(activity);
		
		ActivityDetailsDAO activityDetailsDao = new ActivityDetailsDAO(context);
		activityDetailsDao.removeActivityDetailsByActivity(activity.getIdActivity());
		
		activitiesDao = null;
		activityDetailsDao = null;
	}
	/*Activities  */
	
	public void removeAll(Context context) throws Exception{
		SharedPreferences sharedPref = context.getSharedPreferences(
				AppManager.ID_PRE_FFILE, Context.MODE_PRIVATE);
		
		sharedPref.edit()
			.clear()
			.commit();
		
		ActionsDAO actionsDao = new ActionsDAO(context);
		actionsDao.removeAll();
		
		ActivitiesDAO activitiesDao = new ActivitiesDAO(context);
		activitiesDao.removeAll();
		
		ActivityDetailsDAO activityDetailsDAO = new ActivityDetailsDAO(context);
		activityDetailsDAO.removeAll();
	}
	
	public void removeActivities(Context context) throws Exception{
		ActivitiesDAO activitiesDao = new ActivitiesDAO(context);
		List<ActivityVo> activities = activitiesDao.getAllActivities();
		for (ActivityVo activity : activities){
			removePattern(activity.getPattern(), context);
		}
		
		activitiesDao.removeAll();
 		ActivityDetailsDAO activityDetailsDAO = new ActivityDetailsDAO(context);
		activityDetailsDAO.removeAll();
		ActionsDAO actionsDao = new ActionsDAO(context);
		actionsDao.removeAllByType(AppshortcutDAO.TYPE_ACTIVITY);
	}
	
	public void removeActions(Context context) throws Exception{
		ActionsDAO actionsDao = new ActionsDAO(context);
		List<ActionVo> actions = actionsDao.getAllActions();
		for (ActionVo action : actions){
			removePattern(action.getPattern(), context);
		}
		actionsDao.removeAllByType(AppshortcutDAO.TYPE_ACTION);
	}
	
	
}
