package com.il.appshortcut.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.il.appshortcut.android.fragments.ApplicationListFragment;

//public class GridPagerAdapter extends FragmentPagerAdapter {
public class GridPagerAdapter extends FragmentStatePagerAdapter {

	@Override
	public int getItemPosition(Object object) {
		return FragmentStatePagerAdapter.POSITION_NONE;
//		return super.getItemPosition(object);
	}

	private int mViews = 0;
	public void setmViews(int mViews) {
		this.mViews = mViews;
	}

	public GridPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public GridPagerAdapter(FragmentManager fm, int mViews) {
		this(fm);
		this.mViews = mViews;
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new ApplicationListFragment();
		Bundle args = new Bundle();
		args.putInt(ApplicationListFragment.ARG_SECTION_NUMBER, i);
		fragment.setArguments(args);
		return fragment;
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
