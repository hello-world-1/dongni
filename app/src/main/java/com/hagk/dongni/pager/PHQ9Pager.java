package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.QuestionRadio;

import java.util.ArrayList;
import java.util.List;

public class PHQ9Pager extends BaseMenuDetailPager {

	public PHQ9Pager(Activity activity) {
		super(activity);
	}

	Button register;
	

	@Override
	public View initViews() {
		// 获取网络请求接口
		View view = View.inflate(mActivity, R.layout.phq9_listview, null);// 找到listview所在的布局
		List<String> list = new ArrayList<String>();
		list.add("施工");
		list.add("服务");
		list.add("设计");
		list.add("监理");
		list.add("其他");


		ChoiceGroup choiceGroup = (ChoiceGroup)view.findViewById(R.id.choiceGroup);
		choiceGroup.setColumn(4);//设置列数
		choiceGroup.setValues(list);//设置记录列表
		choiceGroup.setView(mActivity);//设置视图
		choiceGroup.setInitChecked(0);//设置最初默认被选按钮

		choiceGroup.setOnValueChangedListner(new QuestionRadio.OnValueChangedListner() {
			@Override
			public void OnValueChanged(String value) {
				Toast.makeText(mActivity,value,Toast.LENGTH_SHORT).show();
			}
		});

		choiceGroup.getCurrentValue();//获取当前被选择的按钮值
		return view;
	}

	@Override
	public void initData() {
	}
}
