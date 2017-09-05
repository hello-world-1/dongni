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
import android.widget.Toast;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.AddContactActivity;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.view.ChoiceGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ContactAdapter extends BaseAdapter implements ListAdapter{
    private List<String> data;
    private int layout;
    private Context context;
    private TextView nickname = null;
    private TextView phone = null;
    private Button change = null;
    private LayoutInflater mInflater;

    public ContactAdapter(List<String> data, int layout, Context context) {
        this.data = data;
        this.layout = layout;
        this.context = context;
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
        final String contactItem = data.get(position);
        final String tempNickname = contactItem.split(" ")[0];
        final String tempPhone = contactItem.split(" ")[1];
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.nickname = (TextView) convertView.findViewById(R.id.contact_nickname);
            viewHolder.phone = (TextView) convertView.findViewById(R.id.contact_phone);
            viewHolder.change = (Button) convertView.findViewById(R.id.change_contact);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置数据
        viewHolder.nickname.setText("联系人名称:" + tempNickname);
        viewHolder.phone.setText("手机号:" + tempPhone);
        viewHolder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.change_contact:
                        Intent intent = new Intent(context, AddContactActivity.class);
                        intent.putExtra("type","updateContact");
                        intent.putExtra("nickname",tempNickname);
                        intent.putExtra("phone",tempPhone);
                        context.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }

    private final class ViewHolder {
        TextView nickname = null;
        TextView phone = null;
        Button change = null;
    }
}