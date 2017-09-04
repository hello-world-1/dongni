package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends Activity implements TopBarView.onTitleBarClickListener{
    TextView username;
    EditText password;
    EditText repassword;
    EditText authode;
    TopBarView title;
    private String successCode;
    Button regist;

    @Override
    public void onBackClick() {
        ChangePasswordActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.changepassword_activity);
        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(type != null){
            if("repassword".equals(type)){
                title.setTitle("修改密码");
            }else if("forgetpassword".equals(type)){
                title.setTitle("忘记密码");
            }
        }

        username = (TextView) findViewById(R.id.et_regist_username);
        username.setText(PrefUtils.getUsername(ChangePasswordActivity.this));
        authode = (EditText) findViewById(R.id.et_regist_authcode);
        password = (EditText) findViewById(R.id.et_regist_password);
        repassword = (EditText) findViewById(R.id.et_regist_re_password);
        regist = (Button) findViewById(R.id.btn_regist);
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
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(ChangePasswordActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                        throwable.printStackTrace();
                    }
                });
    }

    // 确认按钮点击事件
    public void commitPass(View view) {
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
        params.put("password", PrefUtils.getPassword(ChangePasswordActivity.this));
        params.put("newpassword", str_password);
        params.put("re_newpassword", str_password);
        params.put("userID", PrefUtils.getUserID(ChangePasswordActivity.this));
        params.put("token", PrefUtils.getToken(ChangePasswordActivity.this));

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/resetPassword")//请求的url
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
                                Toast.makeText(ChangePasswordActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();

                                //保存用户的用户名
                                PrefUtils.setUsername(ChangePasswordActivity.this.getBaseContext(), str_username);
                                //保存用户的密码
                                PrefUtils.setPassword(ChangePasswordActivity.this.getBaseContext(), str_password);
                                //跳转到登录的界面
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                ChangePasswordActivity.this.startActivity(intent);
                                ChangePasswordActivity.this.finish();

                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                // TODO 错误信息的处理
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //手机号已经注册
                                    Toast.makeText(ChangePasswordActivity.this, "该手机号已注册请直接登录", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) {
                                    Toast.makeText(ChangePasswordActivity.this, "用户注册失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
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
