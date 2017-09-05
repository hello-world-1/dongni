package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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

import java.util.HashMap;
import java.util.Map;

/*
 * 绑定设备的界面
 */
public class CourseDetailActivity extends Activity implements TopBarView.onTitleBarClickListener{
    EditText imeiNumber;
    EditText phoneNumber;
    CustomImageView bind;
    TopBarView title;
    private String lessonID;

    @Override
    public void onBackClick() {
        CourseDetailActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.course_detail_activity);

        Intent intent = getIntent();
        lessonID = intent.getStringExtra("lessonID");

        Toast.makeText(CourseDetailActivity.this,lessonID,Toast.LENGTH_SHORT).show();

        imeiNumber = (EditText) findViewById(R.id.et_imei_number);
        phoneNumber = (EditText) findViewById(R.id.et_phone_number);

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        bind = (CustomImageView) findViewById(R.id.bind_image);
    }

    // 点击绑定按钮触发的方法
    public void bind(View view) {
        String imeiNumberStr = imeiNumber.getText().toString().trim();
        String phoneNumberStr = phoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(imeiNumberStr)
                || TextUtils.isEmpty(phoneNumberStr)) {
            Toast.makeText(CourseDetailActivity.this, ConstantValue.TXT_EMPTY,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!OthersUtils.isMobileNO(phoneNumberStr)) {
            Toast.makeText(CourseDetailActivity.this,
                    ConstantValue.PHONE_NUMBER_FORMAT_ERROR, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        bindDevice(imeiNumberStr, phoneNumberStr);

    }

    // 绑定设备
    public void bindDevice(final String imeiNumberStr, final String phoneNumberStr) {

        String uerID = PrefUtils.getUserID(CourseDetailActivity.this.getBaseContext());
        String token = PrefUtils.getToken(CourseDetailActivity.this.getBaseContext());
        String username = PrefUtils.getUsername(CourseDetailActivity.this.getBaseContext());

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("userID", uerID);
        params.put("token", token);
        params.put("IMEI", imeiNumberStr);
        params.put("watchTelephone", phoneNumberStr);
        params.put("controlTelephone", username);

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/watch/bind")//请求的url
                .addParams(params)
                .onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                // 绑定成功,替换成对勾图片bindsuccess
                                bind.setImageResource(R.mipmap.bindsuccess);
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode || 4 == errcode || 5 == errcode) { //数据库出错
                                    Toast.makeText(CourseDetailActivity.this, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                } else if (7 == errcode) { //给手表发送命令失败
                                    Toast.makeText(CourseDetailActivity.this, "与手表通信失败", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(CourseDetailActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(CourseDetailActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
