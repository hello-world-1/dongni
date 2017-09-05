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

	// 退出应用时释放资源
	public void releaseResourece() {

	}

	/**
	 * 只有在第一次点击侧边栏的某个条目时会调用这个方法
	 * 
	 * @return 子view
	 */
	public abstract View initViews();

	/**
	 * 每次点击侧边栏条目会调用这个方法
	 */
	public void initData() {

	}
}
