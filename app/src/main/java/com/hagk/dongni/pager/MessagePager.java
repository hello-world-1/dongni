package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hagk.dongni.R;
import com.hagk.dongni.activity.AllReplyActivity;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.MainActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.view.TopBarView;

public class MessagePager extends BaseMenuDetailPager implements OnClickListener,TopBarView.onTitleBarClickListener {

    TextView tv_reply;
    TextView tv_lesson;
    TextView tv_book;

    public MessagePager(Activity activity) {
        super(activity);
    }

    @Override
    public void onBackClick() {
        MainActivity mainActivity = (MainActivity) mActivity;
        mainActivity.getContentFragment().getIndexPager().initData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_reply:
                Intent intent = new Intent(mActivity, AllReplyActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.tv_lesson:
                Intent lessonIntent = new Intent(mActivity, AllReplyActivity.class);
                mActivity.startActivity(lessonIntent);
                break;
            case R.id.tv_book:
                Intent bookIntent = new Intent(mActivity, AllReplyActivity.class);
                mActivity.startActivity(bookIntent);
                break;
        }
    }

    @Override
    public View initViews() {
        // 获取网络请求接口
        View view = View.inflate(mActivity, R.layout.message_pager, null);// 找到listview所在的布局
        tv_reply = (TextView) view.findViewById(R.id.tv_reply);
        tv_reply.setOnClickListener(this);
        tv_lesson = (TextView) view.findViewById(R.id.tv_lesson);
        tv_lesson.setOnClickListener(this);
        tv_book = (TextView) view.findViewById(R.id.tv_book);
        tv_book.setOnClickListener(this);

        return view;
    }

    @Override
    public void initData() {
    }
}
