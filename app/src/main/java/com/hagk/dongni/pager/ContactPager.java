package com.hagk.dongni.pager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hagk.dongni.R;
import com.hagk.dongni.activity.AddContactActivity;
import com.hagk.dongni.activity.LoginActivity;
import com.hagk.dongni.activity.RegistActivity;
import com.hagk.dongni.adapter.ContactAdapter;
import com.hagk.dongni.adapter.QuestionAdapter;
import com.hagk.dongni.bean.Answer;
import com.hagk.dongni.bean.AnswerJson;
import com.hagk.dongni.bean.QuestionItem;
import com.hagk.dongni.broadcast.ContactBroadcast;
import com.hagk.dongni.utils.ConstantValue;
import com.hagk.dongni.utils.PrefUtils;
import com.hagk.dongni.view.TopBarView;
import com.hdl.myhttputils.MyHttpUtils;
import com.hdl.myhttputils.bean.StringCallBack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactPager extends BaseMenuDetailPager implements TopBarView.onTitleBarClickListener {

    public ContactPager(Activity activity) {
        super(activity);
    }

    private TopBarView title;
    private ListView lv;
    private Button addContact;
    //为什么在这里初始化了contacts对象之后在调用这个方法getContact还是报空指针?
    private List<String> contacts = new ArrayList<>();
    private ContactAdapter adapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onBackClick() {
    }

    // 退出应用时释放资源
    @Override
    public void releaseResourece() {
        mActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View initViews() {

        // 获取网络请求接口
        View view = View.inflate(mActivity, R.layout.concact_listview, null);// 找到listview所在的布局
        lv = (ListView) view.findViewById(R.id.contact_listview);
        addContact = (Button) view.findViewById(R.id.btn_add);

        title = (TopBarView) view.findViewById(R.id.topbar);
        title.setClickListener(this);
//        adapter = new ContactAdapter(contacts, R.layout.contact_listview_item, mActivity);

        addContact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_add:
                        Intent intent = new Intent(mActivity, AddContactActivity.class);
                        intent.putExtra("type", "addContact");
                        mActivity.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        //获取联系人
        getContact();

        lv.setAdapter(adapter);
        //如果listview的适配器的数据源size为0则显示空视图
        lv.setEmptyView(view.findViewById(R.id.tv_empty));

        broadcastReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ConstantValue.ACTION);
        mActivity.registerReceiver(broadcastReceiver,filter);
        return view;
    }

    public class MyReceiver extends BroadcastReceiver {
        //自定义一个广播接收器
        @Override
        public void onReceive(Context context, Intent intent) {
            getContact();
        }
        public MyReceiver(){
            System.out.println("MyReceiver");
            //构造函数，做一些初始化工作，本例中无任何作用
        }

    }

    @Override
    public void initData() {
        getContact();
//        adapter.notifyDataSetChanged();
    }

    public void getContact() {
        Set<String> contactsTmp = PrefUtils.getWatchContact(mActivity);

//        if (contacts == null) {
//            contacts = new ArrayList<>();
//        }
//
//        if (contactsTmp != null) {
//            // 不能new一个新的对象,否则刷新listview时还是原来的对象
//            Iterator iterator = contacts.iterator();
//            while(iterator.hasNext()){
//                contacts.remove(iterator.next());
//            }
//
//            for(String temp : contactsTmp){
//                contacts.add(temp);
//            }
//        }
        contacts = new ArrayList<>(contactsTmp);
        adapter = new ContactAdapter(contacts, R.layout.contact_listview_item, mActivity);
        lv.setAdapter(adapter);
    }
}
