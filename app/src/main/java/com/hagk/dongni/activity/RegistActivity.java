package com.hagk.dongni.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.HttpPostUtils;
import com.hagk.dongni.utils.OthersUtils;

public class RegistActivity extends Activity {
	EditText username;
	EditText password;
	EditText repassword;
	EditText authode;
	Button regist;
	String type;
	Handler handler;
	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regist_activity);
		username = (EditText) findViewById(R.id.et_regist_username);
		authode = (EditText) findViewById(R.id.et_regist_authcode);
		password = (EditText) findViewById(R.id.et_regist_password);
		repassword = (EditText) findViewById(R.id.et_regist_re_password);
		regist = (Button) findViewById(R.id.btn_regist);

//		Intent intent = getIntent();
//		type = intent.getStringExtra("type");
//
//		// 根据从注册界面点击的按钮不同,注册按钮显示的值不同
//		if ("forget".equals(type)) {
//			regist.setText("重置密码");
//		} else if ("regist".equals(regist)) {
//			regist.setText("注册");
//		}
	}

	public void getCode(View view) {
		if (TextUtils.isEmpty(username.getText().toString().trim())) {
			Toast.makeText(RegistActivity.this, "手机号输入不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (!OthersUtils.isMobileNO(username.getText().toString().trim())) {
			Toast.makeText(RegistActivity.this, "手机号输入格式不正确",
					Toast.LENGTH_SHORT).show();
			username.setText("");
			return;
		}
		sendCode(username.getText().toString().trim());
	}

	public void sendCode(final String phoneNumber) {

		ConstantValue.cachedThreadPool.execute(new Runnable() {
			@Override
			public void run() {

				Map<String, Object> requestParamsMap = new HashMap<String, Object>();
				 requestParamsMap.put("phoneNumber", phoneNumber);
				requestParamsMap.put("type", type);

				Map<String, Object> retValue = HttpPostUtils.doPost(
						ConstantValue.BASE_URL
								+ "/androidlocation/RegistServlet",
						requestParamsMap);

				int code = (int) retValue.get("code");

				if (code == 200) {
					String value = (String) retValue.get("value");
					Message msg = new Message();
					msg.obj = value;
					msg.what = SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}
			}
		});
	}

	public void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
//					String result = (String) msg.obj;
					Toast.makeText(RegistActivity.this, "發送驗證碼成功", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}

	// 注册或者重置密码点击事件
	public void regist(View view) {
		/*final String str_username = username.getText().toString();
		final String str_password = password.getText().toString();
		String str_repassword = repassword.getText().toString();

		if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)
				|| TextUtils.isEmpty(str_repassword)) {
			Toast.makeText(RegistActivity.this, "输入值不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (!OthersUtils.isMobileNO(str_username)) {
			Toast.makeText(RegistActivity.this, "手机号输入格式不正确",
					Toast.LENGTH_SHORT).show();
			username.setText("");
			return;
		}

		if (!str_password.equals(str_repassword)) {
			password.setText("");
			repassword.setText("");
			Toast.makeText(RegistActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT)
					.show();
			return;
		}*/
//		checkLogin(str_username, str_password);
		Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
		RegistActivity.this.startActivity(intent);
	}

	// 访问服务器,验证用户名和密码
	public void checkLogin(final String str_username, final String str_password) {

		ConstantValue.cachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {

				Map<String, Object> requestParamsMap = new HashMap<String, Object>();
				requestParamsMap.put("username", str_username);
				requestParamsMap.put("password", str_password);
				requestParamsMap.put("type", type);

				Map<String, Object> retValue = HttpPostUtils.doPost(
						ConstantValue.BASE_URL
								+ "/androidlocation/RegistServlet",
						requestParamsMap);

				int code = (int) retValue.get("code");

				if (code == 200) {
					String value = (String) retValue.get("value");
					Message msg = new Message();
					msg.obj = value;
					msg.what = SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}
			}
		});
	}

}
