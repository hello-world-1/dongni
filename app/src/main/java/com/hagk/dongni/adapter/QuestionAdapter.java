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
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.view.ChoiceGroup;
import com.hagk.dongni.view.QuestionRadio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends BaseAdapter implements ListAdapter {
    private List<QuestionItem> data;
    private int layout;
    private Context context;
    private ChoiceGroup answer = null;
    private TextView question = null;
    private LayoutInflater mInflater;
    private Map<String,Answer> answers;

    public QuestionAdapter(List<QuestionItem> data, int layout, Context context, Map<String,Answer> answers) {
        this.data = data;
        this.layout = layout;
        this.context = context;
        this.answers = answers;
        mInflater = LayoutInflater.from(context);
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
        convertView = LayoutInflater.from(context).inflate(layout, null);
        ChoiceGroup answer = (ChoiceGroup) convertView.findViewById(R.id.question_choiceGroup);
        final TextView question = (TextView) convertView.findViewById(R.id.question_textview);
        question.setText(questionItem.getQuestion());
        answer.setValues(questionItem);
        if(answers.containsKey(questionItem.getQuestion())){
            Answer temp = answers.get(questionItem.getQuestion());
            answer.setInitChecked(temp.getAnswerIndex());
        }
        answer.setColumn(4);
        answer.setView(context);//设置视图
        answer.setOnValueChangedListner(new ChoiceGroup.OnAnswerClickListner() {
            @Override
            public void OnValueChanged(String value,int index) {
                Answer answer = new Answer();
                answer.setTopicName(questionItem.getQuestion());
                answer.setAnswer(value);
                answer.setAnswerIndex(index);
                answers.put(questionItem.getQuestion(),answer);
                Toast.makeText(context, questionItem.getQuestion()+"|"+value+index, Toast.LENGTH_SHORT).show();
            }
        });
//        if (convertView == null) {
//            // 通过ItemType设置不同的布局
//            convertView = mInflater.inflate(R.layout.phq9_listview_item, parent,
//                    false);
//            viewHolder = new ViewHolder();
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        // 设置数据
//        viewHolder.question.setText(questionItem.getQuestion());
//        viewHolder.answer.setColumn(4);
//        viewHolder.answer.setValues(questionItem);
//        viewHolder.answer.setView(context);//设置视图
//
//        viewHolder.answer.setOnValueChangedListner(new QuestionRadio.OnValueChangedListner() {
//            @Override
//            public void OnValueChanged(String value) {
//                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }

    private final class ViewHolder {
        ChoiceGroup answer = null;
        TextView question = null;
    }
}