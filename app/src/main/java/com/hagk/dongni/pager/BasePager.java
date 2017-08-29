package com.hagk.dongni.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.hagk.dongni.activity.MainActivity;
import com.hagk.dongni.R;
import com.hagk.dongni.lib.SlidingMenu;

/**
 * 主页下消息、联系人、我子页面的基类
 * 
 */
public class BasePager {

	public Activity mActivity;

	public View mRootView; // 布局对象

	public FrameLayout flContent; // 内容

	public BasePager(Activity activity) {
		mActivity = activity;
		initViews();
	}

	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_cc);
	}

	/**
	 * 切换SlidingMenu的状态,显示时隐藏, 隐藏时显示
	 */
	protected void toggleSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		// 切换状态, 显示时隐藏, 隐藏时显示
		slidingMenu.toggle();
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
	}
	
	/**
	 * 设置侧边栏是否可用
	 */
	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUi = (MainActivity) mActivity;

		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
