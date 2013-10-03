package com.il.appshortcut.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.il.appshortcut.R;
import com.il.appshortcut.views.ActivityVo;

public class ActivityItemAdapter extends ArrayAdapter<ActivityVo> {

	ActivityListListener mCallback;
	public interface ActivityListListener{
	}
	
	public void setCallback(ActivityListListener callback){
		this.mCallback = callback;
	}
	
	int resource;
	List<ActivityVo> items;
	
	public ActivityItemAdapter(Context context, int resource,
			List<ActivityVo> items) {
		super(context, resource, items);
		this.resource = resource;
	}
	
	@Override
    public int getCount() {
        return (items != null) ? items.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getIdActivity();
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout activityListView;
		ActivityVo item = getItem(position);
		String activityName = item.getName();
		String activityDescription = item.getDescription();
		if (convertView == null) {
			activityListView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, activityListView, true);
		} else {
			activityListView = (LinearLayout) convertView;
		}

		TextView nameText = (TextView) activityListView.findViewById(R.id.activityRowName);
		TextView descriptionText = (TextView) activityListView.findViewById(R.id.activityRowDescription);

		descriptionText.setText(activityDescription);
		nameText.setText(activityName);
		if (mCallback != null){ }
		
		return activityListView;
	}
	
	public void setItems(List<ActivityVo> _items){
		this.items = _items;
	}
	
}