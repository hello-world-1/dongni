package com.hagk.dongni.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hagk.dongni.pager.ContactPager;

/**
 * Created by Administrator on 2017-9-4.
 */

public class ContactBroadcast extends BroadcastReceiver {


    ContactPager pager;

    public ContactBroadcast(ContactPager pager){
        this.pager = pager;
    }

    public ContactBroadcast(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        //对接收到的广播进行处理，intent里面包含数据
        Toast.makeText(context,"广播接受者",Toast.LENGTH_SHORT).show();
        pager.getContact();
    }
}