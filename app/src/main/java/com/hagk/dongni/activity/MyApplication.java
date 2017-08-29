package com.hagk.dongni.activity;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.hagk.dongni.service.LocationService;

public class MyApplication extends Application {
	public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
       
    }
}
