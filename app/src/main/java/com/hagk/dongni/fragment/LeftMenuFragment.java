package com.hagk.dongni.fragment;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hagk.dongni.activity.MainActivity;
import com.hagk.dongni.R;
import com.hagk.dongni.bean.UserInfo;
import com.hagk.dongni.bean.UserInfo.UserBean;
import com.hagk.dongni.http.UserObserver;
import com.hagk.dongni.http.UserService;
import com.hagk.dongni.lib.SlidingMenu;
import com.hagk.dongni.pager.IndexPager;
import com.hagk.dongni.utils.PrefUtils;

/**
 * 侧边栏
 */
public class LeftMenuFragment extends BaseFragment implements
		View.OnClickListener {
	private RelativeLayout friendRequest, locationRequest, locationHistory,
			other;// 中间的相对布局选项对象
	private LinearLayout personInfo;
	View view;

	private UserObserver observer;
	private UserService service;
	private UserBean userBean;

	ImageView icon;
	TextView nickname;
	TextView status;
	TextView describe;

	@Override
	public View initViews() {
		view = View.inflate(mActivity, R.layout.fragment_left, null);
		friendRequest = (RelativeLayout) view
				.findViewById(R.id.rl_friend_request);
		locationRequest = (RelativeLayout) view
				.findViewById(R.id.rl_location_request);
		locationHistory = (RelativeLayout) view
				.findViewById(R.id.rl_location_history);
		other = (RelativeLayout) view.findViewById(R.id.rl_other);
		// 设置监听事件
		friendRequest.setOnClickListener(this);
		locationRequest.setOnClickListener(this);
		locationHistory.setOnClickListener(this);
		other.setOnClickListener(this);
		personInfo.setOnClickListener(this);

		icon = (ImageView) view.findViewById(R.id.icon);
		return view;
	}

	// 获取用户的头像,描述信息,nickname等信息
	@Override
	public void initData() {
		// 获取网络请求接口
//		service = UserService.getUserService();
//		observer = service.getObserver();
//		getJsonResult();// 请求网络数据
	}

	private void getJsonResult() {
		String username = PrefUtils.getUsername(mActivity, "username");
		Observable<UserInfo> observable = observer.getResult(username);
		observable.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<UserInfo>() {
					@Override
					public void onCompleted() {
						Log.v("contactpager", "数据加载完成");
					}

					@Override
					public void onError(Throwable e) {
						Toast.makeText(mActivity, "请检查您的网络", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onNext(UserInfo userInfo) {
						userBean = userInfo.getUserinfo();
						Glide.with(mActivity).load(userBean.getUserimage())
						// .override(dip2px(100), dip2px(70)) // 重新绘制图片大小
								.override(dip2px(44), dip2px(44)) // 图片大小设置为这个像素会加载错误时的图片
								.placeholder(R.mipmap.icon) // 默认图片和加载错误的图片
								.error(R.mipmap.icon).into(icon);
						System.out.println("nickname:" + userBean.getNickname());
						nickname.setText(userBean.getNickname());
						status.setText(userBean.getStatus());
						describe.setText(userBean.getDescribe());
					}
				});
	}

	/**
	 * dp转px
	 */
	public int dip2px(int dip) {
		float scale = mActivity.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_friend_request:
			setMenuPager(0);
			break;
		case R.id.rl_location_request:
			break;
		case R.id.rl_location_history:
			break;
		case R.id.rl_other:
			break;
		case R.id.icon:
			setMenuPager(0);
			break;
		}
	}

	/**
	 * 处理munepager事件，显示指定的页面，关闭侧边栏
	 */
	public void setMenuPager(int pos) {
		setCurrentMenuPager(pos);
		toggleSlidingMenu();
	}

	/**
	 * 设置当前菜单的页签详情页
	 */
	private void setCurrentMenuPager(int pos) {
		MainActivity main = (MainActivity) mActivity;
		ContentFragment content = main.getContentFragment();

		ViewPager viewPager = content.getmViewPager();

		if (viewPager != null) {
			viewPager.setCurrentItem(0, false);
		}

		IndexPager pager = content.getIndexPager();
		pager.setCurrentMenuPager(pos);

	}
	/**
	 * 关闭侧边栏
	 */
	public void toggleSlidingMenu() {
		MainActivity main = (MainActivity) mActivity;
		SlidingMenu slidemenu = main.getSlidingMenu();
		slidemenu.toggle();// 切换状态，显示隐藏，隐藏显示
	}

}
