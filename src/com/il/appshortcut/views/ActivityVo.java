package com.il.appshortcut.views;

import java.util.List;

public class ActivityVo {
	
	private int idActivity;
	private String name;
	private String description;
	private String pattern;
	private boolean assigned;
	private List<ActionVo> actions;
	
	public ActivityVo(){}
	
	public ActivityVo(String _name){
		this.name = _name;
	}
	
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
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public boolean isAssigned() {
		return assigned;
	}
	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
	@Override
	public String toString(){
		return this.name + " -- " + this.description;
	}
	
}
