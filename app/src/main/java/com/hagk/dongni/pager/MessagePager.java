package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.RegistActivity;

public class MessagePager extends BaseMenuDetailPager {

	public MessagePager(Activity activity) {
		super(activity);
	}

	Button register;
	

	@Override
	public View initViews() {
		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.friend_listview, null);// 找到listview所在的布局
		register = (Button) view.findViewById(R.id.register_tv);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.register_tv:
					Intent intent = new Intent(mActivity, RegistActivity.class);
					mActivity.startActivity(intent);
					break;

				default:
					
					break;
				}
				
			}
		});
		return view;
	}

	@Override
	public void initData() {
	}
}
