package com.il.appshortcut.fragments;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
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
import com.il.appshortcut.views.ApplicationItem;

import static com.il.appshortcut.helpers.ActionHelper.isAssignedByApplication;


public class ApplicationListFragment extends Fragment {
	ApplicationListListener mCallback;
	
	 // The container Activity must implement this interface so the frag can deliver messages
    public interface ApplicationListListener {
        public void onApplicationSelected(ApplicationItem appSelected);
    }
	
	GridView gridView;
	final static String ARG_POSITION = "position";
    int mCurrentPosition;
    
    private String filterAppName;
    private int filterType;
    private ArrayList<ApplicationItem> applicationItems;
	private ApplicationListItemAdapter aa;
	private List<ApplicationItem> listApplications;

	public void init(){
		filterAppName = "";
		filterType = 0;
		mCurrentPosition = -1;
		listApplications = new ArrayList<ApplicationItem>();
	}
	
	

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
		init();
		super.onCreate(savedInstanceState);
		new LoadApplication().execute();
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        
        View view = inflater.inflate(R.layout.comp_grid_view, container, false); 
        gridView = (GridView) view.findViewById(R.id.gridview);

        applicationItems = new ArrayList<ApplicationItem>();
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void refresList(){
		applicationItems.clear();
		List<ApplicationItem> tmpList = filterList(listApplications);
		if (!tmpList.isEmpty()){
			for (ApplicationItem application : tmpList){
				addNewItem( application );
			}
		}else{
			aa.notifyDataSetChanged();
		}
	}
	
	public List<ApplicationItem> filterList(List<ApplicationItem> list){
		boolean someNameSearch = (filterAppName != null && !(filterAppName.equalsIgnoreCase("")));
		List<ApplicationItem> returnList = new ArrayList<ApplicationItem>();
		if (someNameSearch || (filterType > 0) && list != null){
			for (ApplicationItem item : list){
				
				boolean type = (filterType == 0)
						|| (filterType == 1 && item.isAssigned())
						|| (filterType == 2 && !item.isAssigned());
				
				boolean name =  (filterAppName != null 
						&& !filterAppName.equalsIgnoreCase("")
						&& item.getApplicationName().toLowerCase().contains(filterAppName.toLowerCase()));
				
				boolean add = name && type;
				
				if (add) {
					returnList.add(item);
				}
			}
		}else{
			returnList = listApplications;
		}
		return returnList;
	}

	public void addNewItem(ApplicationItem appItem) {
		applicationItems.add(0, appItem);
		aa.notifyDataSetChanged();
	}
	
	public boolean allowBackPressed(){
		return true;
	}
	
	public void setFilterValues(String appName, int filterType){
		this.filterAppName = appName;
		this.filterType = filterType;
	}
	

	/**
	 * @author cesaregb
	 * progress dialog 
	 * loads application list
	 */
	private ProgressDialog progressDialog;
	public class LoadApplication extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute(){
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Loading Applicationss...");
			progressDialog.setIndeterminate(false);
			progressDialog.setMax(100);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			PackageManager pm = getActivity().getPackageManager();
			List<ApplicationInfo> apps = pm.getInstalledApplications(0);
			int i = 0;
			for (ApplicationInfo app : apps) {
				boolean addItem = false;
				i++;
				publishProgress((int) ((i / (float) apps.size()) * 100));
				
				if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
					//system applicatoins 
				} else {
					addItem = true;
				}
				
				
				if(addItem){
					ApplicationItem item = new ApplicationItem((String)pm.getApplicationLabel(app));
					item.setApplicationInfo(app);
					
					SharedPreferences sharedPref = getActivity().getApplicationContext()
							.getSharedPreferences(String.valueOf(R.string.idPrefFile),
									Context.MODE_PRIVATE);
					Resources r = getResources();
					boolean assigned = isAssignedByApplication(item, sharedPref, r);
					item.setAssigned(assigned);
					
					listApplications.add(item);
				}
			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		protected void onPostExecute(String params) {
			refresList();
			progressDialog.dismiss();
		}
	}
	
}
