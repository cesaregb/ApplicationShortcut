package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import com.il.appshortcut.R;
import com.il.appshortcut.views.ActivityIconVo;

public class ActivityIconHelper {
	
	private static int[] images = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
			R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
			R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k };
	
	private static List<ActivityIconVo> icons;
	
	static {
		icons = new ArrayList<ActivityIconVo>();
		for (int i = 0; i < images.length; i++){
			ActivityIconVo item = new ActivityIconVo();
			item.setIdIcon(i);
			item.setIdResource(images[i]);
			item.setName(String.valueOf(i));
			item.setDescription(String.valueOf(i));
			icons.add(item);
		}
	}
	
	public static ActivityIconVo getIconById(int id){
		ActivityIconVo result = null;
		for (ActivityIconVo item : icons){
			if (item.getIdIcon() == id){
				result = item;
			}
		}
		return result;
	}

	public static List<ActivityIconVo> getIcons() {
		return icons;
	}
	
	public static int getDrawable(int idIcon){
		return getIconById(idIcon).getIdResource();
	}

}
