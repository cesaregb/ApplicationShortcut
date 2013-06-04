package com.il.appshortcut.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
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
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.helpers.ActionHelper;
import com.il.appshortcut.views.ActionVo;
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

	private ArrayList<ActionVo> applicationActionItems;
	private ArrayList<ActionVo> applicationActionItemsSelected;
	private ApplicationActionItemAdapter aa;
	private ApplicationActionItemAdapter aaSelected;

	public interface ApplicationInfoListener{
		public void onApplicationActionItem(ActionVo item);
		public void onApplicationActionItemSelected(ActionVo item);

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
		//get the selected application..
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
	 * init list
	 * here is configured application actions (open, new message, etc...) 
	 * @param application
	 */
	public void updateApplicationView(ApplicationVo application) {
		SharedPreferences sharedPref = getActivity().getApplicationContext()
				.getSharedPreferences(String.valueOf(R.string.idPrefFile),
						Context.MODE_PRIVATE);
		
		List<ActionVo> allActions = null; 
		List<ActionVo> selectedActions = null; 
		Resources r = null;
		try{
			r = getActivity().getResources();
			allActions = ActionHelper.getApplicationPossibledActions(mCurrentApplication);
			selectedActions = ActionHelper.getApplicationSelectedActions(allActions, mCurrentApplication, sharedPref, r);
		}catch(Exception e){ }
		
		
		TextView article = (TextView) getActivity().findViewById(R.id.app_name);
		article.setText(application.getName());
		ImageView image = (ImageView) getActivity().findViewById(R.id.icon_app_selected);
		ApplicationInfo appInfo = application.getApplicationInfo();
		Drawable icon = appInfo.loadIcon(getActivity().getApplicationContext().getPackageManager());
		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
		image.setImageBitmap(bmpIcon);

		listView = (ListView)getActivity().findViewById(R.id.list_selected_actions);
		applicationActionItems = new ArrayList<ActionVo>();
		int resID = R.layout.comp_app_action_item;
		aa = new ApplicationActionItemAdapter(getActivity(), resID, applicationActionItems);
		listView.setAdapter(aa);

		OnItemClickListener listener = new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ActionVo appItem = (ActionVo) listView.getItemAtPosition(position);
				mCallback.onApplicationActionItem(appItem);
			}
		};
		listView.setOnItemClickListener(listener);
		for (ActionVo item : allActions){
			addApplicatoinActionItem(item, 0);
		}
		
		listViewSelected = (ListView)getActivity().findViewById(R.id.list_possible_actions);
		applicationActionItemsSelected = new ArrayList<ActionVo>();
		resID = R.layout.comp_app_action_item;
		aaSelected = new ApplicationActionItemAdapter(getActivity(), resID, applicationActionItemsSelected);
		listViewSelected.setAdapter(aaSelected);
		OnItemClickListener listenerSelected = new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ActionVo appItem = (ActionVo) listViewSelected.getItemAtPosition(position);
				mCallback.onApplicationActionItemSelected(appItem);
			}
		};
		listViewSelected.setOnItemClickListener(listenerSelected);
		for (ActionVo item : selectedActions){
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
	public void addApplicatoinActionItem(ActionVo item, int type){
		if (item == null){
			item = new ActionVo();
			item.setName("...Error...");
			item.setApplicationPackage(mCurrentApplication.getApplicationInfo().packageName);
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
