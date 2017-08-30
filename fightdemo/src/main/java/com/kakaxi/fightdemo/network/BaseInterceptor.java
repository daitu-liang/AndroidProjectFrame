package com.kakaxi.fightdemo.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by leixiaoliang on 2017/7/28.
 * 邮箱：lxliang1101@163.com
 */

public class BaseInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
//        String nonce = GenerateRandom.getNonce();
//        String timestamp = GenerateRandom.getTimestamp();
//        String signature = GenerateRandom.getSignature(nonce, timestamp);

        Request request = chain.request()
                .newBuilder()
                .addHeader("appid", "")
                .addHeader("timestamp", "")
                .addHeader("verify", "")
                .addHeader("token", "")
                .addHeader("type", "3")
                .build();
        return chain.proceed(request);
    }
}
