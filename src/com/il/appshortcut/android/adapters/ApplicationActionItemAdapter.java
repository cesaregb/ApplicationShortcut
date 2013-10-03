package com.il.appshortcut.android.adapters;

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

import com.il.appshortcut.R;
import com.il.appshortcut.views.ActionVo;

/**
 * @author Cesaregb
 * This adapter is for the action list (open, new message, etc... )
 *
 */

public class ApplicationActionItemAdapter extends ArrayAdapter<ActionVo>{
	int resource; 
	
	
	public ApplicationActionItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.resource = textViewResourceId;
	}
	
	public ApplicationActionItemAdapter(Context context,
			int textViewResourceId, List<ActionVo> items) {
		
		super(context, textViewResourceId, items);
		this.resource = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout applicationActionView;
		ActionVo item = getItem(position);
		String taskString = item.getActionName();
		if (convertView == null) {
			applicationActionView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(resource, applicationActionView, true);
		} else {
			applicationActionView = (LinearLayout) convertView;
		}
		TextView taskView = (TextView)applicationActionView.findViewById(R.id.title);
		ImageView imageView = (ImageView) applicationActionView.findViewById(R.id.item_image);
		if (item.isAssigned()){
			Drawable navigationAccept = getContext().getResources().getDrawable(R.drawable.navigation_accept);
			imageView.setImageDrawable(navigationAccept);
		}
		taskView.setText(taskString);
		return applicationActionView;
	}

}
