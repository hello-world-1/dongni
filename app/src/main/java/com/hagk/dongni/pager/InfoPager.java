package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.ChangePasswordActivity;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.OthersUtils;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.CustomImageView;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoPager extends BaseMenuDetailPager implements TopBarView.onTitleBarClickListener {

	//	标题
	TopBarView title;
	//	头像
	CustomImageView picture;
	//  昵称
	EditText nickname;
	//  年龄
	EditText age;
	//	性别
	ChoiceGroup sex;
	//	关系
	ChoiceGroup relation;
	//	电话
	EditText telephone;
	//	生日
	EditText birth;
	//	性格特点
	EditText character;

	/*孩子的信息*/
	//  年龄
	EditText childAge;
	//	性别
	ChoiceGroup childSex;
	//  年级
	EditText childGrade;
	//  生日
	EditText childBirth;
	//  性格特点
	EditText childCharacter;
	//	修改
	Button change;

	//单选按钮的选项
	String parentSexStr;
	String childSexStr;
	String relationStr;


	public InfoPager(Activity activity) {
		super(activity);
	}

	@Override
	public void onBackClick() {
		Toast.makeText(mActivity, "你点击了左侧按钮", Toast.LENGTH_LONG).show();
	}

	@Override
	public View initViews() {
		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.info_pager, null);// 找到listview所在的布局

		title = (TopBarView) view.findViewById(R.id.topbar);
		title.setClickListener(this);
		picture = (CustomImageView) view.findViewById(R.id.picture);
		/*家长的信息*/
		nickname = (EditText) view.findViewById(R.id.et_info_nickname);
		age = (EditText) view.findViewById(R.id.et_info_age);
		sex = (ChoiceGroup) view.findViewById(R.id.sex_choiceGroup);
		List<String> sexGroup = new ArrayList<>();
		sexGroup.add("男");
		sexGroup.add("女");
		sex.setValues(sexGroup);
		sex.setColumn(2);
		sex.setView(mActivity);//设置视图
		sex.setOnValueChangedListner(new ChoiceGroup.OnAnswerClickListner() {
			@Override
			public void OnValueChanged(String value,int index) {
				parentSexStr = value;
			}
		});
		relation = (ChoiceGroup) view.findViewById(R.id.relation_choiceGroup);
		List<String> relationGroup = new ArrayList<>();
		relationGroup.add("爸爸");
		relationGroup.add("妈妈");
		relationGroup.add("其他");
		relation.setValues(relationGroup);
		relation.setColumn(3);
		relation.setView(mActivity);//设置视图
		relation.setOnValueChangedListner(new ChoiceGroup.OnAnswerClickListner() {
			@Override
			public void OnValueChanged(String value,int index) {
				relationStr = value;
			}
		});
		telephone = (EditText) view.findViewById(R.id.et_info_telephone);
		birth = (EditText) view.findViewById(R.id.et_info_birth);
		character = (EditText) view.findViewById(R.id.et_info_character);

		/*孩子的信息*/
		childAge = (EditText) view.findViewById(R.id.et_info_child_age);
		childSex = (ChoiceGroup) view.findViewById(R.id.child_sex_choiceGroup);
		List<String> childSexGroup = new ArrayList<>();
		childSexGroup.add("男");
		childSexGroup.add("女");
		childSex.setValues(childSexGroup);
		childSex.setColumn(2);
		childSex.setView(mActivity);//设置视图
		childSex.setOnValueChangedListner(new ChoiceGroup.OnAnswerClickListner() {
			@Override
			public void OnValueChanged(String value,int index) {
				childSexStr = value;
			}
		});
		childGrade = (EditText) view.findViewById(R.id.et_info_child_grade);
		childBirth = (EditText) view.findViewById(R.id.et_info_child_birth);
		childCharacter = (EditText) view.findViewById(R.id.et_info_child_character);


		change = (Button) view.findViewById(R.id.change_info);

		change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()){
					case R.id.change_info:
						//处理修改按钮点击事件
						commitInfo();
						break;
				}
			}
		});

		return view;
	}

	public void commitInfo(){
		if (TextUtils.isEmpty(nickname.getText().toString().trim())
				|| TextUtils.isEmpty(age.getText().toString().trim())
				|| TextUtils.isEmpty(telephone.getText().toString().trim())
				|| TextUtils.isEmpty(birth.getText().toString().trim())
				|| TextUtils.isEmpty(character.getText().toString().trim())
				|| TextUtils.isEmpty(childAge.getText().toString().trim())
				|| TextUtils.isEmpty(childGrade.getText().toString().trim())
				|| TextUtils.isEmpty(parentSexStr)
				|| TextUtils.isEmpty(childSexStr)
				|| TextUtils.isEmpty(relationStr)
				|| TextUtils.isEmpty(childCharacter.getText().toString().trim())
				|| TextUtils.isEmpty(childBirth.getText().toString().trim())) {
			Toast.makeText(mActivity, "输入值不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if(!OthersUtils.isMobileNO(telephone.getText().toString().trim())){
			Toast.makeText(mActivity, "手机号输入格式不正确", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		/*
>> * userID:requested
>> * token:requested
>> * childAge:requested
>> * childSex:requested
>> * childGrade:requested
>> * childCharacter:requested
>> * parentAge:requested
>> * parentSex:requested
>> * parentCharacter:requested
>> * relationship:requested*/

		Map<String, Object> params = new HashMap<>();
		params.put("token", PrefUtils.getToken(mActivity));
		params.put("userID", PrefUtils.getUserID(mActivity));
		params.put("nickname", nickname.getText().toString().trim());
		params.put("parentSex", parentSexStr);
		params.put("relation", relationStr);
		params.put("parentTelephone", telephone.getText().toString().trim());
		params.put("parentBirth", birth.getText().toString().trim());
		params.put("parentCharacter", character.getText().toString().trim());
		params.put("childAge", childAge.getText().toString().trim());
		params.put("childSex", childSexStr);
		params.put("childGrade", childGrade.getText().toString().trim());
		params.put("childBirth", childBirth.getText().toString().trim());
		params.put("childCharacter", childCharacter.getText().toString().trim());

		MyHttpUtils.build()//构建myhttputils
				.url(ConstantValue.BASE_URL+"/api/user/information/changeInfo")//请求的url
				.addParams(params)
				.onExecuteByPost(new StringCallBack() {//开始执行，并有一个回调（异步的哦---->直接可以更新ui）
					@Override
					public void onSucceed(String result) {//请求成功之后会调用这个方法----显示结果
						JsonParser parse = new JsonParser();
						try {
							JsonObject json = (JsonObject) parse.parse(result);
							String status = json.get("status").getAsString();
							if (ConstantValue.SUCCESS_STATUS.equals(status)) {
								// TODO 修改成功后的逻辑
                                // 保存到数据库中第一次设置已完成,第一次提问时不需要再进行设置了
                                if(!PrefUtils.getInfoSetting(mActivity)){
                                    PrefUtils.setInfoSetting(mActivity,true);
                                }
							} else if (ConstantValue.ERROR_STATUS.equals(status)) {
								//error
								int errcode = json.get("errcode").getAsInt();
								//  TODO 错误信息的处理
//								if (3 == errcode) { //手机号未注册
//									Toast.makeText(LoginActivity.this, "该手机号未注册", Toast.LENGTH_SHORT).show();
//								} else if (4 == errcode) {
//									Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//								}else if (2 == errcode) {
//									Toast.makeText(LoginActivity.this, "服务器内部错误", Toast.LENGTH_SHORT).show();
//								}else if (6 == errcode) {
//									Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//								}
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
	}
}
