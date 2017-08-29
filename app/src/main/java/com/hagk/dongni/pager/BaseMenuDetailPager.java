package com.hagk.dongni.pager;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {
	public Activity mActivity;
	public View mRootView;

	public BaseMenuDetailPager(Activity activity) {
		this.mActivity = activity;
		mRootView = initViews();// 把子类视图给mrootview
	}

	/**
	 * 初始化界面
	 * 
	 * @return 子view
	 */
	public abstract View initViews();

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
