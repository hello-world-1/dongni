package com.hagk.dongni.http;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import com.hagk.dongni.bean.UserInfo;

public interface UserObserver {
	@GET("/androidlocation/UserServlet")
    Observable<UserInfo> getResult(@Query("username") String username);
}
