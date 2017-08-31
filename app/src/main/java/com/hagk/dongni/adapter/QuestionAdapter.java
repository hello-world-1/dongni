package com.hagk.dongni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hagk.dongni.R;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.QuestionRadio;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends BaseAdapter implements ListAdapter {
    private List<QuestionItem> data;
    private int layout;
    private Context context;
    private ChoiceGroup answer = null;
    private TextView question = null;

    public QuestionAdapter(List<QuestionItem> data, int layout, Context context) {
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
        final QuestionItem questionItem = data.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            // 通过ItemType设置不同的布局
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.answer = (ChoiceGroup) convertView.findViewById(R.id.question_choiceGroup);
            viewHolder.question = (TextView) convertView.findViewById(R.id.question_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 设置数据
        viewHolder.question.setText(questionItem.getQuestion());
//        viewHolder.answer.setColumn(4);
        List<String> list = new ArrayList<String>();
        list.add(questionItem.getAnswer1());
        list.add(questionItem.getAnswer2());
        list.add(questionItem.getAnswer3());
        list.add(questionItem.getAnswer4());
        viewHolder.answer.setValues(list);//设置记录列表
        viewHolder.answer.setView(context);//设置视图

        viewHolder.answer.setOnValueChangedListner(new QuestionRadio.OnValueChangedListner() {
            @Override
            public void OnValueChanged(String value) {
                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    private final class ViewHolder {
        ChoiceGroup answer = null;
        TextView question = null;
    }
}