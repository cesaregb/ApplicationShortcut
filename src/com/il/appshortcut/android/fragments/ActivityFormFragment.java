package com.il.appshortcut.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.il.appshortcut.R;
import com.il.appshortcut.android.adapters.ActivityActionItemAdapter;
import com.il.appshortcut.android.listeners.SwipeDismissListViewTouchListener;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.helpers.ActivityIconHelper;
import com.il.appshortcut.views.ActivityDetailVo;
import com.il.appshortcut.views.ActivityIconVo;
import com.il.appshortcut.views.ActivityVo;

public class ActivityFormFragment extends Fragment {
	
	ActivityFormListener mCallback;
	private List<ActivityDetailVo> activitiesDetails;
	
	public interface ActivityFormListener{
		public void removeService(ActivityDetailVo detail);
		public void removeAction(ActivityDetailVo detail);
		public void selectIcon(ActivityIconVo idDrawable);
	}
	
	public final static String ARG_POSITION = "position";
	private ActivityVo mCurrentactivity;
	
	private ArrayList<ActivityDetailVo> acticityApplicationActionsItems  = new ArrayList<ActivityDetailVo>();;
	private ActivityActionItemAdapter applicationActionsItemsArrayAdapter;
	ListView listActions;
	
	private ArrayList<ActivityDetailVo> acticityApplicationServicesItems = new ArrayList<ActivityDetailVo>();
	private ActivityActionItemAdapter applicationServicesItemsArrayAdapter;
	ListView listServices;
	View view;
	ImageView activityIcon;
	
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
		view = inflater.inflate(R.layout.comp_activity_form, container, false);
		
		activityIcon = (ImageView) view.findViewById(R.id.activityIcon);
		listActions = (ListView) view.findViewById(R.id.list_activity_actions);
		int resID = R.layout.comp_activity_action_list_item;
		applicationActionsItemsArrayAdapter = new ActivityActionItemAdapter(
				view.getContext(), resID, acticityApplicationActionsItems);
		listActions.setAdapter(applicationActionsItemsArrayAdapter);

		SwipeDismissListViewTouchListener touchActionsListener = new SwipeDismissListViewTouchListener(
				listActions,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}
					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							mCallback
									.removeAction(applicationActionsItemsArrayAdapter
											.getItem(position));
							applicationActionsItemsArrayAdapter
									.remove(applicationActionsItemsArrayAdapter
											.getItem(position));
						}
						applicationActionsItemsArrayAdapter.notifyDataSetChanged();
					}
				});
		
		listActions.setOnTouchListener(touchActionsListener);
		listActions.setOnScrollListener(touchActionsListener.makeScrollListener());
		
		listServices = (ListView) view
				.findViewById(R.id.list_activity_services);
		int resIDService = R.layout.comp_activity_service_list_item;
		applicationServicesItemsArrayAdapter = new ActivityActionItemAdapter(
				view.getContext(), resIDService,
				acticityApplicationServicesItems);
		listServices.setAdapter(applicationServicesItemsArrayAdapter);

		SwipeDismissListViewTouchListener touchServicesListener = new SwipeDismissListViewTouchListener(
				listServices,
				new SwipeDismissListViewTouchListener.DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							mCallback.removeService(applicationServicesItemsArrayAdapter.getItem(position));
							applicationServicesItemsArrayAdapter.remove(applicationServicesItemsArrayAdapter.getItem(position));
						}
						applicationServicesItemsArrayAdapter.notifyDataSetChanged();
					}
				});
		
		listServices.setOnTouchListener(touchServicesListener);
		listServices.setOnScrollListener(touchServicesListener.makeScrollListener());
		
		AppShortcutApplication appState = (AppShortcutApplication) view.getContext().getApplicationContext();
		mCurrentactivity = appState.getCurrentActivity();
		return view;
	}
	
	public void updateIcon(ActivityIconVo icon){
		activityIcon.setImageResource(icon.getIdResource());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateArticleView();
	}

	public void updateArticleView() {
		if (mCurrentactivity != null) {
			EditText editTextName = (EditText) getActivity().findViewById(
					R.id.activityName);
			if (editTextName != null) {
				editTextName.setText(mCurrentactivity.getName());
			}
			EditText editTextDescription = (EditText) getActivity()
					.findViewById(R.id.acticityDescription);
			if (editTextDescription != null) {
				editTextDescription.setText(mCurrentactivity.getDescription());
			}

			if (mCurrentactivity.getIdIcon() > 0) {
				activityIcon.setImageResource(ActivityIconHelper
						.getDrawable(mCurrentactivity.getIdIcon()));
			}
		}
		
		if (activitiesDetails != null && activitiesDetails.size() > 0) {
			acticityApplicationServicesItems.clear();
			acticityApplicationActionsItems.clear();
			List<ActivityDetailVo> actions = new ArrayList<ActivityDetailVo>();
			List<ActivityDetailVo> services = new ArrayList<ActivityDetailVo>();

			for (ActivityDetailVo detail : activitiesDetails) {
				if (detail.getType() == ActivitiesDAO.TYPE_ACTION) {
					actions.add(detail);
				} else {
					services.add(detail);
				}
			}
			acticityApplicationActionsItems.addAll(actions);
			applicationActionsItemsArrayAdapter.notifyDataSetChanged();

			acticityApplicationServicesItems.addAll(services);
			applicationServicesItemsArrayAdapter.notifyDataSetChanged();
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

	public List<ActivityDetailVo> getActivitiesDetails() {
		return activitiesDetails;
	}

	public void setActivitiesDetails(List<ActivityDetailVo> activitiesDetails) {
		this.activitiesDetails = activitiesDetails;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateArticleView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		registerForContextMenu(getListView());
	}

}
