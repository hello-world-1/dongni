package com.hagk.dongni.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Environment;

public class ConstantValue {
	public static final String BASE_URL = "http://115.28.242.3:90";
	public static final String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/location/";
	public static final String ACTION = "android.intent.action.CONTACT_BROADCAST";
	public static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
	public static final String FRAGMENT_CONTENT = "fragment_content";
	public static final int LEFT_MENU_WIDTH = 200;
	public static final String TXT_EMPTY = "输入值不能为空";
	public static final String SUCCESS_STATUS = "success";
	public static final String ERROR_STATUS = "error";
	public static final String PHONE_NUMBER_FORMAT_ERROR = "手机号输入格式不正确";
	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
}
