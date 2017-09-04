package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.ChangePasswordActivity;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.HashMap;
import java.util.Map;

public class SettingPager extends BaseMenuDetailPager implements TopBarView.onTitleBarClickListener,OnClickListener {

	public SettingPager(Activity activity) {
		super(activity);
	}

	TopBarView title;
	TextView exit;
	TextView repassword;

	@Override
	public void onClick(View view){
		switch (view.getId()) {
			case R.id.tv_exit:
				exit();
				break;
			case R.id.tv_repassword:
				Intent intent = new Intent(mActivity, ChangePasswordActivity.class);
                intent.putExtra("type","repassword");
				mActivity.startActivity(intent);
				break;
			default:
				break;
		}
	}

	public void exit(){
		Map<String, Object> params = new HashMap<>();
		params.put("userID", PrefUtils.getUserID(mActivity));
		params.put("token", PrefUtils.getToken(mActivity));

		MyHttpUtils.build()//构建myhttputils
				.url(ConstantValue.BASE_URL+"/api/user/logout")//请求的url
				.addParams(params)
				.onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
					@Override
					public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
						JsonParser parse = new JsonParser();
						try {
							JsonObject json = (JsonObject) parse.parse(result);
							String status = json.get("status").getAsString();
							if (ConstantValue.SUCCESS_STATUS.equals(status)) {
								//登录成功,把用户数据保存到数据库中
								JsonObject user = json.get("user").getAsJsonObject();
								String userID = user.get("userID").getAsString();
								String token = user.get("token").getAsString();
								//保存用户的ID
								PrefUtils.setUserID(mActivity, userID);
								//保存用户的token
								PrefUtils.setToken(mActivity, token);

								Toast.makeText(mActivity,"注销成功",Toast.LENGTH_SHORT).show();
							} else if (ConstantValue.ERROR_STATUS.equals(status)) {
								//error
								int errcode = json.get("errcode").getAsInt();
								if (3 == errcode) { //手机号未注册
									Toast.makeText(mActivity, "该手机号未注册", Toast.LENGTH_SHORT).show();
								} else if (1 == errcode) {
									Toast.makeText(mActivity, "数据库查询出错", Toast.LENGTH_SHORT).show();
								}else if (2 == errcode) {
									Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
								}else if (0 == errcode) {
									Toast.makeText(mActivity, "userID或token为空", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(mActivity, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
					}
				});
	}

	@Override
	public void onBackClick() {
		Toast.makeText(mActivity, "你点击了左侧按钮", Toast.LENGTH_LONG).show();
	}


	@Override
	public View initViews() {
		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.setting_pager, null);// 找到listview所在的布局
		title = (TopBarView) view.findViewById(R.id.topbar);
		title.setClickListener(this);
		exit = (TextView) view.findViewById(R.id.tv_exit);
		repassword = (TextView) view.findViewById(R.id.tv_repassword);
		exit.setOnClickListener(this);
		repassword.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
	}
}
