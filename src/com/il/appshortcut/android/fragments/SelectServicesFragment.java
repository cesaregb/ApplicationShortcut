package com.il.appshortcut.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.il.appshortcut.R;
import com.il.appshortcut.android.adapters.ServicesItemAdapter;
import com.il.appshortcut.config.AppManager;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.helpers.ServicesHelper;
import com.il.appshortcut.services.ServiceVo;
import com.il.appshortcut.views.ActivityVo;

public class SelectServicesFragment extends Fragment implements
		ServicesItemAdapter.ServicesItemAdapterListener {
	ServicesHelper helper;
	SelectServicesListener mCallback;
	
	public interface SelectServicesListener{
		public void refreshList(List<ServiceVo> list);
		public List<ServiceVo> getParentList();
	}
	
	public final static String ARG_POSITION = "position";
	private ActivityVo mCurrentactivity;
	
	private List<ServiceVo> serviceItems = new ArrayList<ServiceVo>();
	private ServicesItemAdapter servicesArrayAdapter;
	
	private List<ServiceVo> returnSelectedServices = new ArrayList<ServiceVo>();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (SelectServicesListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement SelectServicesListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		AppShortcutApplication appState = (AppShortcutApplication)getActivity().getApplicationContext();
		mCurrentactivity = appState.getCurrentActivity();
		View view = inflater.inflate(R.layout.comp_activity_services_dialog, container, false);
		helper = new ServicesHelper(getActivity().getApplicationContext());
		
		
		ListView listFragment = (ListView) view.findViewById(R.id.list_services);
		serviceItems = new ArrayList<ServiceVo>();
		
		int resID = R.layout.comp_activity_services_list_dialog;
		servicesArrayAdapter = new ServicesItemAdapter(view.getContext(), resID, serviceItems);
		servicesArrayAdapter.setCallback(this);
		listFragment.setAdapter(servicesArrayAdapter);
		listFragment.setChoiceMode(ListView.CHOICE_MODE_NONE);
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (returnSelectedServices == null ){
			returnSelectedServices = new ArrayList<ServiceVo>();
		}
		updateServiceListeView();
	}

	public void updateServiceListeView() {
		if (mCurrentactivity != null){ 
			// fill saved information.. 
		} 
		updateServiceList();
	}
	
	public void updateServiceList(){
		Log.d(AppManager.LOG_ACTIVITIES, "Fragment size: " + returnSelectedServices.size());
		serviceItems.clear();
		serviceItems.addAll(helper.getServiceList(mCallback.getParentList()));
		servicesArrayAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void addServiceSelected(ServiceVo service) {
		returnSelectedServices.add(service);
		mCallback.refreshList(returnSelectedServices);
	}

	@Override
	public void removeServiceSelected(ServiceVo service) {
		int pos = 0;
		for (ServiceVo item : returnSelectedServices){
			if (item.equals(service)){
				returnSelectedServices.remove(pos);
				break;
			}
			pos++;
		}
		mCallback.refreshList(returnSelectedServices);
	}

	public List<ServiceVo> getSelections(){
		return servicesArrayAdapter.getSelections();
	}
	
}
