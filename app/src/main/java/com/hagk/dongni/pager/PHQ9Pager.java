package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.adapter.QuestionAdapter;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.QuestionRadio;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class PHQ9Pager extends BaseMenuDetailPager {

    public PHQ9Pager(Activity activity) {
        super(activity);
    }

    private ListView lv;
    private Button commit;
    private List<QuestionItem> questions;

    @Override
    public View initViews() {

        questions = new ArrayList<>();
        for(int i=0; i < 2; i++){
            QuestionItem item = new QuestionItem();
            item.setQuestion("question" + i);
            item.setAnswer1("question"+i+"answer"+i);
            item.setAnswer2("question"+i+"answer"+(i+1));
            item.setAnswer3("question"+i+"answer"+(i+2));
            item.setAnswer4("question"+i+"answer"+(i+3));
            questions.add(item);
        }

        // 获取网络请求接口
        View view = View.inflate(mActivity, R.layout.phq9_listview, null);// 找到listview所在的布局
        lv = (ListView) view.findViewById(R.id.phq9_listview);
        commit = (Button) view.findViewById(R.id.btn_commit);
        lv.setAdapter(new QuestionAdapter(questions, R.layout.phq9_listview_item, mActivity));
        return view;
    }

    // 提交按钮点击触发事件
    public void answerCommit(View view) {
        //向数据库中插入答案
        Map<String, Object> params = new HashMap<>();

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/survey/insertAnswer")//请求的url
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
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) { //手机号未注册
                                    Toast.makeText(mActivity, "该手机号未注册", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) {
                                    Toast.makeText(mActivity, "密码错误", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                } else if (6 == errcode) {
                                    Toast.makeText(mActivity, "密码错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(mActivity, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void initData() {
        // 获取问题
        /*Map<String, Object> params = new HashMap<>();
        params.put("surveyName", "phq9");

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/survey/productSurvey")//请求的url
                .addParams(params)
                .onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
                    @Override
                    public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
                        JsonParser parse = new JsonParser();
                        try {
                            JsonObject json = (JsonObject) parse.parse(result);
                            String status = json.get("status").getAsString();
                            if (ConstantValue.SUCCESS_STATUS.equals(status)) {
                                // TODO这个问卷应该是一个对象
                                JsonArray futureArray = json.get("survey").getAsJsonArray();
                                for (int i = 0; i < futureArray.size(); ++i) {
                                    JsonObject subObj = futureArray.get(i).getAsJsonObject();
                                    // 得到问卷
                                    JsonArray topic = json.get("topic").getAsJsonArray();
                                    //创建所有条目的数组对象
                                    questions = new ArrayList<QuestionItem>();
                                    for (int j = 0; j < topic.size(); ++j) {
                                        //得到每个条目
                                        JsonObject topicItem = topic.get(i).getAsJsonObject();
                                        QuestionItem item = new QuestionItem();
                                        item.setQuestion(topicItem.get("topicName").getAsString());
                                        item.setAnswer1(topicItem.get("answer1").getAsString());
                                        item.setAnswer2(topicItem.get("answer2").getAsString());
                                        item.setAnswer3(topicItem.get("answer3").getAsString());
                                        item.setAnswer4(topicItem.get("answer4").getAsString());
                                        //向问题列表中插入问题
                                        questions.add(item);
                                    }
                                }
                            } else if (ConstantValue.ERROR_STATUS.equals(status)) {
                                //error
                                int errcode = json.get("errcode").getAsInt();
                                if (3 == errcode) {
                                    Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                } else if (4 == errcode) {
                                    Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                } else if (2 == errcode) {
                                    Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                } else if (6 == errcode) {
                                    Toast.makeText(mActivity, "服务器内部错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
                        Toast.makeText(mActivity, throwable.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/

    }
}
