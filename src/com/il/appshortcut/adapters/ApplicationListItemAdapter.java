package com.il.appshortcut.adapters;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.il.appshortcut.R;
import com.il.appshortcut.android.views.ApplicationListItemView;
import com.il.appshortcut.views.ApplicationItem;

/**
 * @author Cesaregb this adapter is for the application list (grid)
 */

public class ApplicationListItemAdapter extends ArrayAdapter<ApplicationItem> {
	int resource;

	public ApplicationListItemAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.resource = textViewResourceId;
	}

	public ApplicationListItemAdapter(Context context, int textViewResourceId,
			List<ApplicationItem> items) {
		super(context, textViewResourceId, items);
		this.resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout view;
		ApplicationItem item;
		item = getItem(position);
		String applicationName = item.getApplicationName();
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

		ApplicationInfo appInfo = item.getApplicationInfo();
		Drawable icon = appInfo.loadIcon(getContext().getPackageManager());
		Bitmap bmpIcon = ((BitmapDrawable) icon).getBitmap();

		iconImage.setImageBitmap(bmpIcon);
		return view;
	}

}
