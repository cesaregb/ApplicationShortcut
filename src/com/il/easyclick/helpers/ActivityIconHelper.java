package com.il.easyclick.helpers;

import java.util.ArrayList;
import java.util.List;

import com.il.easyclick.R;
import com.il.easyclick.views.EventIconVo;

public class ActivityIconHelper {
	
	private static int[] images = { 
		R.drawable.bed2,
		R.drawable.chat27,
		R.drawable.chess3,
		R.drawable.chicago,
		R.drawable.circular51,
		R.drawable.climb,
		R.drawable.clock37,
		R.drawable.cocktail6,
		R.drawable.coffee18,
		R.drawable.constructor1,
		R.drawable.cooker3,
		R.drawable.couple8,
		R.drawable.cycling,
		R.drawable.cycling1,
		R.drawable.dark5,
		R.drawable.dj1,
		R.drawable.fishing2,
		R.drawable.group12,
		R.drawable.guitar1,
		R.drawable.mini2,
		R.drawable.paint16,
		R.drawable.painting14,
		R.drawable.person169,
		R.drawable.person91,
		R.drawable.read1,
		R.drawable.restaurant2,
		R.drawable.running3,
		R.drawable.small135,
		R.drawable.standing23,
		R.drawable.swimmer1,
		R.drawable.walking3
			};
	
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
