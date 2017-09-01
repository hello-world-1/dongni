package com.hagk.dongni.utils;

/**
 * Created by Administrator on 2017-9-1.
 */

public interface ResultHandler {
    public void processResult(String result);
    public void processResultError(Throwable throwable);
}
