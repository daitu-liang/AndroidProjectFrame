package com.kakaxi.fightdemo.network;


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
    private static class OkHttpHolder {
        private static final OkHttpUtils INSTANCE = new OkHttpUtils();

    }

    public static final OkHttpUtils getInstance() {
        return OkHttpUtils.OkHttpHolder.INSTANCE;
    }
    private static class GetOkHttpInstance {
        private static final OkHttpClient INSTANCE =
                new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
//                        .addInterceptor(new LoggerInterceptor("HttpMethods",true))
                        .addInterceptor(getHttpLoggingInterceptor())
//                        .addNetworkInterceptor(getCacheInterceptor()).cache(cache).addInterceptor(getCacheInterceptor())
                        .build();
    }

    public static final OkHttpClient getClient() {
        return GetOkHttpInstance.INSTANCE;
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
