package com.il.easyclick.android.fragments;

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

import com.il.easyclick.R;
import com.il.easyclick.android.adapters.ActivityDetailItemAdapter;
import com.il.easyclick.android.listeners.SwipeDismissListViewTouchListener;
import com.il.easyclick.helpers.ActivityIconHelper;
import com.il.easyclick.views.ActivityDetailVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.EventIconVo;

public class ActivityFormFragment extends Fragment {
	
	ActivityFormListener mCallback;
	
	public interface ActivityFormListener{
		public List<ActivityDetailVo> getDetailItemsArray();
		public void removeActivityDetail(ActivityDetailVo detail);
		public void selectIcon(EventIconVo idDrawable);
	}
	
	public final static String ARG_POSITION = "position";
	private ActivityVo mCurrentActivity;
	
	private List<ActivityDetailVo> activityDetailsItems;
	private ActivityDetailItemAdapter activityDetailsItemsArrayAdapter;
	ListView listActivityDetails;
	
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

		//Application Services List 
		int resID = R.layout.comp_simple_list_item;
		activityDetailsItems = new ArrayList<ActivityDetailVo>();
		activityDetailsItemsArrayAdapter = new ActivityDetailItemAdapter(
				view.getContext(), resID, activityDetailsItems);
		listActivityDetails = (ListView) view.findViewById(R.id.list_activity_actions);
		listActivityDetails.setAdapter(activityDetailsItemsArrayAdapter);
		SwipeDismissListViewTouchListener touchActionsListener = new SwipeDismissListViewTouchListener(
				listActivityDetails,
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
									.removeActivityDetail(activityDetailsItemsArrayAdapter
											.getItem(position));
							activityDetailsItems.clear();
							activityDetailsItems.addAll(mCallback.getDetailItemsArray());
						}
						activityDetailsItemsArrayAdapter.notifyDataSetChanged();
					}
				});
		
		listActivityDetails.setOnTouchListener(touchActionsListener);
		listActivityDetails.setOnScrollListener(touchActionsListener.makeScrollListener());
		//End Application Services List
		return view;
	}
	
	public void updateIcon(EventIconVo icon){
		activityIcon.setImageResource(icon.getIdResource());
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateServiceFormView();
	}

	public void updateServiceFormView() {
		if (mCurrentActivity != null) {
			EditText editTextName = (EditText) getActivity().findViewById(
					R.id.activityName);
			if (editTextName != null) {
				editTextName.setText(mCurrentActivity.getName());
			}
			editTextName.setSelected(false);
			EditText editTextDescription = (EditText) getActivity()
					.findViewById(R.id.acticityDescription);
			if (editTextDescription != null) {
				editTextDescription.setText(mCurrentActivity.getDescription());
			}

			if (mCurrentActivity.getIdIcon() > 0) {
				activityIcon.setImageResource(ActivityIconHelper
						.getDrawableResource(mCurrentActivity.getIdIcon()));
			}
		}
		
		if (mCurrentActivity.getActivityDetailListVo() != null 
				&& mCurrentActivity.getActivityDetailListVo().data != null 
				&& mCurrentActivity.getActivityDetailListVo().size() > 0) {
			activityDetailsItems.clear();
			activityDetailsItems.addAll(mCurrentActivity.getActivityDetailListVo().data);
			activityDetailsItemsArrayAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		updateServiceFormView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public ActivityVo getmCurrentActivity() {
		return mCurrentActivity;
	}

	public void setmCurrentActivity(ActivityVo mCurrentActivity) {
		this.mCurrentActivity = mCurrentActivity;
	}

	public ActivityDetailItemAdapter getActivityDetailsItemsArrayAdapter() {
		return activityDetailsItemsArrayAdapter;
	}

	public void setActivityDetailsItemsArrayAdapter(
			ActivityDetailItemAdapter activityDetailsItemsArrayAdapter) {
		this.activityDetailsItemsArrayAdapter = activityDetailsItemsArrayAdapter;
	}
}
