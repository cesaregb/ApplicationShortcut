package com.il.appshortcut.android.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityListFragment extends ListFragment {
	
	private ActivityListFragmentListener mCallback;
	public interface ActivityListFragmentListener{
		public boolean onItemLongClickListener(Object object, int position, View eventView);
		public boolean onItemClickListener(Object object, int position, View eventView);
	}
	
	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent,
					View eventView, int position, long id) {
				return mCallback.onItemLongClickListener(getListView()
						.getItemAtPosition(position), position, eventView);
			}
		});
		
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onItemClickListener(getListView().getItemAtPosition(position), position, view);
			}
		});
	}

	public ActivityListFragmentListener getmCallback() {
		return mCallback;
	}

	public void setmCallback(ActivityListFragmentListener mCallback) {
		this.mCallback = mCallback;
	}
}
