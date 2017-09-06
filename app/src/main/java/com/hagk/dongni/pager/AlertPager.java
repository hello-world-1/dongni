package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.AddQuestionActivity;
import com.hagk.dongni.activity.BaiduLocation;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;

public class AlertPager extends BaseMenuDetailPager {

	public AlertPager(Activity activity) {
		super(activity);
	}

	TextView register;
	

	@Override
	public View initViews() {
		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.alert_pager, null);// 找到listview所在的布局
		register = (TextView) view.findViewById(R.id.tv_alert);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_alert:
					Intent intent = new Intent(mActivity, AddQuestionActivity.class);
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
