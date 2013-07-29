package com.il.appshortcut.views;

import java.util.List;


public class ActivityVo {
	
	private int idActivity;
	private String name;
	private String description;
	private List<ActionVo> actions;
	
	public int getIdActivity() {
		return idActivity;
	}
	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
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
	public List<ActionVo> getActions() {
		return actions;
	}
	public void setActions(List<ActionVo> actions) {
		this.actions = actions;
	}
	
}
