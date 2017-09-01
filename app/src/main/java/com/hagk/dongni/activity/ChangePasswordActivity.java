package com.hagk.dongni.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends Activity implements TopBarView.onTitleBarClickListener{
    EditText username;
    EditText password;
    EditText repassword;
    EditText authode;
    TopBarView title;
    private String successCode;
    Button regist;
    String type;

    @Override
    public void onBackClick() {
        Toast.makeText(ChangePasswordActivity.this, "你点击了左侧按钮", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.regist_activity);

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

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
            Toast.makeText(ChangePasswordActivity.this, "手机号输入不能为空", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!OthersUtils.isMobileNO(username.getText().toString().trim())) {
            Toast.makeText(ChangePasswordActivity.this, "手机号输入格式不正确",
                    Toast.LENGTH_SHORT).show();
            username.setText("");
            return;
        }
        sendCode(username.getText().toString().trim());
    }

    public void sendCode(final String phoneNumber) {

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("telephone", phoneNumber);

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/sendCode")//请求的url
                .addParams(params)
                .onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果

                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                successCode = json.get("code").getAsString();
                                Toast.makeText(ChangePasswordActivity.this, "发送短信验证码成功", Toast.LENGTH_SHORT).show();
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                Toast.makeText(ChangePasswordActivity.this, "发送短信验证码失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
//							http://www.cnblogs.com/kaituorensheng/p/6616126.html
//							System.out.println("resultcode:" + json.get("resultcodeu").getAsInt());
//							System.out.println("reason:" + json.get("reason").getAsString());
//							JsonObject result = json.get("result").getAsJsonObject();
//							JsonObject today = result.get("today").getAsJsonObject();
//							System.out.println("weak:" + today.get("week").getAsString());
//							System.out.println("weather:" + today.get("weather").getAsString());
//							JsonArray futureArray = result.get("future").getAsJsonArray();
//							for (int i = 0; i < futureArray.size(); ++i) {
//								JsonObject subObj = futureArray.get(i).getAsJsonObject();
//								System.out.println("------");
//								System.out.println("week:" + subObj.get("week").getAsString());
//								System.out.println("weather:" + subObj.get("weather").getAsString());
//							}

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(ChangePasswordActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    }
                });
                /*.setJavaBean(LoginBean.class)
                        .onExecuteByPost(new CommCallback<LoginBean>() {
                            @Override
                            public void onSucceed(LoginBean loginBean) {
                                ToastUtils.showToast(LoginActivity.this,loginBean.getMsg());
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                ToastUtils.showToast(LoginActivity.this, FailedMsgUtils.getErrMsgStr(throwable));
                            }
                });*/
    }

    // 注册或者重置密码点击事件
    public void regist(View view) {
        final String str_username = username.getText().toString();
        final String str_password = password.getText().toString();
        final String str_repassword = repassword.getText().toString();
        final String str_authode = authode.getText().toString();

        if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_password)
                || TextUtils.isEmpty(str_repassword) || TextUtils.isEmpty(str_repassword)) {
            Toast.makeText(ChangePasswordActivity.this, "输入值不能为空", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!OthersUtils.isMobileNO(str_username)) {
            Toast.makeText(ChangePasswordActivity.this, "手机号输入格式不正确",
                    Toast.LENGTH_SHORT).show();
            username.setText("");
            return;
        }

        if (!str_password.equals(str_repassword)) {
            password.setText("");
            repassword.setText("");
            Toast.makeText(ChangePasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (!str_authode.equals(successCode)) {
            authode.setText("");
            Toast.makeText(ChangePasswordActivity.this, "验证码输入错误", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        checkLogin(str_username, str_password);
    }

    // 访问服务器,验证用户名和密码
    public void checkLogin(final String str_username, final String str_password) {

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("telephone", str_username);
        params.put("password", str_password);

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/signup")//请求的url
                .addParams(params)
                .onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                // success
                                // TODO 注册成功后的处理
                                Toast.makeText(ChangePasswordActivity.this, "用户注册成功", Toast.LENGTH_SHORT).show();
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //手机号已经注册
                                    Toast.makeText(ChangePasswordActivity.this, "该手机号已注册请直接登录", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) {
                                    Toast.makeText(ChangePasswordActivity.this, "用户注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(ChangePasswordActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
