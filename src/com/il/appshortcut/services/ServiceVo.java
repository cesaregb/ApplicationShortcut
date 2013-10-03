package com.il.appshortcut.services;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class ServiceVo {
	private String name;
	private String description; 
	private Drawable icon;
	private boolean selected;
	public int ID = 0;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	abstract public boolean run(Context context);
	
	@Override
	public boolean equals(Object o) {
		ServiceVo item = (ServiceVo) o;
		return (item.ID == this.ID);
	}
	
}



