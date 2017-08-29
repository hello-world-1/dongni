package com.hagk.dongni.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 */
public class PrefUtils {

	public static final String PREF_NAME = "config";

	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static String getUsername(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
	
	public static void setUsername(Context ctx, String key, String username) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, username).commit();
	}
	
	/*public static String getPictureUrl(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
	
	public static void setPictureUrl(Context ctx, String key, String pictureUrl) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, pictureUrl).commit();
	}*/
	
	public static String getPassword(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
	
	public static void setPassword(Context ctx, String key, String password) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, password).commit();
	}

	public static void setFirstPicture(Context ctx, String key, String picture) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, picture).commit();
	}
	
	public static String getFirstPicture(Context ctx, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
}
