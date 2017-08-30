package com.kakaxi.fightdemo.network;


import com.kakaxi.fightdemo.utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by leixiaoliang on 2017/1/10.
 */
public class OkHttpUtils {
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_READ_TIMEOUT = 10;
    private static final int DEFAULT_WRITE_TIMEOUT = 10;


    private static class OkHttpClientBuilder {
        private static final OkHttpClient.Builder BUILDER = create();
        private static OkHttpClient.Builder create() {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.addInterceptor(new BaseInterceptor());
            if (Constant.DEBUG) {
                builder.addInterceptor(getHttpLoggingInterceptor());
//                builder .addInterceptor(new LoggerInterceptor("fightdemo",true));
            }
            return builder;
        }
    }

    public static final OkHttpClient getClient() {
        return OkHttpClientBuilder.BUILDER.build();
    }

    /**
     * 日志拦截器
     * @return
     */
    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }
}
