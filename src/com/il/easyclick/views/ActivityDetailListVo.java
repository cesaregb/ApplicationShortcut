package com.il.easyclick.views;

import java.util.ArrayList;
import java.util.List;

import com.il.easyclick.dao.ActivitiesDAO;
import com.il.easyclick.services.ServiceVo;

public class ActivityDetailListVo {
	public List<ActivityDetailVo> data;

	public ActivityDetailListVo(){
		this.data = new ArrayList<ActivityDetailVo>();
	}
	public void addDetail(ActivityDetailVo dtl){
		if (this.data == null){
			this.data = new ArrayList<ActivityDetailVo>();
		}
		this.data.add(dtl);
	}
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
		List<ApplicationVo> returnList = null;
		if (this.data != null){
			returnList = new ArrayList<ApplicationVo>();
			for (ActivityDetailVo dtl : this.data){
				if (dtl.getType() == ActivitiesDAO.TYPE_ACTION){
					returnList.add(dtl.getApplication());
				}
			}
		}
		return returnList;
	}
	
	
	public List<ServiceVo> getListServices() {
		List<ServiceVo> returnList = null;
		if (this.data != null){
			returnList = new ArrayList<ServiceVo>();
			for (ActivityDetailVo dtl : this.data){
				if (dtl.getType() == ActivitiesDAO.TYPE_SERVICE){
					returnList.add(dtl.getService());
				}
			}
		}
		return returnList;
	}
	
	public void removeApplications() {
		List<ActivityDetailVo> helper = new ArrayList<ActivityDetailVo>();
		if (this.data != null)
			for (int i = 0; i < this.data.size(); i++) {
				if (this.data.get(i).getType() == ActivitiesDAO.TYPE_ACTION) {
					this.data.remove(i);
				}else
					helper.add(this.data.get(i));
			}
		this.data.clear();
		this.data.addAll(helper);
	}
	
	public void removeServices() {
		List<ActivityDetailVo> helper = new ArrayList<ActivityDetailVo>();
		if (this.data != null)
			for (int i = 0; i < this.data.size(); i++) {
				if (this.data.get(i).getType() == ActivitiesDAO.TYPE_SERVICE) {
					this.data.remove(i);
				}else
					helper.add(this.data.get(i));
			}
		this.data.clear();
		this.data.addAll(helper);
	}

	public int size(){
		if (this.data != null)
			return this.data.size();
		else
			return 0;
	}
	
	public void clear() {
		data.clear();
	}
}
