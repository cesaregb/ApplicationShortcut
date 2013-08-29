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

import com.il.appshortcut.R;
import com.il.appshortcut.views.ActivityDetailVo;

public class ActivityActionItemAdapter extends ArrayAdapter<ActivityDetailVo> {
	ActivityListListener mCallback;
	public interface ActivityListListener{
		public void itemSelected(ActivityDetailVo activity);
	}
	
	public void setCallback(ActivityListListener callback){
		this.mCallback = callback;
	}
	
	int resource;
	List<ActivityDetailVo> items;
	
	public ActivityActionItemAdapter(Context context, int resource,
			List<ActivityDetailVo> items) {
		super(context, resource, items);
		this.resource = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout activityListView;
		ActivityDetailVo item = getItem(position);
		if (convertView == null) {
			activityListView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, activityListView, true);
		} else {
			activityListView = (LinearLayout) convertView;
		}
		
		ImageView iconImage = (ImageView) activityListView.findViewById(R.id.activity_action_list_icon_image);
		iconImage.setImageDrawable(item.getIcon());
		
		final ActivityDetailVo itemParam = item;
		if (mCallback != null){
			OnClickListener listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.itemSelected(itemParam);
				}
			};
			activityListView.setOnClickListener(listener);
			iconImage.setOnClickListener(listener);
		}
		return activityListView;
	}
	
	public void setItems(List<ActivityDetailVo> _items){
		this.items = _items;
	}
}
