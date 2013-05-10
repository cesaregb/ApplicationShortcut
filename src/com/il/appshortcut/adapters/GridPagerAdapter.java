package com.il.appshortcut.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.il.appshortcut.fragments.ApplicationListFragment;

public class GridPagerAdapter extends FragmentPagerAdapter {

	private int mViews = 0;

	public int getmViews() {
		return mViews;
	}

	public void setmViews(int mViews) {
		this.mViews = mViews;
	}

	public GridPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return new ApplicationListFragment();
		default:
			// The other sections of the app are dummy placeholders.
			// Fragment fragment = new DummySectionFragment();
			// Bundle args = new Bundle();
			// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
			// fragment.setArguments(args);
			// return fragment;
			return new ApplicationListFragment();
		}
	}

	@Override
	public int getCount() {
		return mViews;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Section " + (position + 1);
	}
}
