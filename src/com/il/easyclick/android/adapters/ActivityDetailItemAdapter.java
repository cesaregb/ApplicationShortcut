package com.il.easyclick.android.adapters;

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

import com.il.easyclick.R;
import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.views.ActivityDetailVo;

public class ActivityDetailItemAdapter extends ArrayAdapter<ActivityDetailVo> {
	ActivityListListener mCallback;
	public interface ActivityListListener{
		public void itemSelected(ActivityDetailVo activity);
	}
	
	public void setCallback(ActivityListListener callback){
		this.mCallback = callback;
	}
	
	int resource;
	List<ActivityDetailVo> items;
	
	public ActivityDetailItemAdapter(Context context, int resource,
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
		
		ImageView iconImage = (ImageView) activityListView.findViewById(R.id.simple_list_item_icon);
		TextView text = (TextView) activityListView.findViewById(R.id.simple_list_item_text);
		iconImage.setImageDrawable(item.getIcon());
		
		if (item.getType() == ActivitiesDAO.TYPE_ACTION){
			if (item.getApplication()!=null && item.getApplication().getName() != null)
				text.setText(item.getApplication().getName());
			else
				text.setText("Application not found");
		}else if (item.getType() == ActivitiesDAO.TYPE_SERVICE){
			if (item.getService() != null && item.getService().getName() != null)
				text.setText(item.getService().getName());
			else
				text.setText("Error getting the service");
		}
		
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

	@Override
	public ActivityDetailVo getItem(int position) {
		
		return super.getItem(position);
	}
}
