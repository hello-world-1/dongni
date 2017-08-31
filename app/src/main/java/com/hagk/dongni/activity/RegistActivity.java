package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.hagk.dongni.utils.HttpPostUtils;
import com.hagk.dongni.utils.OthersUtils;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;
import com.hdl.myhttputils.utils.FailedMsgUtils;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.content;

public class RegistActivity extends Activity {
    EditText username;
    EditText password;
    EditText repassword;
    EditText authode;
    private String successCode;
    Button regist;

    String type;

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
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                Toast.makeText(RegistActivity.this, "发送短信验证码失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(RegistActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
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
        }

        if (!str_authode.equals(successCode)) {
            authode.setText("");
            Toast.makeText(RegistActivity.this, "验证码输入错误", Toast.LENGTH_SHORT)
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
                .onExecute(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                // success

                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //手机号已经注册
                                    Toast.makeText(RegistActivity.this, "该手机号已注册请直接登录", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) {
                                    Toast.makeText(RegistActivity.this, "用户注册失败", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(RegistActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
