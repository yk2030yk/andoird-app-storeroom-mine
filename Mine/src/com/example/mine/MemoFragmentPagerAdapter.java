package com.example.mine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MemoFragmentPagerAdapter extends FragmentStatePagerAdapter {
	int fragmentCount = 2;

	public MemoFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			return new MemoFragment();
		default:
			return new ExtraFragment();
		}
	}

	@Override
	public int getCount() {
		return fragmentCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String titles[] = { "chat", "extra" };
		String title;
		if (position < fragmentCount) {
			title = titles[position];
		} else {
			title = titles[titles.length - 1];
		}
		return title;
	}
}