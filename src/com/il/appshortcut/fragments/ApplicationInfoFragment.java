package com.il.appshortcut.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.il.appshortcut.R;
import com.il.appshortcut.adapters.ApplicationActionItemAdapter;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.helpers.ActionHelper;
import com.il.appshortcut.views.ApplicationActionVo;
import com.il.appshortcut.views.ApplicationVo;

/**
 * @author Cesaregb
 * Fragment for application selection 1st step 
 * show application actions such open, bla bla bla.. 
 *
 */
public class ApplicationInfoFragment extends Fragment {
	ApplicationInfoListener mCallback;

	public final static String ARG_POSITION = "position";
	private ApplicationVo mCurrentApplication = null;

	private ArrayList<ApplicationActionVo> applicationActionItems;
	private ArrayList<ApplicationActionVo> applicationActionItemsSelected;
	private ApplicationActionItemAdapter aa;
	private ApplicationActionItemAdapter aaSelected;

	public interface ApplicationInfoListener{
		public void onApplicationActionItem(ApplicationActionVo item);
		public void onApplicationActionItemSelected(ApplicationActionVo item);
		public void updateNumberOfActionsByApplication(int number);

	}

	ListView listView;
	ListView listViewSelected;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ApplicationInfoListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ApplicationInfoListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		AppShortcutApplication appState = (AppShortcutApplication)getActivity().getApplicationContext();
		mCurrentApplication = (ApplicationVo) appState.getAppSelected();
		
		return inflater.inflate(R.layout.comp_app_info, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateApplicationView(mCurrentApplication);
	}

	/**
	 * init list here is configured application actions (open, new message, etc...) 
	 * @param application
	 */
	public void updateApplicationView(ApplicationVo application) {
		
		SharedPreferences sharedPref = getActivity().getApplicationContext()
				.getSharedPreferences(AppManager.ID_PRE_FFILE,
						Context.MODE_PRIVATE);
		
		List<ApplicationActionVo> notSelectedActions = new ArrayList<ApplicationActionVo>(); 
		List<ApplicationActionVo> selectedActions = new ArrayList<ApplicationActionVo>(); 
		int countActionsByApplication = 0;
		if (application.getActions() != null
				&& application.getActions().getActions() != null ){
			for (ApplicationActionVo a : application.getActions().getActions()){
				String pattern = ActionHelper.isAssignedByAction(application, a, sharedPref);
				if (pattern != null && !pattern.equals("")){
					countActionsByApplication++;
					a.setPatter(pattern);
					selectedActions.add(a);
				}else{
					notSelectedActions.add(a);
				}
			}
			
		}
		if(countActionsByApplication > 0){
			mCallback.updateNumberOfActionsByApplication(countActionsByApplication);
		}
		
		TextView article = (TextView) getActivity().findViewById(R.id.app_name);
		article.setText(application.getName());
		ImageView image = (ImageView) getActivity().findViewById(R.id.icon_app_selected);
		ApplicationInfo appInfo = application.getApplicationInfo();
		Drawable icon = appInfo.loadIcon(getActivity().getApplicationContext().getPackageManager());
		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
		image.setImageBitmap(bmpIcon);

		listView = (ListView)getActivity().findViewById(R.id.list_selected_actions);
		applicationActionItems = new ArrayList<ApplicationActionVo>();
		int resID = R.layout.comp_app_action_item;
		aa = new ApplicationActionItemAdapter(getActivity(), resID, applicationActionItems);
		listView.setAdapter(aa);

		OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ApplicationActionVo appItem = (ApplicationActionVo) listView.getItemAtPosition(position);
				mCallback.onApplicationActionItem(appItem);
			}
		};
		listView.setOnItemClickListener(listener);
		for (ApplicationActionVo item : notSelectedActions){
			addApplicatoinActionItem(item, 0);
		}
		
		listViewSelected = (ListView)getActivity().findViewById(R.id.list_possible_actions);
		applicationActionItemsSelected = new ArrayList<ApplicationActionVo>();
		resID = R.layout.comp_app_action_item;
		aaSelected = new ApplicationActionItemAdapter(getActivity(), resID, applicationActionItemsSelected);
		listViewSelected.setAdapter(aaSelected);
		OnItemClickListener listenerSelected = new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ApplicationActionVo appItem = (ApplicationActionVo) listViewSelected.getItemAtPosition(position);
				mCallback.onApplicationActionItemSelected(appItem);
			}
		};
		listViewSelected.setOnItemClickListener(listenerSelected);
		for (ApplicationActionVo item : selectedActions){
			addApplicatoinActionItem(item, 1);
		}
	}

	public ApplicationVo getmCurrentApplication() {
		return mCurrentApplication;
	}

	public void setmCurrentApplication(ApplicationVo mCurrentApplication) {
		this.mCurrentApplication = mCurrentApplication;
	}

	/**
	 * function to add items. 
	 * @param item
	 * @param type
	 */
	public void addApplicatoinActionItem(ApplicationActionVo item, int type){
		if (item == null){
			item = new ApplicationActionVo();
			item.setName("...Error...");
			item.setActionPackage(mCurrentApplication.getApplicationInfo().packageName);
		}
		if (type == 0){
			applicationActionItems.add(0, item);
			aa.notifyDataSetChanged();
		}else{
			applicationActionItemsSelected.add(0, item);
			aaSelected.notifyDataSetChanged();
		}
	}
}
