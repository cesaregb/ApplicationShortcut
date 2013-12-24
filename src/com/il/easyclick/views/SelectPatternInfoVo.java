package com.il.easyclick.views;

import android.graphics.drawable.Drawable;

public class SelectPatternInfoVo {
	private Drawable icon;
	private String name;
	private String description;
	private String pattern;
	private int type;
	public static final int TYPE_APPLICATAION = 1;
	public static final int TYPE_ACTIVITY = 1;
	
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
