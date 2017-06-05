package com.kakaxi.fightdemo.network.uploaddowon;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by leixiaoliang on 2017/4/15.
 * 邮箱：lxliang1101@163.com
 */

public class HttpClientHelper {


    /**
     * 获取添加了自定义Response的okhttp builder
     * @return okhttp.builder
     */
    public static OkHttpClient.Builder getOkHttpClientBuilder(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return addProgressDownloadListener(client,null);
    }


    /**
     * 为okhttp.builder添加自定义response
     * @param builder okhttp.builder
     * @return 添加自定义response后的okhttp.builder
     */
    public static OkHttpClient.Builder addCustomResponse(OkHttpClient.Builder builder){
        return addProgressDownloadListener(builder,null);
    }


    /**
     * 包装OkHttpClient，用于下载文件的回调
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient builder，使用clone方法返回
     */
    public static OkHttpClient.Builder addProgressDownloadListener(OkHttpClient.Builder builder,final DownloadProgressListener progressListener){
        //增加拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //拦截
                Response originalResponse = chain.proceed(request);
                List<String> segments = request.url().pathSegments();
                String filename = segments.get(segments.size()-1);

                DownloadResponseBody body = new DownloadResponseBody(originalResponse.body(), progressListener);
                //从request中取出对应的header即我们设置的文件保存地址,然后保存到我们自定义的response中
                body.setSavePath(request.header(FileConverter.SAVE_PATH));
                body.setFileName(filename);
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(body)
                        .build();
            }
        });
        return builder;
    }


    /**
     * 包装OkHttpClient，用于上传文件进度的回调
     * @param progressListener 请求进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient.Builder addProgressUploadListener(OkHttpClient.Builder builder, final UploadProgressListener progressListener){
        //增加拦截器
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .method(original.method(), new UploadRequestBody(original.body(),progressListener))
                        .build();
                return chain.proceed(request);
            }
        });
        return builder;
    }


}
