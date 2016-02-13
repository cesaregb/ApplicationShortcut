package com.il.easyclick.android.adapters;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.il.easyclick.R;
import com.il.easyclick.dao.AppshortcutDAO;
import com.il.easyclick.helpers.ActivityIconHelper;
import com.il.easyclick.views.ActionVo;
import com.il.easyclick.views.ActivityVo;
import com.il.easyclick.views.EventWrapper;

/**
 * @author Cesaregb
 * This adapter is for the action list (open, new message, etc... )
 *
 */

public class EventItemAdapter extends ArrayAdapter<EventWrapper> {
	int resource;
	
	public EventItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.resource = textViewResourceId;
	}
	
	public EventItemAdapter(Context context,
			int textViewResourceId, List<EventWrapper> items) {
		
		super(context, textViewResourceId, items);
		this.resource = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout eventView;
		EventWrapper item = getItem(position);
		String taskString = "";
		ActionVo action = null;
		ActivityVo activity = null;
		Drawable eventIcon = getContext().getResources().getDrawable(R.drawable.ic_launcher);
		if (item.getType() == AppshortcutDAO.TYPE_ACTION){
			action = (ActionVo) item.getObject();
			try{
				ApplicationInfo app = getContext().getPackageManager()
						.getApplicationInfo(action.getParentPackage(), 0);        
				eventIcon = getContext().getPackageManager().getApplicationIcon(app);
			}catch(Exception e ){
				//TODO no icon 
			}
			taskString = action.getActionName();
		}else if (item.getType() ==  AppshortcutDAO.TYPE_ACTIVITY){
			activity = (ActivityVo) item.getObject();
			taskString = activity.getName();
			eventIcon = getContext().getResources().getDrawable(ActivityIconHelper.getDrawableResource(activity.getIdIcon()));
		}
		if (convertView == null) {
			eventView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(resource, eventView, true);
		} else {
			eventView = (LinearLayout) convertView;
		}
		TextView taskView = (TextView) eventView.findViewById(R.id.title);
		ImageView imageView = (ImageView) eventView.findViewById(R.id.item_image);
		imageView.setImageDrawable(eventIcon);
		taskView.setText(taskString);
		return eventView;
	}

}
