package com.hagk.dongni.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.HttpPostUtils;
import com.hagk.dongni.utils.PrefUtils;

public class LoginActivity extends Activity {
	TextView mBtnBindPhone;

	EditText username;
	EditText password;
	Button login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);

		username = (EditText) findViewById(R.id.et_login_username);
		password = (EditText) findViewById(R.id.et_login_password);
		login = (Button) findViewById(R.id.btn_login);
		
		username.setText(PrefUtils.getUsername(this, "username"));
		
		password.setText(PrefUtils.getPassword(this, "password"));
		
		if(!TextUtils.isEmpty(username.getText())){
			login.setFocusable(true);
			login.setFocusableInTouchMode(true);
			login.requestFocus();
			login.requestFocusFromTouch();
		}
		
	}


	// 忘记密码按钮点击触发事件
	public void forgetPassword(View view) {
		Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
		intent.putExtra("type", "forget");
//		init_sms(intent);
		System.out.println("forgetpassword button onclick");
	}

	// 新用户按钮点击触发事件
	public void regist(View view) {
		Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
		intent.putExtra("type", "regist");
//		init_sms(intent);
		System.out.println("regist button onclick");
	}

	Handler handler;
	String str_username;
	String str_password;
	
	/**
	 * dp转px
	 */
	public int dip2px(int dip) {
		float scale = LoginActivity.this.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ConstantValue.ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ConstantValue.ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	// 登录按钮点击触发事件
	public void login(View view) {/*
		System.out.println("login button onclick");
		str_username = username.getText().toString().trim();
		str_password = password.getText().toString().trim();

		if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)) {
			Toast.makeText(LoginActivity.this, "输入值不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		
		if(!OthersUtils.isMobileNO(str_username)){
			Toast.makeText(LoginActivity.this, "手机号输入格式不正确", Toast.LENGTH_SHORT)
			.show();
			username.setText("");
			return;
		}

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SUCCESS:
					String result = (String) msg.obj;
					System.out.println("LoginActivity result:" + result);
					//验证成功后把用户名存到数据库中
					PrefUtils.setUsername(LoginActivity.this.getBaseContext(), "username", str_username);
					PrefUtils.setPassword(LoginActivity.this.getBaseContext(), "password", str_password);
					
					if (result.contains("success")) {
						final String picture = result.split("#")[1];
						
						if(PrefUtils.getFirstPicture(LoginActivity.this.getBaseContext(), "firstpicture" + str_username) == null){
							new Thread(new Runnable() {

								@Override
								public void run() {
									Bitmap bitmap;
									try {
										bitmap = Glide
												.with(LoginActivity.this)
												.load(picture)
												.asBitmap()
												.override(dip2px(44), dip2px(44))
												.into(Target.SIZE_ORIGINAL,
														Target.SIZE_ORIGINAL).get();
										String fileName = picture.substring(picture.lastIndexOf("/")+1, picture.length());
										saveFile(bitmap, fileName);
										
										PrefUtils.setFirstPicture(
												LoginActivity.this.getBaseContext(), "firstpicture" + str_username,ConstantValue.ALBUM_PATH + fileName);
										
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						}
						
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.putExtra("username", str_username);
						LoginActivity.this.startActivity(intent);
						
						//验证用户成功后修改用户的状态
						new Thread() {
							@Override
							public void run() {
								Map<String, Object> requestParamsMap = new HashMap<String, Object>();
								requestParamsMap.put("username", str_username);
								requestParamsMap.put("type", "changestatus");

								HttpPostUtils.doPost(ConstantValue.BASE_URL+"/androidlocation/LoginServlet",
										requestParamsMap);
							}
						}.start();
						
						System.out.println("inner");
						// 跳转到下一个页面后不显示
						LoginActivity.this.finish();
					} else{
						Toast.makeText(LoginActivity.this, "用户名或密码错误",
								Toast.LENGTH_SHORT).show();
						System.out.println("Error:" + result);
						password.setText("");
					}

					break;
					
				default:
					Toast.makeText(LoginActivity.this, "服务器错误",
							Toast.LENGTH_SHORT).show();
					password.setText("");
					break;
				}
			}
		};

		checkLogin(str_username, str_password);
	*/
		Intent intent = new Intent(LoginActivity.this, DeviceBindActivity.class);
		LoginActivity.this.startActivity(intent);
	}

	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;

	// 访问服务器,验证用户名和密码
	public void checkLogin(final String str_username, final String str_password) {

		new Thread() {
			@Override
			public void run() {
				Map<String, Object> requestParamsMap = new HashMap<String, Object>();
				requestParamsMap.put("username", str_username);
				requestParamsMap.put("password", str_password);
				requestParamsMap.put("type", "login");

				Map<String, Object> retValue = HttpPostUtils.doPost(ConstantValue.BASE_URL+"/androidlocation/LoginServlet",
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
		}.start();
	}

}
