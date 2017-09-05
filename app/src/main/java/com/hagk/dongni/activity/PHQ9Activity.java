package com.hagk.dongni.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hagk.dongni.R;
import com.hagk.dongni.view.TopBarView;

/**
 * Created by Administrator on 2017-9-5.
 */

public class PHQ9Activity extends Activity implements TopBarView.onTitleBarClickListener{
    @Override
    public void onBackClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_contact_activity);
//
//        nickname = (EditText) findViewById(R.id.et_contact_nickname);
//        phone = (EditText) findViewById(R.id.et_contact_phone);
//
//        title = (TopBarView) findViewById(R.id.topbar);
//        title.setClickListener(this);
//
//        addContact = (ImageView) findViewById(R.id.iv_add_contact);
//        addContact.setOnClickListener(this);
//        saveContact = (Button)findViewById(R.id.bt_save_contact);
//        saveContact.setOnClickListener(this);
    }
}
