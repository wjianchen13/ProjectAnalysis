package com.cold.http;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
    public static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
        CookieManager cookieManager = new CookieManager();
        OkHttpUtil.setCookie(cookieManager);
    }

    public interface MyCallBack {
        void onFailure(Request request, IOException e);

        void onResponse(String json);

    }

    public static void setCookie(CookieManager cookieManager) {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        mOkHttpClient.setCookieHandler(cookieManager);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");




}