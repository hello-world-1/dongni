package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.CourseDetailActivity;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.MainActivity;
import com.hagk.dongni.activity.PHQ9Activity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.adapter.CourseDigestAdapter;
import com.hagk.dongni.adapter.SurHistoryAdapter;
import com.hagk.dongni.bean.CourseDigest;
import com.hagk.dongni.bean.SurveyHistory;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoursePager extends BaseMenuDetailPager implements TopBarView.onTitleBarClickListener {

	public CoursePager(Activity activity) {
		super(activity);
	}

	private TopBarView title;
	private ListView lv;
	private List<CourseDigest> courses;

	private CourseDigestAdapter adapter;

	@Override
	public void onBackClick() {
		MainActivity mainActivity = (MainActivity) mActivity;
		mainActivity.getContentFragment().getIndexPager().initData();
	}

	@Override
	public View initViews() {
//		courses = new ArrayList<>();
//		for (int i = 0; i < 37; i++) {
//			CourseDigest item = new CourseDigest();
//			item.setTitle("课程一");
//			item.setTime(new Date().toString());
//			item.setLessonID("fasdfdsf");
//			courses.add(item);
//		}

		//获取报名的所有课程
//        getCourse();

		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.course_listview, null);// 找到listview所在的布局
		lv = (ListView) view.findViewById(R.id.course_listview);



		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mActivity, CourseDetailActivity.class);
				intent.putExtra("lessonID",courses.get(position).getLessonID());
				mActivity.startActivity(intent);
			}
		});

		title = (TopBarView) view.findViewById(R.id.topbar);
		title.setClickListener(this);

//		adapter = new CourseDigestAdapter(courses, R.layout.course_listview_item, mActivity);
//		lv.setAdapter(adapter);
		lv.setEmptyView(view.findViewById(R.id.tv_empty));
		return view;
	}

	@Override
	public void initData() {
		// 每次点击条目时触发的方法
		getCourse();
	}

	public void getCourse(){
		Map<String, Object> params = new HashMap<>();
		params.put("userID", PrefUtils.getUserID(mActivity));
		params.put("token", PrefUtils.getToken(mActivity));

		MyHttpUtils.build()//构建myhttputils
				.url(ConstantValue.BASE_URL + "/api/user/lesson/list")//请求的url
				.addParams(params)
				.onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
					@Override
					public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
						JsonParser parse = new JsonParser();
						try {
							JsonObject json = (JsonObject) parse.parse(result);
							String status = json.get("status").getAsString();
							if (ConstantValue.SUCCESS_STATUS.equals(status)) {
								// TODO 得到所有报名的课程
								JsonArray futureArray = json.get("bills").getAsJsonArray();
								courses = new ArrayList<CourseDigest>();
								// 得到每次做的问卷
								for (int i = 0; i < futureArray.size(); ++i) {
									JsonObject subObj = futureArray.get(i).getAsJsonObject();
									CourseDigest item = new CourseDigest();
									String title = json.get("title").getAsString();
									item.setTitle(title);
									String time = json.get("createAt").getAsString();
									item.setTime(time);
									String lessonID = json.get("lessonID").getAsString();
									item.setLessonID(lessonID);
									courses.add(item);
								}

								adapter = new CourseDigestAdapter(courses, R.layout.course_listview_item, mActivity);
								lv.setAdapter(adapter);
							} else if (ConstantValue.ERROR_STATUS.equals(status)) {
								//error
								int errcode = json.get("errcode").getAsInt();
								if (0 == errcode) {
									Toast.makeText(mActivity, "userID或token为空", Toast.LENGTH_SHORT).show();
								} else if (1 == errcode) {
									Toast.makeText(mActivity, "数据库查询失败", Toast.LENGTH_SHORT).show();
								} else if (2 == errcode) {
									Toast.makeText(mActivity, "用户不存在", Toast.LENGTH_SHORT).show();
								} else if (4 == errcode) {
									Toast.makeText(mActivity, "您没有报名过任何课程", Toast.LENGTH_SHORT).show();
								}
							}
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailed(Throwable throwable) {//请求失败的时候会调用这个方法
						Toast.makeText(mActivity, throwable.getStackTrace().toString() + "错误", Toast.LENGTH_SHORT).show();
					}
				});
	}
}
