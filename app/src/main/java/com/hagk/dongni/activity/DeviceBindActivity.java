package com.hagk.dongni.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.HttpPostUtils;

/*
 * 绑定设备的界面
 */
public class DeviceBindActivity extends Activity {
	EditText imeiNumber;
	EditText phoneNumber;
	Handler handler;
	Button bind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.device_bind_activity);

		imeiNumber = (EditText) findViewById(R.id.et_imei_number);
		phoneNumber = (EditText) findViewById(R.id.et_phone_number);
		bind = (Button) findViewById(R.id.btn_bind);
	}

	// 点击绑定按钮触发的方法
	public void bind(View view) {
//		String imeiNumberStr = imeiNumber.getText().toString().trim();
//		String phoneNumberStr = phoneNumber.getText().toString().trim();

//		if (TextUtils.isEmpty(imeiNumberStr)
//				|| TextUtils.isEmpty(phoneNumberStr)) {
//			Toast.makeText(DeviceBindActivity.this, ConstantValue.TXT_EMPTY,
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		if (!OthersUtils.isMobileNO(phoneNumberStr)) {
//			Toast.makeText(DeviceBindActivity.this,
//					ConstantValue.PHONE_NUMBER_FORMAT_ERROR, Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
		Intent intent = new Intent(DeviceBindActivity.this, BaiduLocation.class);
		DeviceBindActivity.this.startActivity(intent);
		
	}

	// 访问服务器,验证用户名和密码
	public void checkLogin(final String str_username, final String str_password) {

		new Thread() {
			@Override
			public void run() {
				Map<String, Object> requestParamsMap = new HashMap<String, Object>();
				requestParamsMap.put("username", str_username);
				requestParamsMap.put("password", str_password);
				requestParamsMap.put("type", "login");

				Map<String, Object> retValue = HttpPostUtils.doPost(
						ConstantValue.BASE_URL
								+ "/androidlocation/LoginServlet",
						requestParamsMap);

				int code = (int) retValue.get("code");

				if (code == 200) {
					String value = (String) retValue.get("value");
					Message msg = new Message();
					msg.obj = value;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

}
