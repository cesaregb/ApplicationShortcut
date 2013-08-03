package com.il.appshortcut.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.il.appshortcut.R;
import com.il.appshortcut.android.adapters.ActivityActionItemAdapter;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityVo;

public class ActivityFormFragment extends Fragment {
	ActivityFormListener mCallback;
	public interface ActivityFormListener{
	}
	
	public final static String ARG_POSITION = "position";
	private ActivityVo mCurrentactivity;
	
	private ArrayList<ActivityDetailVo> acticityApplicationActionsItems;
	private ActivityActionItemAdapter applicationActionsItemsArrayAdapter;
	
	private ArrayList<ActivityDetailVo> acticityApplicationServicesItems;
	private ActivityActionItemAdapter applicationServicesItemsArrayAdapter;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ActivityFormListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ActivityFormListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		//get the activity selected! 
		AppShortcutApplication appState = (AppShortcutApplication)getActivity().getApplicationContext();
		mCurrentactivity = appState.getCurrentActivity();
		return inflater.inflate(R.layout.comp_activity_form, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateArticleView();
	}

	public void updateArticleView() {
		if (mCurrentactivity != null){ 
			// fill saved information.. 
			EditText editTextName = (EditText)getActivity().findViewById(R.id.activityName);
			if (editTextName != null){
				editTextName.setText(mCurrentactivity.getName());
			}
			EditText editTextDescription = (EditText)getActivity().findViewById(R.id.acticityDescription);
			if (editTextDescription != null){
				editTextDescription.setText(mCurrentactivity.getDescription());
			}
			
		} 
		
		ListView listView = (ListView)getActivity().findViewById(R.id.list_activity_actions);
		acticityApplicationActionsItems = new ArrayList<ActivityDetailVo>();
		int resID = R.layout.comp_activity_action_list_item;
		applicationActionsItemsArrayAdapter = new ActivityActionItemAdapter(getActivity(), resID, acticityApplicationActionsItems);
		listView.setAdapter(applicationActionsItemsArrayAdapter);
		
		//TODO Get information from dao
		List<ActivityDetailVo> list = new ArrayList<ActivityDetailVo>();
		ActivityDetailVo o1 = new ActivityDetailVo();
		Drawable myIcon = getResources().getDrawable( R.drawable.cuadrito_selected );
		o1.setIcon(myIcon);
		o1.setIdActivity(1);
		list.add(o1);
		
		ActivityDetailVo o2 = new ActivityDetailVo();
		o2.setIcon(myIcon);
		o2.setIdActivity(1);
		list.add(o2);
		applicationActionsItemsArrayAdapter.setItems(list);
		for (ActivityDetailVo detail : list){
			addItem2AppList(detail);
		}
		
		ListView listServices = (ListView)getActivity().findViewById(R.id.list_activity_services);
		acticityApplicationServicesItems = new ArrayList<ActivityDetailVo>();
		int resIDService = R.layout.comp_activity_service_list_item;
		applicationServicesItemsArrayAdapter = new ActivityActionItemAdapter(getActivity(), resIDService, acticityApplicationServicesItems);
		listServices.setAdapter(applicationServicesItemsArrayAdapter);
		
		//TODO Get information from dao
		applicationServicesItemsArrayAdapter.setItems(list);
		for (ActivityDetailVo detail : list){
			addItem2ServiceList(detail);
		}
		
	}
	
	public void addItem2AppList(ActivityDetailVo item){
		acticityApplicationActionsItems.add(0, item);
		applicationActionsItemsArrayAdapter.notifyDataSetChanged();
	}
	public void addItem2ServiceList(ActivityDetailVo item){
		acticityApplicationServicesItems.add(0, item);
		applicationServicesItemsArrayAdapter.notifyDataSetChanged();
	}

}
