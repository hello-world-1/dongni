package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
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


// TODO 获取数据的代码
public class CourseDetailActivity extends Activity implements TopBarView.onTitleBarClickListener{
    TextView describe;
    TextView teacher;
    TextView date;
    TextView classTime;
    TextView limitPerson;
    TextView entryDeadline;
    TextView period;
    TextView phone;
    TextView price;
    TextView applicantNumber;
    TextView status;
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

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        describe = (TextView) findViewById(R.id.tv_describe);
        teacher = (TextView) findViewById(R.id.tv_teacher);
        date = (TextView) findViewById(R.id.tv_date);
        classTime = (TextView) findViewById(R.id.tv_class_date);
        limitPerson = (TextView) findViewById(R.id.tv_limit_person);
        entryDeadline = (TextView) findViewById(R.id.tv_limit_date);
        period = (TextView) findViewById(R.id.tv_period);
        phone = (TextView) findViewById(R.id.tv_phone);
        price = (TextView) findViewById(R.id.tv_price);
        applicantNumber = (TextView) findViewById(R.id.tv_applicantNumber);
        status = (TextView) findViewById(R.id.tv_status);

        setText();
    }

    // 初始化数据
    public void setText() {

        String uerID = PrefUtils.getUserID(CourseDetailActivity.this.getBaseContext());
        String token = PrefUtils.getToken(CourseDetailActivity.this.getBaseContext());
        String username = PrefUtils.getUsername(CourseDetailActivity.this.getBaseContext());

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("userID", uerID);
        params.put("token", token);
        params.put("lessonID", lessonID);

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
//                                设置文本框的内容
                                describe.setText(json.get("status").getAsString());
                                teacher.setText(json.get("status").getAsString());
                                date.setText(json.get("status").getAsString());
                                classTime.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());
                                describe.setText(json.get("status").getAsString());



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
