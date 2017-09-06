package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AllReplyActivity extends Activity implements TopBarView.onTitleBarClickListener,View.OnClickListener{

    TextView titleText;
    TextView describe;
    ListView replys;
    TopBarView title;

    @Override
    public void onBackClick() {
        AllReplyActivity.this.finish();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.all_reply_listview);

        titleText = (TextView) findViewById(R.id.tv_title);
        describe = (TextView) findViewById(R.id.tv_describe);

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        replys = (ListView) findViewById(R.id.reply_listview);
    }

    // 添加联系人
    public void getReplys() {

        String uerID = PrefUtils.getUserID(AllReplyActivity.this.getBaseContext());
        String token = PrefUtils.getToken(AllReplyActivity.this.getBaseContext());

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("userID", uerID);
        params.put("token", token);

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/watch/contact/add")//请求的url
                .addParams(params)
                .onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                //TODO 保存成功后的处理
                                Intent intent = new Intent(ConstantValue.CONTACT_ACTION);
                                sendBroadcast(intent);
                                AllReplyActivity.this.finish();
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //数据库出错
                                    Toast.makeText(AllReplyActivity.this, "服务器保存时有空值", Toast.LENGTH_SHORT).show();
                                } else if (5 == errcode) { //给手表发送命令失败
                                    Toast.makeText(AllReplyActivity.this, "数据库查询出错", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) { //给手表发送命令失败
                                    Toast.makeText(AllReplyActivity.this, "该用户注册手机号未绑定手机", Toast.LENGTH_SHORT).show();
                                } else if (7 == errcode) { //给手表发送命令失败
                                    Toast.makeText(AllReplyActivity.this, "与手表通信失败", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(AllReplyActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(AllReplyActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
