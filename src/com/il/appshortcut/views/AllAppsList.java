package com.il.appshortcut.views;

import java.util.ArrayList;

public class AllAppsList {
	public static final int DEFAULT_APPLICATIONS_NUMBER = 42;

	public ArrayList<ApplicationItem> data = new ArrayList<ApplicationItem>(
			DEFAULT_APPLICATIONS_NUMBER);

	public void add(ApplicationItem info) {
		if (findActivity(data, info.componentName)) {
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

	public ApplicationItem get(int index) {
		return data.get(index);
	}

	private static boolean findActivity(ArrayList<ApplicationItem> apps,
			String component) {
		final int N = apps.size();
		for (int i = 0; i < N; i++) {
			final ApplicationItem info = apps.get(i);
			if (info.componentName.equalsIgnoreCase(component)) {
				return true;
			}
		}
		return false;
	}

}
