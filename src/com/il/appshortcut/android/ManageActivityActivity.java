package com.il.appshortcut.android;

import static com.il.appshortcut.converter.ActivitiesConverter.convertActivity2SelectPatternInfoView;
import static com.il.appshortcut.helpers.ApplicationHelper.safeLongToInt;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.il.appshortcut.dao.AppshortcutDAO;
import com.il.appshortcut.helpers.ServicesHelper;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.views.ActivityDetailListVo;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityIconVo;
import com.il.appshortcut.views.ActivityVo;
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
	
	ActivityVo mCurrentActivity;
	
	private List<ServiceVo> mCurrentSelectedServices;
	private List<ApplicationVo> mCurrentApplications;
	private ActivityDetailListVo mActivityDetailListVo;
	private int topOrderServices = 0;
	private int topOrderApplications = 0;
	ServicesHelper servicesHelper;
	private String selectedPattern = "";
	private ActivityIconVo selectedIcon;
	protected ActivityFormFragment formFragment;
	SelectServicesFragment selectServicesFragment;
	private boolean detailsUpdated = false;
	private AppShortcutApplication appState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_activity);
		mCurrentSelectedServices = new ArrayList<ServiceVo>();
		mCurrentApplications = new ArrayList<ApplicationVo>();
		servicesHelper = new ServicesHelper(getApplicationContext());
		activitiesDao = new ActivitiesDAO(getApplicationContext());
		actionsDao = new ActionsDAO(getApplicationContext());
		activitiesDetailsDao = new ActivityDetailsDAO(getApplicationContext());
		appState = ((AppShortcutApplication) getApplicationContext());
		mActivityDetailListVo = new ActivityDetailListVo();
		
		if (findViewById(R.id.fragment_container_activity) != null) {
			try{
				if (savedInstanceState != null) { return; }
				final ActionBar actionBar = getActionBar();
				actionBar.setDisplayHomeAsUpEnabled(true);
				
				formFragment = new ActivityFormFragment();
				getAppContextCurrentActivity();
				
				if (mCurrentActivity != null 
						&& mCurrentActivity.isSavedActivity()){
					
					// get ActivityDetails list with proper object mapping 
					mActivityDetailListVo.data = activitiesDetailsDao
							.getAllActivityDetailsByActivity(
									String.valueOf(mCurrentActivity.getIdActivity()),
									appState.getAllAppsList(),
									getApplicationContext());

					if (mActivityDetailListVo.size() > 0) {
						mCurrentApplications.addAll(mActivityDetailListVo.getListApplications());
						mCurrentSelectedServices.addAll(mActivityDetailListVo.getListServices());
					}
				}else{
					mCurrentActivity = new ActivityVo();
				}
			}catch(Exception e){
				e.printStackTrace();
				Log.d(AppManager.LOG_EXCEPTIONS, "Exception: " + this.getClass()
						+ " method: onCreate");
				Toast.makeText(getApplicationContext(),
						"Error retriving activity", Toast.LENGTH_SHORT).show();
				mCurrentActivity = new ActivityVo();
			}
			mCurrentActivity.setActivityDetailListVo(mActivityDetailListVo);
			formFragment.setmCurrentActivity(mCurrentActivity);
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
	
	public void getAppContextCurrentActivity(){
		mCurrentActivity = appState.getCurrentActivity();
	}
	
	public void castCurrentActivityObject(){
		View formView = findViewById(R.id.fragment_container_activity);
		String textName = "";
		String textDescription = "";
		
		if (formView != null) {
			EditText editTextName = (EditText) formView
					.findViewById(R.id.activityName);
			textName = editTextName.getText().toString();
			EditText editTextDescription = (EditText) formView
					.findViewById(R.id.acticityDescription);
			textDescription = editTextDescription.getText().toString();
		}
		mCurrentActivity.setName(textName);
		mCurrentActivity.setDescription(textDescription);
		if(selectedIcon != null 
				&& selectedIcon.getIdIcon() > 0){
			mCurrentActivity.setIdIcon(selectedIcon.getIdIcon());
		}
		
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		appState.setCurrentActivity(mCurrentActivity);
	}
	
	public void callSaveActivity(View view) {
		try{
			getAppContextCurrentActivity();
			if (mCurrentActivity == null) {
				mCurrentActivity = new ActivityVo();
			}
			castCurrentActivityObject();
			ActivitiesDAO activitiDao = new ActivitiesDAO(getApplicationContext());
			if (selectedPattern != null && selectedPattern != "") {
				mCurrentActivity.setPattern(selectedPattern);
				AppshortcutDAO dao = new AppshortcutDAO();
				dao.savePattern(selectedPattern, getApplicationContext(),
						AppshortcutDAO.TYPE_ACTIVITY);
			}
			
			long idActivity = activitiDao.addUpdateActivity(mCurrentActivity);
			if (detailsUpdated) {
				saveLists(mergeLists(), idActivity);
			}
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), "Error saving activity.",
					Toast.LENGTH_SHORT).show();
		}
		onBackPressed();
	}
	
	
	/**
	 * Add application selected
	 */
	public void openAddApplication(View view){
		castCurrentActivityObject();
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		appState.setAppSelected(null);
		Intent intent = new Intent(ManageActivityActivity.this,
				ManageActionListActivity.class);
		intent.putExtra(AppManager.ACTIVITY_ACTION_PARAM,
				AppManager.ACTIVITY_ACTION_FROM_ACTIVITIES);
		startActivityForResult(intent, 1);
	}
	
	/**
	 * Add services selected
	 */
	public void openAddService(View view) {
		castCurrentActivityObject();
		selectServicesFragment = new SelectServicesFragment();
		getSupportFragmentManager()
				.beginTransaction().replace(R.id.fragment_container_activity, selectServicesFragment).commit();
	}

	/**
	 * update Services selected 
	 */
	public void callSelectServices(View view) {
		getAppContextCurrentActivity();
		detailsUpdated = true;
		mCurrentSelectedServices.clear();
		mCurrentSelectedServices.addAll(selectServicesFragment.getSelections());
		Log.d(AppManager.LOG_ACTIVITIES, "Service: " + mCurrentSelectedServices.size());
		mCurrentActivity.getActivityDetailListVo().data = mergeLists();
		formFragment.setmCurrentActivity(mCurrentActivity);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container_activity, formFragment)
				.commit();
	}
	
	/**
	 * open dialog "Select application "
	 */
	public void callSelectApplication(int result) {
		getAppContextCurrentActivity();
		detailsUpdated = true;
		AppShortcutApplication appState = ((AppShortcutApplication) getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		if (appSelected != null) {
			boolean found = false;
			if (mCurrentApplications.size() > 0) {
				for (ApplicationVo application : mCurrentApplications) {
					if (application.equals(appSelected)) {
						found = true;
					}
				}
			}
			if (!found) {
				mCurrentApplications.add(appSelected);
			}
		}
		mCurrentActivity.getActivityDetailListVo().data = mergeLists();
		formFragment.setmCurrentActivity(mCurrentActivity);
		formFragment.updateArticleView();
	}
	
	/**
	 * merge applications and services into activityDetails
	 */
	public List<ActivityDetailVo> mergeLists(){
		List<ActivityDetailVo> result = new ArrayList<ActivityDetailVo>();
		if(mCurrentSelectedServices != null){
			for (ServiceVo service : mCurrentSelectedServices){
				if (service != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(mCurrentActivity != null){
						detail.setIdActivity(mCurrentActivity.getIdActivity());
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
		
		if (mCurrentApplications != null){
			for (ApplicationVo application : mCurrentApplications){
				if (application != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(mCurrentActivity != null){
						detail.setIdActivity(mCurrentActivity.getIdActivity());
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
	
	/**
	 * if any service || app was updated save new information
	 * this is done discarting (deletting) existing information and saving current state.
	 */
	public void saveLists(List<ActivityDetailVo> listDetails, long activityId){
		try {
			// Delete Actions (applications)  
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
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"An exception has occured saving the information",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			Log.d(AppManager.LOG_EXCEPTIONS, "Exception: " + this.getClass()
					+ " method: saveLists");
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				int result = data.getIntExtra(AppManager.ACTIVITY_ACTION_RESULT_PARAM, 0);
				callSelectApplication(result);
			}
			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"No Application selected", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.il.appshortcut.android.fragments.SelectServicesFragment.SelectServicesListener#refreshList(java.util.List)
	 * this method is called from child to refresh the current Service list
	 */
	@Override
	public void refreshList(List<ServiceVo> list) {
		mCurrentSelectedServices = list;
	}

	@Override
	public void removeService(ActivityDetailVo detail) {
		detailsUpdated = true;
		int i = 0;
		for (ServiceVo item : mCurrentSelectedServices) {
			if (detail.getService() != null
					&& item.equals(detail.getService())) {
				mCurrentSelectedServices.remove(i);
				break;
			}
			i++;
		}
	}

	@Override
	public void removeAction(ActivityDetailVo detail) {
		detailsUpdated = true;
		int i = 0;
		for (ApplicationVo item : mCurrentApplications) {
			if (detail.getApplication() != null
					&& item.equals(detail.getApplication())) {
				mCurrentApplications.remove(i);
				break;
			}
			i++;
		}
	}

	@Override
	public List<ServiceVo> getParentList() {
		return mCurrentSelectedServices;
	}
	
	/**
	 * assign selected pattern to the activity
	 */
	public void assignPattern(){
		if (mCurrentActivity != null){
			mCurrentActivity.setPattern(selectedPattern);
		}
		detailsUpdated = true;
		formFragment.setmCurrentActivity(mCurrentActivity);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container_activity, formFragment)
				.commit();
	}

	@Override
	public void onSomething(String something) {
		// Method not used, used on interface:  com.il.appshortcut.android.fragments.ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener
	}
	@Override
	public void fireApplication(String currentSelection) {
		// Method not used: used on interface: com.il.appshortcut.android.views.LuncherPatternView.LuncherPatternListener
	}

	@Override
	public void registerSelection(String currentSelection) {
		this.selectedPattern = currentSelection;
	}
	
	/**
	 * logic to assign pattern
	 */
	public void assignPattern(View view) {
		if (selectedPattern != null) {
			AppshortcutDAO dao = new AppshortcutDAO();
			try{
				Log.d(AppManager.LOG_MANAGE_APPLICATIONS, "saving applicatoin...");
				if(dao.isPatternAssigned(selectedPattern, getApplicationContext())){
					Toast.makeText(getApplicationContext(),
							"Pattern assigned Show confirmation ",
							Toast.LENGTH_SHORT).show();
					
					new AlertDialog.Builder(this)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Confirmation")
							.setMessage("Bla bla bla.... ")
							.setPositiveButton("Confirm",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											assignPattern();
										}
									}).setNegativeButton("Cancel", null).show();

				} else {
					assignPattern();
				}
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Error Saving Information", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void selectIcon(ActivityIconVo icon) {
		selectedIcon = icon;  
	}
	
	/**
	 * show Dialog for selecting icon.
	 */
	public void selectImage(View view){
		ActivitySelectIconDialogFragment fa = new ActivitySelectIconDialogFragment();
		fa.show(getSupportFragmentManager(), "Select Icon...");
	}

	/* 
	 * assign icon for the activity
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, ActivityIconVo activityIcon) {
		selectedIcon = activityIcon;
		if (formFragment != null){
			formFragment.updateIcon(activityIcon);
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// not used.. if cancel on select icon is pressed nothing to do
	}
	
	public void openPatternPanel(View view){
		if (mCurrentActivity != null){
			castCurrentActivityObject();
			ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
			newFragment.setmCurrentInformation(convertActivity2SelectPatternInfoView(mCurrentActivity, getResources()));
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container_activity, newFragment)
					.commit();
		}else{
			Toast.makeText(getApplicationContext(),
					"Error Activity not assigned plase try again",
					Toast.LENGTH_SHORT).show();
		}
	}
}
