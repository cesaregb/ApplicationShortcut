package com.il.appshortcut.android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.il.appshortcut.R;

public class ActivityFormFragment extends Fragment {
	ActivityFormListener mCallback;
	
	public final static String ARG_POSITION = "position";
	private int mCurrentPosition = 0;
	
	public interface ActivityFormListener{
		public void saveActivity();
	}
	
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
		if (savedInstanceState != null){
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		}
		
		return inflater.inflate(R.layout.comp_activity_form, container, false);
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		if (args != null) {
			updateArticleView(args.getInt(ARG_POSITION));
		} else if (mCurrentPosition != -1) {
			updateArticleView(mCurrentPosition);
		}
	}

	public void updateArticleView(int position) {
		if (position > 0 ){
			//TODO get information from dao and fill the form. 
		}
		mCurrentPosition = position;
	}
}
