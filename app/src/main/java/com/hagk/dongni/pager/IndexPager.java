package com.hagk.dongni.pager;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;

import com.hagk.dongni.R;

public class IndexPager extends BasePager {

	public IndexPager(Activity activity) {
		super(activity);
	}

	private ArrayList<BaseMenuDetailPager> pagerList;

	@Override
	public void initData() {
		setSlidingMenuEnable(true);// 关闭侧边栏
		flContent.removeAllViews();// 清除先前的绘图
		addMenuPager();// 添加侧栏内容
		// 定义listview
		View v = View.inflate(mActivity, R.layout.index_pager, null);// 找到listview所在的布局
		flContent.addView(v);// 给framelayout添加视图
	}

	/**
	 * 为pagerlist添加MenuPager
	 */
	public void addMenuPager() {
		pagerList = new ArrayList<>();
		pagerList.add(new FriendRequestPager(mActivity));
//		pagerList.add(new LocationRequestPager(mActivity));
//		pagerList.add(new LocationHistoryPager(mActivity));
//		pagerList.add(new LeftContactPager(mActivity));
//		pagerList.add(new PersonInfoPager(mActivity));
	}

	public void setCurrentMenuPager(int position) {
		BaseMenuDetailPager menuPsger = pagerList.get(position);
		flContent.removeAllViews();// 清除baserpager里面framelayout的布局（中间显示的部分）
		flContent.addView(menuPsger.mRootView);
		menuPsger.initData();// 初始化数据
	}
}
