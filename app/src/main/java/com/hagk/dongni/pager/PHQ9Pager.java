package com.hagk.dongni.pager;

import android.app.Activity;
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
import com.hagk.dongni.adapter.QuestionAdapter;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.bean.AnswerJson;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PHQ9Pager extends BaseMenuDetailPager {

    public PHQ9Pager(Activity activity) {
        super(activity);
    }

    private ListView lv;
    private Button commit;
    private List<QuestionItem> questions;
    private String surveyID;
    private Map<String, Answer> answers;

    @Override
    public View initViews() {
//        answers = new HashMap<>();
//        questions = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            QuestionItem item = new QuestionItem();
//            item.setQuestion("question" + i);
//            item.setAnswer1("question" + i + "answer" + i);
//            item.setAnswer2("question" + i + "answer" + (i + 1));
//            item.setAnswer3("question" + i + "answer" + (i + 2));
//            item.setAnswer4("question" + i + "answer" + (i + 3));
//            questions.add(item);
//        }

//获取问卷
        getQuestion();

        // 获取网络请求接口
        View view = View.inflate(mActivity, R.layout.phq9_listview, null);// 找到listview所在的布局
        lv = (ListView) view.findViewById(R.id.phq9_listview);
        commit = (Button) view.findViewById(R.id.btn_commit);

        commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_commit:
                        answerCommit();
                        break;
                    default:
                        break;
                }
            }
        });

        lv.setAdapter(new QuestionAdapter(questions, R.layout.phq9_listview_item, mActivity, answers));
        return view;
    }

    public String JsonPost(final String path, final String content) {
        BufferedReader in = null;
        String result = "";
        OutputStream os = null;
        try {
            URL url = new URL(path);
            // 然后我们使用httpPost的方式把lientKey封装成Json数据的形式传递给服务器
            // 在这里呢我们要封装的时这样的数据
            // 现在呢我们已经封装好了数据,接着呢我们要把封装好的数据传递过去
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // 设置User-Agent: Fiddler
            conn.setRequestProperty("ser-Agent", "Fiddler");
            // 设置contentType
            conn.setRequestProperty("Content-Type", "application/json");
            os = conn.getOutputStream();
            os.write(content.getBytes());
            os.flush();
            // 定义BufferedReader输入流来读取URL的响应
            // Log.i("-----send", "end");

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            if (conn.getResponseCode() == 200) {
                while ((line = in.readLine()) != null) {
                    result += line;
                }

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
        } catch (SocketTimeoutException e) {
            // Log.i("错误", "连接时间超时");
            e.printStackTrace();
            return "错误";
        } catch (MalformedURLException e) {
            // Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        } catch (ProtocolException e) {
            // Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        } catch (IOException e) {
            // Log.i("错误", "jdkfa");
            e.printStackTrace();
            return "错误";
        }// 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    // 提交按钮点击触发事件
    public void answerCommit() {
        if (surveyID == null) {
            Toast.makeText(mActivity, "生成问卷出错", Toast.LENGTH_SHORT).show();
            return;
        }
        if (questions == null || answers == null) {
            Toast.makeText(mActivity, "生成问卷出错", Toast.LENGTH_SHORT).show();
            return;
        }
        if (answers.size() != questions.size()) {
            Toast.makeText(mActivity, "请填写所有问卷", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String userID = PrefUtils.getUserID(mActivity);
            AnswerJson bean = new AnswerJson();
            bean.setSurveyID(surveyID);
            bean.setUserID(userID);
            bean.setAnswer(answers.values());
            String jsonstr = new Gson().toJson(bean);

            Toast.makeText(mActivity,jsonstr,Toast.LENGTH_SHORT).show();

            JsonPost(ConstantValue.BASE_URL + "/survey/insertAnswer", jsonstr);
        }

    }

    @Override
    public void initData() {
        // 获取问题
    }

    public void getQuestion(){
        Map<String, Object> params = new HashMap<>();
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
                                    //保存文件的ID
                                    surveyID = json.get("_id").getAsString();
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
                });
    }
}
