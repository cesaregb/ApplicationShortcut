package com.il.appshortcut.android.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.il.appshortcut.R;
import com.il.appshortcut.services.ServiceVo;

public class ServicesItemAdapter extends ArrayAdapter<ServiceVo> {
	int resource;

	public void init(Context context){
	}
	
	List<ServiceVo> serviceList;
	ServicesItemAdapterListener mCallback;
	public interface ServicesItemAdapterListener{
		public void addServiceSelected(ServiceVo service);
		public void removeServiceSelected(ServiceVo service);
	}
	
	public void setCallback(ServicesItemAdapterListener callback){
		this.mCallback = callback;
	}
	
	public ServicesItemAdapter(Context context, int resource,
			List<ServiceVo> items) {
		super(context, resource, items);
		serviceList = items;
		init(context);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout vi;
		ServiceVo item = getItem(position);
		if (convertView == null) {
			vi = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, vi, true);
		} else {
			vi = (LinearLayout) convertView;
		}

		TextView title = (TextView) vi.findViewById(R.id.title);
		TextView artist = (TextView) vi.findViewById(R.id.artist);
		TextView duration = (TextView) vi.findViewById(R.id.duration);
		ImageView thumb_image = (ImageView)vi.findViewById(R.id.list_image);
		CheckBox selectedCheckbox = (CheckBox)vi.findViewById(R.id.selectedCheckbox);
		if (item != null){
			title.setText(item.getName());
			artist.setText(item.getName());
			duration.setText(item.getName());
			thumb_image.setImageDrawable(item.getIcon());
			if (mCallback != null) {
				
//				selectedCheckbox.setOnCheckedChangeListener(myCheckChangList);
				
				final int posParam = position;
				selectedCheckbox
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						getService(posParam).setSelected(isChecked);
					}
				});
			}
			selectedCheckbox.setChecked(item.isSelected());
		}
		return vi;
	}
	
	ServiceVo getService(int position) {
		return ((ServiceVo) getItem(position));
	}
	
	
	public ArrayList<ServiceVo> getSelections() {
		ArrayList<ServiceVo> box = new ArrayList<ServiceVo>();
		for (ServiceVo p : serviceList) {
			if (p.isSelected())
				box.add(p);
		}
		return box;
	}
	
	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getService((Integer) buttonView.getTag()).setSelected(isChecked);
		}
	};
	
}