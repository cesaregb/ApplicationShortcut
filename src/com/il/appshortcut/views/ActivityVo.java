package com.il.appshortcut.views;

import java.util.List;

public class ActivityVo {
	
	private String name;
	private String description;
	private List<ActivityDetailVo> details;
	
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
	public List<ActivityDetailVo> getDetails() {
		return details;
	}
	public void setDetails(List<ActivityDetailVo> details) {
		this.details = details;
	}
	
	
}
