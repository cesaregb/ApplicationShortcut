package com.il.appshortcut.android.fragments;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.il.appshortcut.R;
import com.il.appshortcut.helpers.ActivityIconHelper;
import com.il.appshortcut.views.EventIconVo;

public class ActivitySelectIconDialogFragment extends DialogFragment {
	public int lastSelected = -1;
	ActivitySelectIconDialogImageAdapter gridAdapter;
	List<EventIconVo> myObjects;
	

	ActivitySelectIconDialogListener mCallback;
	
	public interface ActivitySelectIconDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, EventIconVo activityIcon);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ActivitySelectIconDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ActivitySelectIconDialogListener");
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		myObjects = ActivityIconHelper.getIcons();
		View layout = inflater.inflate(R.layout.comp_activity_select_icon_dialog, null);
		GridView grid = (GridView) layout.findViewById(R.id.activity_icon_list);
		gridAdapter = new ActivitySelectIconDialogImageAdapter(getActivity().getApplicationContext());
		grid.setAdapter(gridAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
            	if (lastSelected >= 0 && lastSelected != position){
                	myObjects.get(lastSelected).setSelected(false);
                }
                lastSelected = position;
                
                myObjects.get(position).setSelected(true);
                gridAdapter.notifyDataSetChanged();
            }
        });
		
		builder.setView(layout)
				.setTitle(R.string.filter)
				.setPositiveButton(R.string.apply,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mCallback.onDialogPositiveClick(ActivitySelectIconDialogFragment.this, myObjects.get(lastSelected));
								myObjects.get(lastSelected).setSelected(false);
								lastSelected = -1;
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mCallback.onDialogNegativeClick(ActivitySelectIconDialogFragment.this);
							}
						})
		;
		return builder.create();
	}
	
	class ActivitySelectIconDialogImageAdapter extends BaseAdapter {   
		private Context mContext;   
		public ActivitySelectIconDialogImageAdapter(Context c) {   
			mContext = c;   
		}   
		public int getCount() {   
			return myObjects.size();   
		}   
		public Object getItem(int position) {   
			return null;   
		}   
		public long getItemId(int position) {   
			return 0;   
		}   
		public View getView(int position, View convertView, ViewGroup   
				parent) {
			EventIconVo icon = myObjects.get(position);
			ImageView imageView;   
			if (convertView == null) {  
				imageView = new ImageView(mContext);   
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));   
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);   
				imageView.setPadding(8, 8, 8, 8);   
			} else {   
				imageView = (ImageView) convertView;   
			}
			
			if (icon.isSelected()) {
				imageView.setBackgroundColor(Color.LTGRAY);
            }else{
            	imageView.setBackgroundColor(Color.WHITE);
            }
			imageView.setImageResource(icon.getIdResource());
			return imageView;   
		}   
	}   
	
}
