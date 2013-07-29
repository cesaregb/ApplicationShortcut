package com.il.appshortcut.android.fragments;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.il.appshortcut.R;
import com.il.appshortcut.config.AppShortcutApplication;
import com.il.appshortcut.views.ApplicationVo;

public class ApplicationSelectPatternFragment extends Fragment {
	ApplicationSelectPatternFragmentListener mCallback;
	public final static String ARG_POSITION = "application";
	private ApplicationVo mCurrentApplication = null;

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
		AppShortcutApplication appState = (AppShortcutApplication)getActivity().getApplicationContext();
		mCurrentApplication = (ApplicationVo) appState.getAppSelected();
		
		return inflater.inflate(R.layout.comp_app_select_pattern, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		updateApplicationView(mCurrentApplication);
	}

	public void updateApplicationView(ApplicationVo application) {
		mCurrentApplication = application;
		
		TextView article = (TextView) getActivity().findViewById(R.id.app_name);
		article.setText(application.getName());
		ImageView image = (ImageView) getActivity().findViewById(
				R.id.icon_app_selected);
		ApplicationInfo appInfo = application.getApplicationInfo();
		Drawable icon = appInfo.loadIcon(getActivity().getApplicationContext()
				.getPackageManager());
		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
		image.setImageBitmap(bmpIcon);
	}

	
}
