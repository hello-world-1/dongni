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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecommendBookActivity extends Activity implements TopBarView.onTitleBarClickListener,View.OnClickListener{

    EditText nickname;
    EditText phone;
    ImageView addContact;
    TopBarView title;
    Button saveContact;
    String type;

    @Override
    public void onBackClick() {
        RecommendBookActivity.this.finish();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_add_contact://添加图片点击事件
                break;
            case R.id.bt_save_contact://保存按钮的点击事件
                saveContact();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_contact_activity);

        nickname = (EditText) findViewById(R.id.et_contact_nickname);
        phone = (EditText) findViewById(R.id.et_contact_phone);

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        addContact = (ImageView) findViewById(R.id.iv_add_contact);
        addContact.setOnClickListener(this);
        saveContact = (Button)findViewById(R.id.bt_save_contact);
        saveContact.setOnClickListener(this);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if("updateContact".equals(type)){
            title.setTitle("修改联系人");
            saveContact.setText("修改");
            nickname.setHint(intent.getStringExtra("nickname"));
            nickname.setText(intent.getStringExtra("nickname"));
            phone.setHint(intent.getStringExtra("phone"));
            phone.setText(intent.getStringExtra("phone"));
        }
    }

    // 点击保存按钮触发的方法
    public void saveContact() {
        String nicknameStr = nickname.getText().toString().trim();
        String phoneStr = phone.getText().toString().trim();

        if (TextUtils.isEmpty(nicknameStr) || TextUtils.isEmpty(phoneStr)) {
            Toast.makeText(RecommendBookActivity.this, ConstantValue.TXT_EMPTY,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!OthersUtils.isMobileNO(phoneStr)) {
            Toast.makeText(RecommendBookActivity.this,
                    ConstantValue.PHONE_NUMBER_FORMAT_ERROR, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //如果是新增联系人
        if("addContact".equals(type)){
            //判断联系人昵称是否存在
            Set<String> contacts = PrefUtils.getWatchContact(RecommendBookActivity.this); //获取到所有的联系人
            if(contacts != null){
                for(String item : contacts){
                    String temp = item.split(" ")[0];
                    if(nicknameStr.equals(temp)){
                        //如果包含了这个联系人
                        Toast.makeText(RecommendBookActivity.this,"已经添加过该联系人",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
        //如果没有包含该对象,则添加到本地数据库
        PrefUtils.setWatchContact(RecommendBookActivity.this,nicknameStr + " " +phoneStr);
        Intent intent = new Intent(ConstantValue.CONTACT_ACTION);
        sendBroadcast(intent);
        finish();
//        postData(nicknameStr, phoneStr);
    }

    // 添加联系人
    public void postData(final String nicknameStr, final String phoneStr) {

        String uerID = PrefUtils.getUserID(RecommendBookActivity.this.getBaseContext());
        String token = PrefUtils.getToken(RecommendBookActivity.this.getBaseContext());

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("userID", uerID);
        params.put("token", token);
        params.put("contactName", nicknameStr);
        params.put("telephoneNum", phoneStr);

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
                                RecommendBookActivity.this.finish();
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //数据库出错
                                    Toast.makeText(RecommendBookActivity.this, "服务器保存时有空值", Toast.LENGTH_SHORT).show();
                                } else if (5 == errcode) { //给手表发送命令失败
                                    Toast.makeText(RecommendBookActivity.this, "数据库查询出错", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) { //给手表发送命令失败
                                    Toast.makeText(RecommendBookActivity.this, "该用户注册手机号未绑定手机", Toast.LENGTH_SHORT).show();
                                } else if (7 == errcode) { //给手表发送命令失败
                                    Toast.makeText(RecommendBookActivity.this, "与手表通信失败", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(RecommendBookActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(RecommendBookActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
