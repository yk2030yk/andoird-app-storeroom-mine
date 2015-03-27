package com.example.mine;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class MainMemoActivity extends FragmentActivity implements ActionBar.TabListener {
	MemoFragmentPagerAdapter memoFragmentPagerAdapter;
	ViewPager mViewPager;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawer;

	static SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_memo_activity);

		// タブ部分の準備
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		memoFragmentPagerAdapter = new MemoFragmentPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(memoFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < memoFragmentPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(memoFragmentPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		// スライドで出てくるやつの準備
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {}

			@Override
			public void onDrawerOpened(View drawerView) {}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {}
		};
		mDrawer.setDrawerListener(mDrawerToggle);

	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {}

}
