package com.kakaxi.fightdemo.network.uploaddowon;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by leixiaoliang on 2017/4/15.
 * 邮箱：lxliang1101@163.com
 */

public class ServiceGenerator {
    private static final String HOST = "http://192.168.1.115:8080/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(FileConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    public static <T> T createService(Class<T> tClass){
        return builder.build().create(tClass);
    }
    /**
     * 创建带响应进度(下载进度)回调的service
     */
    public static <T> T createDownloadService(Class<T> tClass, DownloadProgressListener listener){
        OkHttpClient client = HttpClientHelper.addProgressDownloadListener(new OkHttpClient.Builder(),listener).build();
        return builder
                .client(client)
                .build()
                .create(tClass);
    }
    /**
     * 创建带请求体进度(上传进度)回调的service
     */
    public static <T> T createUploadService(Class<T> tClass, UploadProgressListener listener){
        OkHttpClient client = HttpClientHelper.addProgressUploadListener(new OkHttpClient.Builder(),listener).build();
        return builder
                .client(client)
                .build()
                .create(tClass);
    }
}
