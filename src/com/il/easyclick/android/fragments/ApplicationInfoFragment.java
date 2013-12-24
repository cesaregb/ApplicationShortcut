package com.il.easyclick.android.fragments;

import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.il.easyclick.R;
import com.il.easyclick.android.adapters.ApplicationActionItemAdapter;
import com.il.easyclick.config.EasyClickApplication;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ApplicationVo;

/**
 * @author Cesaregb
 * Fragment for application selection 1st step 
 * show application actions such open, bla bla bla.. 
 *
 */
public class ApplicationInfoFragment extends Fragment {
	ApplicationInfoListener mCallback;

	public final static String ARG_POSITION = "position";
	private ApplicationVo mCurrentApplication = null;

	private ArrayList<ActionVo> applicationActionItems = new ArrayList<ActionVo>();
	private ApplicationActionItemAdapter aa;
	View view;

	public interface ApplicationInfoListener{
		public void onApplicationActionItem(ActionVo item);
		public boolean longPressApplicationActionItem(ActionVo item, View eventView);
	}

	ListView listView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ApplicationInfoListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ApplicationInfoListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.comp_action_info, container, false);
		listView = (ListView) view.findViewById(
				R.id.list_application_actions);
		
		int resID = R.layout.comp_action_list_item;
		aa = new ApplicationActionItemAdapter(view.getContext(), resID,
				applicationActionItems);
		listView.setAdapter(aa);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ActionVo appItem = (ActionVo) listView
						.getItemAtPosition(position);
				mCallback.onApplicationActionItem(appItem);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View eventView,
					int position, long id) {
				ActionVo appItem = (ActionVo) listView.getItemAtPosition(position);
				return mCallback.longPressApplicationActionItem(appItem, eventView);
			}
		});
		
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyClickApplication appState = (EasyClickApplication) getActivity()
				.getApplicationContext();
		
		mCurrentApplication = (ApplicationVo) appState.getAppSelected();
		updateApplicationView();
	}

	/**
	 * init list here is configured application actions (open, new message, etc...) 
	 * @param application
	 */
	public void updateApplicationView() {
		applicationActionItems.clear();
		if (mCurrentApplication.getCommonActions() != null 
				&& mCurrentApplication.getCommonActions().getActions() != null) {
			applicationActionItems.addAll(mCurrentApplication.getCommonActions().getActions());
		}
		aa.notifyDataSetChanged();

		TextView article = (TextView) getActivity().findViewById(R.id.app_name);
		article.setText(mCurrentApplication.getName());
		ImageView image = (ImageView) getActivity().findViewById(
				R.id.icon_app_selected);
		ApplicationInfo appInfo = mCurrentApplication.getApplicationInfo();
		Drawable icon = appInfo.loadIcon(getActivity().getApplicationContext()
				.getPackageManager());
		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();
		image.setImageBitmap(bmpIcon);
	}

	public ApplicationVo getmCurrentApplication() {
		return mCurrentApplication;
	}

	public void setmCurrentApplication(ApplicationVo mCurrentApplication) {
		this.mCurrentApplication = mCurrentApplication;
	}

}
