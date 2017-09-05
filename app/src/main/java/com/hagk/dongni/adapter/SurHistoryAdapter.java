package com.hagk.dongni.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.AddContactActivity;
import com.hagk.dongni.bean.SurveyHistory;

import java.util.List;

public class SurHistoryAdapter extends BaseAdapter implements ListAdapter{
    private List<SurveyHistory> data;
    private int layout;
    private Context context;
    private TextView surveyName = null;
    private TextView surveyTime = null;
    private TextView score = null;

    public SurHistoryAdapter(List<SurveyHistory> data, int layout, Context context) {
        this.data = data;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SurveyHistory surveyItem = data.get(position);
        final String tempSurveyName = surveyItem.getSurveyName();
        final String tempSurveyTime = surveyItem.getSurveyTime();
        final int tempScore = surveyItem.getScore();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.surveyName = (TextView) convertView.findViewById(R.id.tv_survy_type);
            viewHolder.surveyTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        viewHolder.surveyName.setText("问卷类型:" + tempSurveyName);
        viewHolder.surveyTime.setText("提交时间:" + tempSurveyTime);
        viewHolder.score.setText("分数:" + tempScore);
        return convertView;
    }

    private final class ViewHolder {
        TextView surveyName = null;
        TextView surveyTime = null;
        TextView score = null;
    }
}