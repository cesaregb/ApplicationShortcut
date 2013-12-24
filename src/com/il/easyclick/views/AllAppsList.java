package com.il.easyclick.views;

import java.util.ArrayList;

import android.content.ComponentName;

public class AllAppsList {
	public static final int DEFAULT_APPLICATIONS_NUMBER = 42;

	public ArrayList<ApplicationVo> data = new ArrayList<ApplicationVo>(
			DEFAULT_APPLICATIONS_NUMBER);

	public void add(ApplicationVo info) {
		if (findActivity(data, info.getComponentName())) {
			return;
		}
		data.add(info);
	}

	public void clear() {
		data.clear();
	}

	public int size() {
		return data.size();
	}

	public ApplicationVo get(int index) {
		return data.get(index);
	}

	private static boolean findActivity(ArrayList<ApplicationVo> apps,
			ComponentName component) {
		final int N = apps.size();
		for (int i = 0; i < N; i++) {
			final ApplicationVo info = apps.get(i);
			if (info.getComponentName().equals(component)) {
				return true;
			}
		}
		return false;
	}

}
