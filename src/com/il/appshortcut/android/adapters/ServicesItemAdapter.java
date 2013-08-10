package com.il.appshortcut.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.il.appshortcut.R;
import com.il.appshortcut.services.ServiceVo;

public class ServicesItemAdapter extends ArrayAdapter<ServiceVo> {
	int resource;

	public void init(Context context){
	}
	
	ServicesItemAdapterListener mCallback;
	public interface ServicesItemAdapterListener{
		public void itemSelected(ServiceVo activity);
	}
	
	public void setCallback(ServicesItemAdapterListener callback){
		this.mCallback = callback;
	}
	
	
	public ServicesItemAdapter(Context context, int resource,
			List<ServiceVo> items) {
		super(context, resource, items);
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
 
		title.setText(item.getName());
		artist.setText(item.getName());
		duration.setText(item.getName());
		thumb_image.setImageDrawable(item.getIcon());
		
		
		final ServiceVo itemParam = item;
		if (mCallback != null){
			OnClickListener listenerCB = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.itemSelected(itemParam);
				}
			};
			thumb_image.setOnClickListener(listenerCB);
		}
		return vi;
	}
}