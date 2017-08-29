package com.hagk.dongni.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.hagk.dongni.utils.ConstantValue;

public class UserService {
	private UserObserver observer;

	static class UserHolder {
		private static UserService userService = new UserService();
	}

	public static UserService getUserService() {
		return UserHolder.userService;
	}

	public UserService() {
		Retrofit retrofit = new Retrofit.Builder().baseUrl(ConstantValue.BASE_URL)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create()).build();
		observer = retrofit.create(UserObserver.class);
	}

	/**
	 * 获取service的实例
	 */
	public UserObserver getObserver() {
		return observer;
	}
}
