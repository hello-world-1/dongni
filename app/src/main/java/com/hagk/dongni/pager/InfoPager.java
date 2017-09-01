package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.CustomImageView;
import com.hagk.dongni.view.TopBarView;

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
		relation = (ChoiceGroup) view.findViewById(R.id.relation_choiceGroup);
		telephone = (EditText) view.findViewById(R.id.et_info_telephone);
		birth = (EditText) view.findViewById(R.id.et_info_birth);
		character = (EditText) view.findViewById(R.id.et_info_character);

		/*孩子的信息*/
		childAge = (EditText) view.findViewById(R.id.et_info_child_age);
		childSex = (ChoiceGroup) view.findViewById(R.id.child_sex_choiceGroup);
		childGrade = (EditText) view.findViewById(R.id.et_info_child_grade);
		childBirth = (EditText) view.findViewById(R.id.et_info_child_birth);
		childCharacter = (EditText) view.findViewById(R.id.et_info_child_character);


		change = (Button) view.findViewById(R.id.change_info);

		change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (v.getId()){
//					修改按钮的点击事件
					case R.id.change_info:
						//处理修改按钮点击事件
						break;
				}
			}
		});

		return view;
	}

	@Override
	public void initData() {
	}
}
