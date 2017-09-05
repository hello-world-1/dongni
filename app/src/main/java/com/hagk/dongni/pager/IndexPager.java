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
		View v = View.inflate(mActivity, R.layout.index_pager, null);// 找到首页所在的布局
		flContent.addView(v);// 给framelayout添加视图
	}

	/**
	 * 为pagerlist添加MenuPager
	 */
	public void addMenuPager() {
		pagerList = new ArrayList<>();
		pagerList.add(new MessagePager(mActivity));
		pagerList.add(new AlertPager(mActivity));
		pagerList.add(new PHQ9Pager(mActivity));
		pagerList.add(new CoursePager(mActivity));
		pagerList.add(new ContactPager(mActivity));
		pagerList.add(new SettingPager(mActivity));
		pagerList.add(new InfoPager(mActivity));
	}

	public void setCurrentMenuPager(int position) {
		BaseMenuDetailPager menuPsger = pagerList.get(position);
		flContent.removeAllViews();// 清除baserpager里面framelayout的布局（中间显示的部分）
		//先初始化数据
		flContent.addView(menuPsger.mRootView);//initView
		menuPsger.initData();// 初始化数据
	}

	// 退出应用时释放资源
	public void releaseResourece() {
		for(BaseMenuDetailPager pager : pagerList){
			pager.releaseResourece();
		}
	}
}
