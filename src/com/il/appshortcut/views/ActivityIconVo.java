package com.il.appshortcut.views;

import android.graphics.drawable.Drawable;

/**
 * @author Administrator
 * helper object to assign icons to activities.  
 */

public class ActivityIconVo {
	private int idIcon;
	private int idResource;
	private String name;
	private String description;
	private Drawable drawable;
	private boolean selected;
	
	public int getIdIcon() {
		return idIcon;
	}
	public void setIdIcon(int idIcon) {
		this.idIcon = idIcon;
	}
	public int getIdResource() {
		return idResource;
	}
	public void setIdResource(int idResource) {
		this.idResource = idResource;
	}
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
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
