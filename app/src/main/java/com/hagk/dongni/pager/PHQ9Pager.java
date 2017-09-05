package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.PHQ9Activity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.adapter.ContactAdapter;
import com.hagk.dongni.adapter.QuestionAdapter;
import com.hagk.dongni.adapter.SurHistoryAdapter;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.bean.AnswerJson;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.bean.SurveyHistory;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PHQ9Pager extends BaseMenuDetailPager implements TopBarView.onTitleBarClickListener{

    public PHQ9Pager(Activity activity) {
        super(activity);
    }

    private TopBarView title;
    private ListView lv;
    private Button commitSurvey;
    private List<SurveyHistory> historys;
    private BroadcastReceiver broadcastReceiver;
    private SurHistoryAdapter adapter;

    @Override
    public void onBackClick() {
    }

    // 退出应用时释放资源
    @Override
    public void releaseResourece() {
        mActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View initViews() {
        historys = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            SurveyHistory item = new SurveyHistory();
            item.setSurveyName("phq-9");
            item.setSurveyTime(new Date().toString());
            item.setScore(i);
            historys.add(item);
        }

        //获取问卷
//        getSurveyHistory();

        // 获取网络请求接口
        View view = View.inflate(mActivity, R.layout.survey_listview, null);// 找到listview所在的布局
        lv = (ListView) view.findViewById(R.id.survy_listview);
        commitSurvey = (Button) view.findViewById(R.id.btn_survy);

        title = (TopBarView) view.findViewById(R.id.topbar);
        title.setClickListener(this);

        commitSurvey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_survy:
                        //跳转到其他的activity
                        Intent intent = new Intent(mActivity, PHQ9Activity.class);
                        mActivity.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        adapter = new SurHistoryAdapter(historys, R.layout.survy_listview_item, mActivity);
        lv.setAdapter(adapter);
        lv.setEmptyView(view.findViewById(R.id.tv_empty));

        broadcastReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValue.SURVEY_ACTION);
        mActivity.registerReceiver(broadcastReceiver,filter);

        return view;
    }

    @Override
    public void initData() {
        // 每次点击条目时触发的方法
        getSurveyHistory();
    }

    public class MyReceiver extends BroadcastReceiver {
        //自定义一个广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"广播节后这",Toast.LENGTH_SHORT).show();
//            getSurveyHistory();
        }
        public MyReceiver(){
        }

    }

    public void getSurveyHistory(){
        Map<String, Object> params = new HashMap<>();
        params.put("parentID", PrefUtils.getUserID(mActivity));

        MyHttpUtils.build()//构建myhttputils
                .url(ConstantValue.BASE_URL + "/survey/newestScore")//请求的url
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
                                JsonArray futureArray = json.get("surveyAnswer").getAsJsonArray();
                                historys = new ArrayList<SurveyHistory>();
                                // 得到每次做的问卷
                                for (int i = 0; i < futureArray.size(); ++i) {
                                    JsonObject subObj = futureArray.get(i).getAsJsonObject();
                                    SurveyHistory item = new SurveyHistory();
//                                    得到问卷名称
                                    String surveyName = json.get("surveyName").getAsString();
                                    item.setSurveyName(surveyName);
                                    String surveyTime = json.get("surveyTime").getAsString();
                                    item.setSurveyTime(surveyTime);
                                    // 得到答案
                                    JsonArray answer = json.get("answer").getAsJsonArray();
                                    int score = 0;
                                    //创建所有条目的数组对象
                                    for (int j = 0; j < answer.size(); ++j) {
                                        JsonObject topicItem = answer.get(i).getAsJsonObject();
                                        score += topicItem.get("answerIndex").getAsInt();
                                    }
                                    item.setScore(score);
                                    historys.add(item);
                                }

                                adapter = new SurHistoryAdapter(historys, R.layout.survy_listview_item, mActivity);
                                lv.setAdapter(adapter);
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
                });
    }
}
