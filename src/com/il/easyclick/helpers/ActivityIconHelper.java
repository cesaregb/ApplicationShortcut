package com.il.easyclick.helpers;

import java.util.ArrayList;
import java.util.List;

import com.il.easyclick.R;
import com.il.easyclick.views.EventIconVo;

public class ActivityIconHelper {
	
	private static int[] images = { 
		R.drawable.bed2,
		R.drawable.car1,
		R.drawable.clock37,
		R.drawable.cocktail6,
		R.drawable.coffee18,
		R.drawable.couple8,
		R.drawable.cycling,
		R.drawable.fishing2,
		R.drawable.group12,
		R.drawable.painting14,
		R.drawable.person169,
		R.drawable.read1,
		R.drawable.restaurant2,
		R.drawable.running1,
		R.drawable.walking3
	};
	
	private static List<EventIconVo> icons;
	
	static {
		icons = new ArrayList<EventIconVo>();
		for (int i = 0; i < images.length; i++){
			EventIconVo item = new EventIconVo();
			item.setIdIcon(i+1);
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
		return (idIcon > 0) ?getIconById(idIcon).getIdResource():0;
	}
}
