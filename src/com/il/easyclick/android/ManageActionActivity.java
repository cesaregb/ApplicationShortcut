package com.il.easyclick.android;

import static com.il.easyclick.converter.ActionsConverter.convertApplication2SelectPatternInfoView;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.il.easyclick.R;
import com.il.easyclick.android.fragments.ApplicationInfoFragment;
import com.il.easyclick.android.fragments.ApplicationSelectPatternFragment;
import com.il.easyclick.android.views.LuncherPatternView;
import com.il.easyclick.config.AppManager;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ApplicationVo;

public class ManageActionActivity extends FragmentActivity
		implements
		ApplicationInfoFragment.ApplicationInfoListener,
		ApplicationSelectPatternFragment.ApplicationSelectPatternFragmentListener,
		LuncherPatternView.LuncherPatternListener {
	
	private String selectedPattern = "";
	protected Object mActionMode;
	private ActionVo mActionLongOver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_application);
		
		if (findViewById(R.id.fragment_container_application) != null) {
			if (savedInstanceState != null) {
				return;
			}
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			ApplicationInfoFragment newFragment = new ApplicationInfoFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container_application, newFragment)
					.commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, ManageActionListActivity.class);
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manage_application, menu);
		return true;
	}

	@Override
	public void onApplicationActionItem(ActionVo item) {
		if (mActionMode != null) {((ActionMode) mActionMode).finish();}
		EasyClickApplication appState = ((EasyClickApplication)getApplicationContext());
		ApplicationVo appSelected = appState.getAppSelected();
		int getTypeResponse = appState.getTypeSelectAppReturn();
		//regardless action set the common information for this type of applications.
		item.setParentPackage(appSelected.getApplicationPackage());
		item.setType(AppshortcutDAO.TYPE_ACTION); // this is override if activity calls it
		if (item.getActionPackage() == null) {
			item.setActionPackage(appSelected.getApplicationPackage());
		}
		appSelected.setCurrentAction(item);
		appState.setAppSelected(appSelected);
		
		if (getTypeResponse == AppManager.ACTIVITY_ACTION_FROM_MAIN) {
			ApplicationSelectPatternFragment newFragment = new ApplicationSelectPatternFragment();
			newFragment.setmCurrentInformation(convertApplication2SelectPatternInfoView(appSelected, getApplicationContext()));
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction
					.replace(R.id.fragment_container_application, newFragment)
					.addToBackStack(null).commit();
		} else {
			Intent returnIntent = new Intent();
			try{
				returnIntent.putExtra(AppManager.ACTIVITY_ACTION_RESULT_PARAM, 1);
				if (getParent() == null) {
					setResult(RESULT_OK, returnIntent);
				} else {
					getParent().setResult(RESULT_OK, returnIntent);
				}
				
			}catch(Exception e){
				if (getParent() == null) {
					setResult(RESULT_CANCELED, returnIntent);
				} else {
					getParent().setResult(RESULT_CANCELED, returnIntent);
				}
				
			}finally{
				finish();
			}
		}
	}

	@Override
	public void registerSelection(String currentSelection) {
		this.selectedPattern = currentSelection;
	}

	public void assignPattern(View view) {
		if (selectedPattern != null) {
			AppshortcutDAO dao = new AppshortcutDAO();
			try{
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
											saveAction();
											onBackPressed();
										}

									}).setNegativeButton("Cancel", null).show();

				} else {
					saveAction();
					onBackPressed();
				}
			}catch(Exception e){
				//TODO Add String...
				Toast.makeText(getApplicationContext(), "Error Saving Information", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void saveAction(){
		try{
			AppshortcutDAO dao = new AppshortcutDAO();
			dao.saveAction(selectedPattern, getApplicationContext());
		}catch(Exception e){
			//TODO exception handle... 
		}
	}

	public void removeSelectedItem(){
		if (mActionLongOver.isSaved()){
			try{
				AppshortcutDAO dao = new AppshortcutDAO();
				dao.removeAction(mActionLongOver, getApplicationContext());
				ApplicationInfoFragment newFragment = new ApplicationInfoFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container_application,
								newFragment).commit();
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), "Error deleting application", Toast.LENGTH_SHORT).show();
				//TODO handle exception 
			}
		}
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.manage_common_list_context, menu);
	        return true;
	    }

	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.action_deselect:
	                removeSelectedItem();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	        }
	    }

	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	mActionLongOver = null;
	        mActionMode = null;
	    }
	};

	@Override
	public boolean longPressApplicationActionItem(ActionVo item, View eventView) {
		if (mActionMode == null && item.isSaved()) {
			mActionLongOver = item;
			mActionMode = ManageActionActivity.this
					.startActionMode(mActionModeCallback);
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void onSomething(String something) { }

	@Override
	public void fireApplication(String currentSelection) { }
}
