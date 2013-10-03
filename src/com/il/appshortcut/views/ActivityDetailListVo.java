package com.il.appshortcut.views;

import java.util.ArrayList;
import java.util.List;

import com.il.appshortcut.dao.ActivitiesDAO;
import com.il.appshortcut.services.ServiceVo;

public class ActivityDetailListVo {
	public List<ActivityDetailVo> data = new ArrayList<ActivityDetailVo>();

	public List<ActivityDetailVo> getListDetails() {
		return data;
	}

	public void setListDetails(List<ActivityDetailVo> listDetails) {
		this.data = listDetails;
	}
	
	public List<ActivityDetailVo> getListDetails(int type) {
		List<ActivityDetailVo> listDetailsTmp = new ArrayList<ActivityDetailVo>();
		for (ActivityDetailVo dtl : this.data){
			if (dtl.getType() == type){
				listDetailsTmp.add(dtl);
			}
		}
		return listDetailsTmp;
	}
	
	public List<ApplicationVo> getListApplications() {
		List<ApplicationVo> returnList = new ArrayList<ApplicationVo>();
		for (ActivityDetailVo dtl : this.data){
			if (dtl.getType() == ActivitiesDAO.TYPE_ACTION){
				returnList.add(dtl.getApplication());
			}
		}
		return returnList;
	}
	
	public List<ServiceVo> getListServices() {
		List<ServiceVo> returnList = new ArrayList<ServiceVo>();
		for (ActivityDetailVo dtl : this.data){
			if (dtl.getType() == ActivitiesDAO.TYPE_SERVICE){
				returnList.add(dtl.getService());
			}
		}
		return returnList;
	}
	
	public int size(){
		return this.data.size();
	}
	
	public void clear() {
		data.clear();
	}
}
