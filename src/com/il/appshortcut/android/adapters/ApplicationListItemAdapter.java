package com.il.appshortcut.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.il.appshortcut.R;
import com.il.appshortcut.android.views.ApplicationListItemView;
import com.il.appshortcut.views.ApplicationVo;

/**
 * @author Cesaregb this adapter is for the application list (grid)
 */

public class ApplicationListItemAdapter extends ArrayAdapter<ApplicationVo> {
	int resource;

	public ApplicationListItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.resource = textViewResourceId;
	}

	public ApplicationListItemAdapter(Context context, int textViewResourceId,
			List<ApplicationVo> items) {
		super(context, textViewResourceId, items);
		this.resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout view;
		ApplicationVo item;
		item = getItem(position);
		String applicationName = item.getName();
		if (convertView == null) {
			view = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(resource, view, true);
		} else {
			view = (LinearLayout) convertView;
		}
		ApplicationListItemView applicationView = (ApplicationListItemView) view
				.findViewById(R.id.row);
		applicationView.setText(applicationName);

		ImageView iconImage = (ImageView) view.findViewById(R.id.icon_image);
		iconImage.setImageDrawable(item.getIcon());

		return view;
	}

}
