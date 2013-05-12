package com.il.appshortcut.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.il.appshortcut.fragments.ApplicationListFragment;

public class GridPagerAdapter extends FragmentPagerAdapter {
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
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

	
	
//	public static class ArrayListFragment extends ListFragment {
//        int mNum;
//
//        static ArrayListFragment newInstance(int num) {
//            ArrayListFragment f = new ArrayListFragment();
//            Bundle args = new Bundle();
//            args.putInt("num", num);
//            f.setArguments(args);
//            return f;
//        }
//
//        /**
//         * When creating, retrieve this instance's number from its arguments.
//         */
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
//        }
//
//        /**
//         * The Fragment's UI is just a simple text view showing its
//         * instance number.
//         */
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//        	
//        	Fragment fragment = new ApplicationListFragment();
//    		Bundle args = new Bundle();
//    		args.putInt(ApplicationListFragment.ARG_SECTION_NUMBER, i);
//    		fragment.setArguments(args);
//    		fragment.getView();
//        	
////            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
////            View tv = v.findViewById(R.id.text);
////            ((TextView)tv).setText("Fragment #" + mNum);
//            return fragment.getView();;
//        }
//
//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            super.onActivityCreated(savedInstanceState);
//            setListAdapter(new ArrayAdapter<String>(getActivity(),
//                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
//        }
//
//        @Override
//        public void onListItemClick(ListView l, View v, int position, long id) {
//            Log.i("FragmentList", "Item clicked: " + id);
//        }
//    }
	
	
}
