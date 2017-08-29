package com.hagk.dongni.fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.hagk.dongni.R;
import com.hagk.dongni.pager.BasePager;
import com.hagk.dongni.pager.IndexPager;

public class ContentFragment extends BaseFragment {

	private ViewPager mViewPager;
	
	public ViewPager getmViewPager() {
		return mViewPager;
	}

	private ArrayList<BasePager> mPagerList;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_content);
		return view;
	}

	@Override
	public void initData() {
		mPagerList = new ArrayList<>();
		mPagerList.add(new IndexPager(mActivity));
		mViewPager.setAdapter(new ContentAdapter());
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {

					}

					@Override
					public void onPageSelected(int position) {
						mPagerList.get(position).initData();// 当前页面加载数据
					}

					@Override
					public void onPageScrollStateChanged(int state) {

					}
				});

		mPagerList.get(0).initData();// 默认主页为第一个
	}
	
	public IndexPager getIndexPager() {
		IndexPager pager = (IndexPager) mPagerList.get(0);
		return pager;
	}
	
	// 为viewpager写适配器
	class ContentAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			// pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 设置当前菜单页面
	 */
	public void setCurrentPage() {
		mViewPager.setCurrentItem(0, false);
	}
}
