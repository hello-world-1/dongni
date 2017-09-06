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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * 添加联系人的界面
 */
public class AddQuestionActivity extends Activity implements TopBarView.onTitleBarClickListener,View.OnClickListener{

    TopBarView title;
    EditText question_title;
    EditText describe;
    ChoiceGroup public_choice;
    Button publish_question;
    int isPublic;

    @Override
    public void onBackClick() {
        AddQuestionActivity.this.finish();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_publish_question://发布按钮的点击事件
                publishQuestion();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_question_activity);

        title = (TopBarView) findViewById(R.id.topbar);
        title.setClickListener(this);

        question_title = (EditText) findViewById(R.id.et_question_title);
        describe = (EditText) findViewById(R.id.et_question_describe);

        public_choice = (ChoiceGroup) findViewById(R.id.public_choiceGroup);

        List<String> publicButton = new ArrayList<>();
        publicButton.add("公开");
        publicButton.add("不公开");

        public_choice.setValues(publicButton);
        public_choice.setColumn(2);
        public_choice.setView(AddQuestionActivity.this);//设置视图
        public_choice.setInitChecked(0);
        public_choice.setOnValueChangedListner(new ChoiceGroup.OnAnswerClickListner() {
            @Override
            public void OnValueChanged(String value,int index) {
                isPublic = index;
            }
        });

        publish_question = (Button)findViewById(R.id.bt_publish_question);
        publish_question.setOnClickListener(this);
    }

    // 发布问题
    public void publishQuestion() {

        if(TextUtils.isEmpty(question_title.getText().toString().trim())
                || TextUtils.isEmpty(describe.getText().toString().trim())){
            Toast.makeText(AddQuestionActivity.this,ConstantValue.TXT_EMPTY,Toast.LENGTH_SHORT).show();
            return;
        }

        String uerID = PrefUtils.getUserID(AddQuestionActivity.this.getBaseContext());
        String token = PrefUtils.getToken(AddQuestionActivity.this.getBaseContext());

        Map<String, Object> params = new HashMap<>();//构造请求的参数
        params.put("userID", uerID);
        params.put("token", token);
        params.put("parentID", uerID);
        params.put("title", question_title.getText().toString().trim());
        params.put("content", describe.getText().toString().trim());
        params.put("openFlag", isPublic);

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/api/user/question/new")//请求的url
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
                                Toast.makeText(AddQuestionActivity.this,"问题发布成功",Toast.LENGTH_SHORT).show();
                                String lessonID = json.get("lessonID").getAsString();
                                Intent intent = new Intent(AddQuestionActivity.this,QuestionDetailActivity.class);
                                intent.putExtra("lessonID",lessonID);
                                AddQuestionActivity.this.startActivity(intent);
                                AddQuestionActivity.this.finish();
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (0 == errcode) {
                                    Toast.makeText(AddQuestionActivity.this, "userID或token为空", Toast.LENGTH_SHORT).show();
                                } else if (1 == errcode) {
                                    Toast.makeText(AddQuestionActivity.this, "数据库查询失败", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(AddQuestionActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                } else if (3 == errcode) {
                                    Toast.makeText(AddQuestionActivity.this, "提问保存出错", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(AddQuestionActivity.this, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
