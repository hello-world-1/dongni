package com.hagk.dongni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hagk.dongni.R;
import com.hagk.dongni.bean.CourseDigest;
import com.hagk.dongni.bean.SurveyHistory;

import java.util.List;

public class CourseDigestAdapter extends BaseAdapter implements ListAdapter{
    private List<CourseDigest> data;
    private int layout;
    private Context context;
    private TextView title = null;
    private TextView time = null;

    public CourseDigestAdapter(List<CourseDigest> data, int layout, Context context) {
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
        final CourseDigest item = data.get(position);
        final String tempTitle = item.getTitle();
        final String tempTime = item.getTime();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.course_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.course_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        viewHolder.title.setText(tempTitle);
        viewHolder.time.setText("时间:" + tempTime);
        return convertView;
    }

    private final class ViewHolder {
        TextView title = null;
        TextView time = null;
    }
}