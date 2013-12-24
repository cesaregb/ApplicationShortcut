package com.il.easyclick.android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.il.easyclick.R;
import com.il.easyclick.views.SelectPatternInfoVo;

public class ApplicationSelectPatternFragment extends Fragment {
	ApplicationSelectPatternFragmentListener mCallback;
	public final static String ARG_POSITION = "application";
	private SelectPatternInfoVo mCurrentInformation = null;

	public interface ApplicationSelectPatternFragmentListener {
		public void onSomething(String something);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ApplicationSelectPatternFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ " must implement ApplicationSelectPatternFragmentListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//get the selected application..
		return inflater.inflate(R.layout.comp_action_select_pattern, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateApplicationView();
	}

	public void updateApplicationView() {
		TextView article = (TextView) getActivity().findViewById(R.id.app_name);
		article.setText(mCurrentInformation.getName());
		ImageView image = (ImageView) getActivity().findViewById(
				R.id.icon_app_selected);
//		if (mCurrentInformation.getCurrentAction().isAssigned()){
		if (mCurrentInformation.getPattern() != null){
			TextView selectedPattern = (TextView) getActivity().findViewById(R.id.selected_pattern);
			selectedPattern.setText(mCurrentInformation.getPattern());
		}
		
		if (mCurrentInformation.getIcon() != null){
			Bitmap bmpIcon = ((BitmapDrawable) mCurrentInformation.getIcon()).getBitmap();
			image.setImageBitmap(bmpIcon);
		}
	}

	public SelectPatternInfoVo getmCurrentInformation() {
		return mCurrentInformation;
	}

	public void setmCurrentInformation(SelectPatternInfoVo mCurrentInformation) {
		this.mCurrentInformation = mCurrentInformation;
	}

	
}
