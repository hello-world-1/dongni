package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.CustomImageView;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity implements TopBarView.onTitleBarClickListener {
	EditText username;
	EditText password;
    TopBarView title;
    CustomImageView picture;
	Button login;
	private int pos;

	@Override
	public void onBackClick() {
		LoginActivity.this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);

		Intent intent = getIntent();
		pos = intent.getIntExtra("pos",0);

		title = (TopBarView) findViewById(R.id.topbar);
		title.setClickListener(this);

		username = (EditText) findViewById(R.id.et_login_username);
		password = (EditText) findViewById(R.id.et_login_password);
        picture = (CustomImageView) findViewById(R.id.picture);
		login = (Button) findViewById(R.id.btn_login);

		//如果之前登录成功过,则自动填写
		username.setText(PrefUtils.getUsername(this));
		password.setText(PrefUtils.getPassword(this));

		//如果不为空,聚焦到登录按钮
		if(!TextUtils.isEmpty(username.getText())){
			login.setFocusable(true);
			login.setFocusableInTouchMode(true);
			login.requestFocus();
			login.requestFocusFromTouch();
		}
	}

	// 忘记密码按钮点击触发事件
	public void forgetPassword(View view) {
		Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
		intent.putExtra("type", "forgetpassword");
		LoginActivity.this.startActivity(intent);
		Toast.makeText(LoginActivity.this,"点击忘记密码",Toast.LENGTH_SHORT).show();
	}

	// 新用户按钮点击触发事件
	public void regist(View view) {
		Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
		LoginActivity.this.startActivity(intent);
		LoginActivity.this.startActivity(intent);
		Toast.makeText(LoginActivity.this,"点击注册按钮",Toast.LENGTH_SHORT).show();
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
	/*final String picture = result.split("#")[1];

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
	}*/

	// 登录按钮点击触发事件
	public void login(View view) {
		String str_username = username.getText().toString().trim();
		String str_password = password.getText().toString().trim();

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
        checkLogin(str_username, str_password);
	}

	// 访问服务器,验证用户名和密码
	public void checkLogin(final String str_username, final String str_password) {

		Map<String, Object> params = new HashMap<>();
		params.put("telephone", str_username);
		params.put("password", str_password);

		MyHttpUtils.build()//构建myhttputils
				.url(ConstantValue.BASE_URL+"/api/user/signin")//请求的url
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
								PrefUtils.setUserID(LoginActivity.this.getBaseContext(), userID);
								//保存用户的token
								PrefUtils.setToken(LoginActivity.this.getBaseContext(), token);
								//保存用户的用户名
								PrefUtils.setUsername(LoginActivity.this.getBaseContext(), str_username);
								//保存用户的密码
								PrefUtils.setPassword(LoginActivity.this.getBaseContext(), str_password);

								//TODO 跳转到其他的activity
								LoginActivity.this.finish();
								Intent intent = new Intent(ConstantValue.LOGIN_ACTION);
								intent.putExtra("pos",pos);
								sendBroadcast(intent);
								Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
							} else if (ConstantValue.ERROR_STATUS.equals(status)) {
								//error
								int errcode = json.get("errcode").getAsInt();
								if (3 == errcode) { //手机号未注册
									Toast.makeText(LoginActivity.this, "该手机号未注册", Toast.LENGTH_SHORT).show();
								} else if (4 == errcode) {
									Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
								}else if (2 == errcode) {
									Toast.makeText(LoginActivity.this, "服务器内部错误", Toast.LENGTH_SHORT).show();
								}else if (6 == errcode) {
									Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(LoginActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
					}
				});
	}

}
