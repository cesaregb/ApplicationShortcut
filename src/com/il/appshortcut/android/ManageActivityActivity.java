package com.il.appshortcut.android;

import static com.il.appshortcut.converter.ActivitiesConverter.convertActivity2SelectPatternInfoView;
import static com.il.appshortcut.helpers.ApplicationHelper.safeLongToInt;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.il.appshortcut.R;
import com.il.appshortcut.android.fragments.ActivityFormFragment;
import com.il.appshortcut.android.fragments.ActivitySelectIconDialogFragment;
import com.il.appshortcut.android.fragments.ApplicationSelectPatternFragment;
import com.il.appshortcut.android.fragments.SelectServicesFragment;
import com.il.appshortcut.android.views.LuncherPatternView;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.ActionsDAO;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.dao.ActivityDetailsDAO;
import com.il.appshortcut.helpers.ServicesHelper;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.views.ActionVo;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityIconVo;
import com.il.appshortcut.views.ActivityVo;
import com.il.appshortcut.views.AllAppsList;
import com.il.appshortcut.views.ApplicationVo;

public class ManageActivityActivity extends FragmentActivity implements
		ActivityFormFragment.ActivityFormListener,
		SelectServicesFragment.SelectServicesListener,
		ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener,
		LuncherPatternView.LuncherPatternListener, 
		ActivitySelectIconDialogFragment.ActivitySelectIconDialogListener{
	
	ActivitiesDAO activitiesDao;
	ActionsDAO actionsDao;
	ActivityDetailsDAO activitiesDetailsDao;
	
	ActivityVo activitySelected;
	
	private List<ServiceVo> selectedServices;
	private List<ApplicationVo> selectedActions;
	private int topOrderServices = 0;
	private int topOrderApplications = 0;
	ServicesHelper servicesHelper;
	private String selectedPattern = "";
	private ActivityIconVo selectedIcon;
	protected ActivityFormFragment formFragment;
	SelectServicesFragment selectServicesFragment;
	
	private boolean detailsUpdated = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_activity);
		
		selectedServices = new ArrayList<ServiceVo>();
		selectedActions = new ArrayList<ApplicationVo>();
		
		servicesHelper = new ServicesHelper(getApplicationContext());
		
		activitiesDao = new ActivitiesDAO(getApplicationContext());
		actionsDao = new ActionsDAO(getApplicationContext());
		activitiesDetailsDao = new ActivityDetailsDAO(getApplicationContext());
		
		if (findViewById(R.id.fragment_container_activity) != null) {
			if (savedInstanceState != null) { return; }
			
			final ActionBar actionBar = getActionBar();
			
			actionBar.setDisplayHomeAsUpEnabled(true);
			formFragment = new ActivityFormFragment();
			
			AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
			activitySelected = appState.getCurrentActivity();
			
			if (activitySelected != null 
					&& activitySelected.getIdActivity() > 0){
				
				List<ActivityDetailVo> listDetails = activitiesDetailsDao
						.getAllActivityDetailsByActivity(String.valueOf(activitySelected.getIdActivity()));
				
				AllAppsList allAppsList = appState.getAllAppsList();
				
				if (listDetails != null && listDetails.size() > 0) {
					for (ActivityDetailVo item : listDetails) {
						if (item.getType() == ActivitiesDAO.TYPE_ACTION) {
							ActionVo action = actionsDao.getActionById(item
									.getIdAction());
							for (ApplicationVo application : allAppsList.data) {
								boolean addItem = false;
								if (application.getComponentName().getClassName() != null) {
									addItem = application.getComponentName().getClassName()
											.equalsIgnoreCase(action.getClassName());
								} else {
									addItem = application
											.getApplicationPackage()
											.equalsIgnoreCase(action.getActionPackage());
								}
								if (addItem) {
									selectedActions.add(application);
									item.setApplication(application);
								}
							}
						} else {
							ServiceVo service = servicesHelper.getServiceById(item
									.getIdAction());
							selectedServices.add(service);
							item.setService(service);
						}
						
					}
				}
			}
			formFragment.setActivitiesDetails(mergeLists());

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container_activity, formFragment)
					.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_activity, menu);
		return true;
	}

	public void callSaveActivity(View view) {
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		activitySelected = appState.getCurrentActivity();
		if (activitySelected == null) {
			activitySelected = new ActivityVo();
		}
		ActivitiesDAO activitiDao = new ActivitiesDAO(getApplicationContext());
		String textName = "";
		String textDescription = "";
		View formView = findViewById(R.id.fragment_container_activity);
		if (formView != null) {
			EditText editTextName = (EditText) formView
					.findViewById(R.id.activityName);
			textName = editTextName.getText().toString();
			EditText editTextDescription = (EditText) formView
					.findViewById(R.id.acticityDescription);
			textDescription = editTextDescription.getText().toString();
		}
		activitySelected.setName(textName);
		activitySelected.setDescription(textDescription);
		if (selectedPattern != null && selectedPattern != ""){
			activitySelected.setPattern(selectedPattern);
		}
		if(selectedIcon != null 
				&& selectedIcon.getIdIcon() > 0){
			activitySelected.setIdIcon(selectedIcon.getIdIcon());
		}
		long idActivity = activitiDao.addUpdateActivity(activitySelected);
		if (detailsUpdated) {
			saveLists(mergeLists(), idActivity);
		}
		onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * show app list for selection 
	 * @param view
	 */
	public void addApplication(View view){
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		appState.setAppSelected(null);

		Intent intent = new Intent(ManageActivityActivity.this,
				ManageActionListActivity.class);
		intent.putExtra(AppManager.ACTIVITY_ACTION_PARAM,
				AppManager.ACTIVITY_ACTION_FROM_ACTIVITIES);
		startActivityForResult(intent, 1);
	}
	
	/**
	 * thos service list for selection
	 * @param view
	 */
	public void addService(View view) {
		selectServicesFragment = new SelectServicesFragment();
		getSupportFragmentManager()
				.beginTransaction().replace(R.id.fragment_container_activity, selectServicesFragment).commit();
	}

	public void callSelectServices(View view) {
		detailsUpdated = true;
		
		selectedServices.clear();
		selectedServices.addAll(selectServicesFragment.getSelections());
		
		Log.d(AppManager.LOG_ACTIVITIES, "Activity size: " + selectedServices.size());
		
		formFragment.setActivitiesDetails(mergeLists());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container_activity, formFragment)
				.commit();
	}
	
	public void callSelectApplication(int result) {
		detailsUpdated = true;
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		if (appSelected != null) {
			boolean found = false;
			if (selectedActions.size() > 0) {
				for (ApplicationVo app : selectedActions) {
					if (app.equals(appSelected)) {
						found = true;
					}
				}
			}
			if (!found) {
				selectedActions.add(appSelected);
			}
		}
		formFragment.setActivitiesDetails(mergeLists());
	}
	
	public List<ActivityDetailVo> mergeLists(){
		List<ActivityDetailVo> result = new ArrayList<ActivityDetailVo>();
		
		if(selectedServices != null){
			for (ServiceVo service : selectedServices){
				if (service != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(activitySelected != null){
						detail.setIdActivity(activitySelected.getIdActivity());
					}
					detail.setIdAction(service.ID);
					detail.setType(ActivitiesDAO.TYPE_SERVICE);
					detail.setIcon(service.getIcon());
					detail.setService(service);
					detail.setOrder(topOrderServices++);
					result.add(detail);
				}
			}
		}
		
		if (selectedActions != null){
			for (ApplicationVo application : selectedActions){
				if (application != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(activitySelected != null){
						detail.setIdActivity(activitySelected.getIdActivity());
					}
					detail.setType(ActivitiesDAO.TYPE_ACTION);
					detail.setIcon(application.getIcon());
					detail.setApplication(application);
					detail.setOrder(topOrderApplications++);
					result.add(detail);
				}
			}
		}
		
		return result;
	}
	
	public void saveLists(List<ActivityDetailVo> listDetails, long activityId){
		try {
			// REMOVE INFORMATION 
			List<ActivityDetailVo> lDetails = activitiesDetailsDao
						.getAllActivityDetailsByActivity(String.valueOf(activityId));
			if (lDetails != null){
				for (ActivityDetailVo detailItem : lDetails ){
					if (detailItem.getType() == ActivitiesDAO.TYPE_ACTION){
						actionsDao.removeActionById(String.valueOf(detailItem.getIdAction()));
					}
				}
			}
			
			activitiesDetailsDao
					.removeActivityDetailsByActivity(safeLongToInt(activityId));
			
			for (ActivityDetailVo detailItem : listDetails) {
				detailItem.setIdActivity(safeLongToInt(activityId));
				if (detailItem.getType() == ActivitiesDAO.TYPE_ACTION) {
					detailItem.getApplication().getCurrentAction()
							.setType(ActivityDetailsDAO.DETAIL_TYPE_ACTIVITY);
					long idAction = actionsDao.addUpdateAction(detailItem
							.getApplication().getCurrentAction());
					detailItem.setIdAction(safeLongToInt(idAction));
				}
				activitiesDetailsDao.addUpdateActivityDetail(detailItem);
			}
		} catch (Exception e) {
			// TODO exception handle...
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				int result = data.getIntExtra(AppManager.ACTIVITY_ACTION_RESULT_PARAM, 0);
				callSelectApplication(result);
			}
			if (resultCode == RESULT_CANCELED) {
				/*TODO no application selected!!*/  
			}
		}
	}

	@Override
	public void refreshList(List<ServiceVo> list) {
		selectedServices = list;
	}

	@Override
	public void removeService(ActivityDetailVo detail) {
		detailsUpdated = true;
		int i = 0;
		for (ServiceVo item : selectedServices) {
			if (detail.getService() != null
					&& item.equals(detail.getService())) {
				selectedServices.remove(i);
				break;
			}
			i++;
		}
	}

	@Override
	public void removeAction(ActivityDetailVo detail) {
		detailsUpdated = true;
		int i = 0;
		for (ApplicationVo item : selectedActions) {
			if (detail.getApplication() != null
					&& item.equals(detail.getApplication())) {
				selectedActions.remove(i);
				break;
			}
			i++;
		}
	}

	@Override
	public List<ServiceVo> getParentList() {
		return selectedServices;
	}
	
	public void assignPattern(View view){
		if (activitySelected != null){
			ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
			newFragment.setmCurrentInformation(convertActivity2SelectPatternInfoView(activitySelected, getResources()));
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container_activity, newFragment)
					.commit();
		}else{
			//TODO Handle expception.
		}
	}

	@Override
	public void onSomething(String something) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fireApplication(String currentSelection) {
		// Not used
	}

	@Override
	public void registerSelection(String currentSelection) {
		this.selectedPattern = currentSelection;
		
	}

	@Override
	public void selectIcon(ActivityIconVo icon) {
		selectedIcon = icon;  
	}
	
	public void selectImage(View view){
		ActivitySelectIconDialogFragment fa = new ActivitySelectIconDialogFragment();
		fa.show(getSupportFragmentManager(), "Select Icon...");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, ActivityIconVo activityIcon) {
		Toast.makeText(getApplicationContext(), "Selected: " + activityIcon.getIdIcon(), Toast.LENGTH_SHORT).show();
		if (formFragment != null){
			formFragment.updateIcon(activityIcon);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
}
