package com.il.appshortcut.helpers;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Config;

import com.il.appshortcut.R;
import com.il.appshortcut.views.EventIconVo;

public class ActivityIconHelper {
	
	private static int[] images = { R.drawable.a, R.drawable.b, R.drawable.c,
			R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
			R.drawable.d, R.drawable.e, R.drawable.f, R.drawable.g,
			R.drawable.h, R.drawable.i, R.drawable.j, R.drawable.k };
	
	private static List<EventIconVo> icons;
	
	static {
		icons = new ArrayList<EventIconVo>();
		for (int i = 0; i < images.length; i++){
			EventIconVo item = new EventIconVo();
			item.setIdIcon(i);
			item.setIdResource(images[i]);
			item.setName(String.valueOf(i));
			item.setDescription(String.valueOf(i));
			icons.add(item);
		}
	}
	
	public static EventIconVo getIconById(int id){
		EventIconVo result = null;
		for (EventIconVo item : icons){
			if (item.getIdIcon() == id){
				result = item;
			}
		}
		return result;
	}

	public static List<EventIconVo> getIcons() {
		return icons;
	}
	
	public static int getDrawableResource(int idIcon){
		return getIconById(idIcon).getIdResource();
	}
	

}
