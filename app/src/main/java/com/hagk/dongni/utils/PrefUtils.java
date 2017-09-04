package com.hagk.dongni.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

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

	public static String getUserID(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("userID", null);
	}

	public static void setUserID(Context ctx, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString("userID", value).commit();
	}
	
	public static String getToken(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("token", null);
	}
	
	public static void setToken(Context ctx, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString("token", value).commit();
	}

	public static String getUsername(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("username", null);
	}

	public static void setUsername(Context ctx, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString("username", value).commit();
	}

	public static String getPassword(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getString("password", null);
	}

	public static void setPassword(Context ctx, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString("password", value).commit();
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

	public static boolean getInfoSetting(Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean("isSetting",false);
	}

	public static void setInfoSetting(Context ctx,boolean isSetting){
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean("isSetting",isSetting).commit();
	}

	public static void setWatchContact(Context ctx,String contact){
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Set<String> contacts = sp.getStringSet("contact",null); //获取到所有的联系人
		String nickname = contact.split(" ")[0];
		for(String item : contacts){
			String temp = item.split(" ")[0];
			if(nickname.equals(temp)){
				//如果包含了这个联系人
				contacts.remove(item);
				contacts.add(contact);
				break;
			}
		}
		sp.edit().putStringSet("contact",contacts).commit();
	}

	public static Set<String> getWatchContact(Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return sp.getStringSet("contact",null);
	}
}
