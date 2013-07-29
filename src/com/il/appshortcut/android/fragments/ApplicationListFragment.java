package com.il.appshortcut.android.fragments;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.il.appshortcut.R;
import com.il.appshortcut.adapters.ApplicationListItemAdapter;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.views.ApplicationVo;


public class ApplicationListFragment extends Fragment {
	
	@Override
	public void onStart() {
		refresList();
		super.onStart();
	}

	public static int GRID_SIZE = 12;
	ApplicationListListener mCallback;
	
    public interface ApplicationListListener {
        public void onApplicationSelected(ApplicationVo appSelected);
    }
	
	GridView gridView;
	int position;
	public static final String ARG_SECTION_NUMBER = "section_number";
    
    private ArrayList<ApplicationVo> applicationItems;
	private ApplicationListItemAdapter aa;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ApplicationListListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ApplicationListListener");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState) {
        
		Bundle args = getArguments();
		position =  args.getInt(ARG_SECTION_NUMBER);
		
        View view = inflater.inflate(R.layout.comp_grid_view, container, false); 
        gridView = (GridView) view.findViewById(R.id.gridview);

        applicationItems = new ArrayList<ApplicationVo>();
		int resID = R.layout.comp_app_list_item;
		aa = new ApplicationListItemAdapter(view.getContext(), resID, applicationItems);
        gridView.setAdapter(aa);

		gridView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				mCallback.onApplicationSelected(applicationItems.get(position));
			}}
		);
        return view;
    }
	
	public void refresList(){
		applicationItems.clear();
		AppShortcutApplication appState = ((AppShortcutApplication)getActivity().getApplicationContext());
		List<ApplicationVo> l = appState.getCurrentListApplications();
		if (l != null){
			int arrPos = ((position>0)?position:1 - 1) * GRID_SIZE;
			int listSize = l.size();
			List<ApplicationVo> subSet = new ArrayList<ApplicationVo>();
			if ((listSize/GRID_SIZE) > position){
				subSet = l.subList(arrPos, (arrPos + GRID_SIZE) );
			}else{
				subSet = l.subList(arrPos, l.size() );
			}
			applicationItems.clear();
			applicationItems.addAll(subSet);
			aa.notifyDataSetChanged();
		}
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
}
