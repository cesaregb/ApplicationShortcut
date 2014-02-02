package com.il.easyclick.android.fragments;

import java.util.ArrayList;
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

import com.il.easyclick.R;
import com.il.easyclick.helpers.ServicesHelper;
import com.il.easyclick.services.ServiceVo;

public class ActivitySelectServicesDialogFragment extends DialogFragment {
	public int lastSelected = -1;
	ActivitySelectIconDialogImageAdapter gridAdapter;
	List<ServiceVo> myObjects;
	private List<ServiceVo> selected;

	ActivitySelectServicesDialogListener mCallback;
	
	public interface ActivitySelectServicesDialogListener {
		public void onServicesDialogPositiveClick(DialogFragment dialog, List<ServiceVo> list);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ActivitySelectServicesDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ActivitySelectServicesDialogListener");
		}
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if (selected == null)
			selected = new ArrayList<ServiceVo>();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		ServicesHelper helper  = new ServicesHelper(getActivity().getApplicationContext());;
		myObjects = helper.getServiceList();
		for (ServiceVo item: myObjects){
			for (ServiceVo selectedItem : selected){
				if (item.equals(selectedItem))
					item.setSelected(true);
			}
		}
		View layout = inflater.inflate(R.layout.comp_activity_select_icon_dialog, null);
		GridView grid = (GridView) layout.findViewById(R.id.activity_icon_list);
		gridAdapter = new ActivitySelectIconDialogImageAdapter(getActivity().getApplicationContext());
		grid.setAdapter(gridAdapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
            	if (selected.contains(myObjects.get(position))){
            		selected.remove(myObjects.get(position));
            		myObjects.get(position).setSelected(false);
            	}else{
            		selected.add(myObjects.get(position));
            		myObjects.get(position).setSelected(true);
            	}
                lastSelected = position;
                gridAdapter.notifyDataSetChanged();
            }
        });
		
		builder.setView(layout)
				.setTitle(R.string.title_select_services)
				.setPositiveButton(R.string.apply,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mCallback
										.onServicesDialogPositiveClick(
												ActivitySelectServicesDialogFragment.this,
												selected);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mCallback
										.onDialogNegativeClick(ActivitySelectServicesDialogFragment.this);
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
			ServiceVo item = myObjects.get(position);
			ImageView imageView;   
			if (convertView == null) {  
				imageView = new ImageView(mContext);   
				imageView.setLayoutParams(new GridView.LayoutParams(85, 85));   
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);   
				imageView.setPadding(5, 5, 5, 5);   
			} else {   
				imageView = (ImageView) convertView;   
			}
			
			if (item.isSelected()) {
				imageView.setBackgroundColor(Color.LTGRAY);
            }else{
            	imageView.setBackgroundColor(Color.WHITE);
            }
			imageView.setImageDrawable(item.getIcon());
			return imageView;   
		}   
	}

	public List<ServiceVo> getSelected() {
		return selected;
	}

	public void setSelected(List<ServiceVo> selected) {
		this.selected = selected;
	}   
	
}
