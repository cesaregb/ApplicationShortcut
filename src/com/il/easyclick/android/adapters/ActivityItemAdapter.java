package com.il.easyclick.android.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.il.easyclick.R;
import com.il.easyclick.helpers.ActivityIconHelper;
import com.il.easyclick.views.ActivityVo;

public class ActivityItemAdapter extends ArrayAdapter<ActivityVo> {

	ActivityListListener mCallback;
	public interface ActivityListListener{ }
	
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
		int idResource = ActivityIconHelper.getDrawableResource(item.getIdIcon());
		Drawable icon = null;
		if (idResource == 0){
			idResource = R.drawable.easy_click_logo;
		}
		icon = getContext().getResources().getDrawable(idResource);
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
		ImageView activityIcon = (ImageView) activityListView.findViewById(R.id.activity_icon_item);
		
		if (icon != null){
			activityIcon.setImageDrawable(icon);
		}
		descriptionText.setText(activityDescription);
		nameText.setText(activityName);
		if (mCallback != null){ }
		
		
		return activityListView;
	}
	
	public void setItems(List<ActivityVo> _items){
		this.items = _items;
	}
	
}