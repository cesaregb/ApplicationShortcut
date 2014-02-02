package com.il.easyclick.android;

import static com.il.easyclick.converter.ActivitiesConverter.convertActivity2SelectPatternInfoView;

import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.il.easyclick.R;
import com.il.easyclick.android.fragments.ActivityFormFragment;
import com.il.easyclick.android.fragments.ActivitySelectIconDialogFragment;
import com.il.easyclick.android.fragments.ActivitySelectServicesDialogFragment;
import com.il.easyclick.android.fragments.ApplicationSelectPatternFragment;
import com.il.easyclick.android.views.LuncherPatternView;
import com.il.easyclick.config.AppManager;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.dao.ActionsDAO;
import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.dao.ActivityDetailsDAO;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.helpers.ServicesHelper;
import com.il.easyclick.services.ServiceVo;
import com.il.easyclick.views.ActivityDetailListVo;
import com.il.easyclick.views.ActivityDetailVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.ApplicationVo;
import com.il.easyclick.views.EventIconVo;

public class ManageActivityActivity extends FragmentActivity implements
		ActivityFormFragment.ActivityFormListener,
		ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener,
		LuncherPatternView.LuncherPatternListener, 
		ActivitySelectIconDialogFragment.ActivitySelectIconDialogListener,
		ActivitySelectServicesDialogFragment.ActivitySelectServicesDialogListener{
	
	ActivitiesDAO mActivitiesDao;
	ActionsDAO mActionsDao;
	ActivityDetailsDAO mActivitiesDetailsDao;
	
	ActivityVo mCurrentActivity;
	
	private int topOrderApplications = 0;
	ServicesHelper servicesHelper;
	private String selectedPattern = "";
	private EventIconVo selectedIcon;
	protected ActivityFormFragment formFragment;
	private boolean detailsUpdated = false;
	private EasyClickApplication mAppState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_activity);
		servicesHelper = new ServicesHelper(getApplicationContext());
		mActivitiesDao = new ActivitiesDAO(getApplicationContext());
		mActionsDao = new ActionsDAO(getApplicationContext());
		mActivitiesDetailsDao = new ActivityDetailsDAO(getApplicationContext());
		mAppState = ((EasyClickApplication) getApplicationContext());
		ActivityDetailListVo mActivityDetailListVo = new ActivityDetailListVo();
		
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
					mActivityDetailListVo.data = mActivitiesDetailsDao
							.getAllActivityDetailsByActivity(
									String.valueOf(mCurrentActivity.getIdActivity()),
									mAppState.getAllAppsList(),
									getApplicationContext());
				}else{
					mCurrentActivity = new ActivityVo();
				}
			}catch(Exception e){
				Log.d(AppManager.LOG_DEBUGGIN, "Exception: " + this.getClass()
						+ " method: onCreate");
				
				Toast.makeText(getApplicationContext(),
						"Error retriving activity", Toast.LENGTH_SHORT).show();
				
				mCurrentActivity = new ActivityVo();
			}
			//if not initial values create an empty list.
			mActivityDetailListVo = (mActivityDetailListVo == null)? new ActivityDetailListVo() : mActivityDetailListVo;
			mCurrentActivity.setActivityDetailListVo(mActivityDetailListVo);
			formFragment.setmCurrentActivity(mCurrentActivity);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container_activity, formFragment)
					.commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent upIntent = new Intent(this, ManageActivityActivity.class);
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
					TaskStackBuilder.create(this).addNextIntent(upIntent)
							.startActivities();
					finish();
				} else {
					NavUtils.navigateUpTo(this, upIntent);
				}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manage_activity, menu);
		return true;
	}
	
	public void getAppContextCurrentActivity(){
		mCurrentActivity = mAppState.getCurrentActivity();
	}
	
	public void setAppContextCurrentActivity(){
		mAppState.setCurrentActivity( mCurrentActivity);
	}
	
	public void castCurrentActivityObject(){
		if (mCurrentActivity == null) {
			mCurrentActivity = new ActivityVo();
		}
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
	}
	
	public void callSaveActivity(View view) {
		try{
			castCurrentActivityObject();
			AppshortcutDAO dao = new AppshortcutDAO();
			dao.saveActivity(getApplicationContext(), selectedPattern,
					mCurrentActivity, detailsUpdated);
			
			onBackPressed();
//			Intent intent = new Intent(MainActivity.this, ManageAcivitiesListActivity.class);
//			startActivity(intent);
		}catch (Exception e){
			Toast.makeText(getApplicationContext(), "Error saving activity.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Add application selected
	 */
	public int addActivityDtlType;
	
	public class SelectActivityDetailDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Detail Type")
	        		.setSingleChoiceItems(R.array.dialog_options_activity_type,
						0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface paramDialogInterface, int which) {
								addActivityDtlType = which;
							}
						})
	               .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   fireOpenAddDetail();
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // do nothing just hide
	                   }
	               });
	        return builder.create();
	    }
	}
	
	public void fireOpenAddDetail(){
		if (addActivityDtlType == 0){
			openAddApplication();
		}else{
			openAddService();
		}
	}
	
	public void openAddActivityDetail(View view){
		//set the current activity to the context... 
		setAppContextCurrentActivity();
		SelectActivityDetailDialogFragment fa = new SelectActivityDetailDialogFragment();
		fa.show(getSupportFragmentManager(), "Detail Type");
	}
	
	public void openAddApplication(){
		castCurrentActivityObject();
		EasyClickApplication appState = ((EasyClickApplication) getApplicationContext());
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
	public void openAddService() {
		castCurrentActivityObject();
		ActivitySelectServicesDialogFragment fa = new ActivitySelectServicesDialogFragment();
		fa.setSelected(mCurrentActivity.getActivityDetailListVo().getListServices());
		fa.show(getSupportFragmentManager(), "Select Services");
	}
	
	@Override
	public void onServicesDialogPositiveClick(DialogFragment dialog,
			List<ServiceVo> list) {
		//get the current activity from context, because we come from another activity 
		getAppContextCurrentActivity();
		//save on exit.. 
		detailsUpdated = true; 
		mergeLists(list, null);
		formFragment.setmCurrentActivity(mCurrentActivity);
		formFragment.updateServiceFormView();
	}
	
	
	/**
	 * open dialog "Select application "
	 */
	public void callSelectApplication(int result) {
		getAppContextCurrentActivity();
		detailsUpdated = true;
		EasyClickApplication appState = ((EasyClickApplication) getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		if (appSelected != null) {
			boolean found = false;
			List<ApplicationVo> listApps = mCurrentActivity.getActivityDetailListVo().getListApplications();
			if (listApps != null && listApps.size() > 0) {
				for (ApplicationVo application : listApps) {
					if (application.equals(appSelected)) {
						found = true;
					}
				}
			}
			listApps = null;
			if (!found) {
				//add detail as application instead of calling merge.
				ActivityDetailVo detail = new ActivityDetailVo();
				if(mCurrentActivity != null){
					detail.setIdActivity(mCurrentActivity.getIdActivity());
				}
				detail.setType(ActivitiesDAO.TYPE_ACTION);
				detail.setIcon(appSelected.getIcon());
				detail.setApplication(appSelected);
				detail.setOrder(topOrderApplications++);
				mCurrentActivity.getActivityDetailListVo().addDetail(detail);
			}
		}
		//update fragment with new application.
		formFragment.setmCurrentActivity(mCurrentActivity);
		formFragment.updateServiceFormView();
	}
	
	/**
	 * merge applications and services into activityDetails
	 */
	private void mergeLists(List<ServiceVo> services, List<ApplicationVo> applications){
		if(services != null){
			mCurrentActivity.getActivityDetailListVo().removeServices();
			for (ServiceVo service : services){
				if (service != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(mCurrentActivity != null){
						detail.setIdActivity(mCurrentActivity.getIdActivity());
					}
					detail.setIdAction(service.ID);
					detail.setType(ActivitiesDAO.TYPE_SERVICE);
					detail.setIcon(service.getIcon());
					detail.setService(service);
					detail.setOrder(0);
					mCurrentActivity.getActivityDetailListVo().addDetail(detail);
				}
			}
		}
		if (applications != null){
			mCurrentActivity.getActivityDetailListVo().removeApplications();
			for (ApplicationVo application : applications){
				if (application != null){
					ActivityDetailVo detail = new ActivityDetailVo();
					if(mCurrentActivity != null){
						detail.setIdActivity(mCurrentActivity.getIdActivity());
					}
					detail.setType(ActivitiesDAO.TYPE_ACTION);
					detail.setIcon(application.getIcon());
					detail.setApplication(application);
					detail.setOrder(topOrderApplications++);
					mCurrentActivity.getActivityDetailListVo().addDetail(detail);
				}
			}
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


	@Override
	public void removeActivityDetail(ActivityDetailVo detail) {
		detailsUpdated = true;
		//iterate thru the apps so it has a O(n) 
		for (int i = 0; 
				i < mCurrentActivity.getActivityDetailListVo().data.size(); 
				i++){
			if (detail.getType() == mCurrentActivity
					.getActivityDetailListVo().data.get(i).getType()) {
				
				if (detail.getType() == ActivitiesDAO.TYPE_ACTION){
					
					ApplicationVo item = mCurrentActivity.getActivityDetailListVo().data.get(i).getApplication();
					if (detail.getApplication() != null
							&& item.equals(detail.getApplication())) {
						mCurrentActivity.getActivityDetailListVo().data.remove(i);
					}
				}else{
					
					ServiceVo item = mCurrentActivity.getActivityDetailListVo().data.get(i).getService();
					if (detail.getService() != null
							&& item.equals(detail.getService())) {
						mCurrentActivity.getActivityDetailListVo().data.remove(i);
					}
				}
			}
		}
	}


	/** 
	 * * assign selected pattern to the activity  
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
	public void onSomething(String something) { }
	
	@Override
	public void fireApplication(String currentSelection) { }

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
				if(dao.isPatternAssigned(selectedPattern, getApplicationContext())){
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
	public void selectIcon(EventIconVo icon) {
		selectedIcon = icon;  
	}
	
	/**
	 * show Dialog for selecting icon.
	 */
	public void openSelectImage(View view){
		ActivitySelectIconDialogFragment fa = new ActivitySelectIconDialogFragment();
		fa.show(getSupportFragmentManager(), "Select Activity Icon...");
	}

	/* 
	 * assign icon for the activity
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, EventIconVo activityIcon) {
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
					.addToBackStack("something")
					.commit();
		}else{
			Toast.makeText(getApplicationContext(),
					"Error Activity not assigned plase try again",
					Toast.LENGTH_SHORT).show();
		}
	}

	/* 
	 * These methods are for select service 
	 */
	@Override
	public List<ActivityDetailVo> getDetailItemsArray() {
		return mCurrentActivity.getActivityDetailListVo().data;
	}

}
