package com.kakaxi.fightdemo.network.api.commom;

import com.kakaxi.fightdemo.bean.NiuxInfo;
import com.kakaxi.fightdemo.network.HttpResult;
import com.kakaxi.fightdemo.network.api.NetApi;
import com.kakaxi.fightdemo.network.uploaddown.FileConverter;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by leixiaoliang on 2017/4/13.
 * 邮箱：lxliang1101@163.com
 */

public interface ApiCommom {

    @GET(NetApi.UGET_NUIX)
    Observable<HttpResult<NiuxInfo>> getNunix();

    /**
     * 上传单文件
     * @param file
     * @return
     */
    @Multipart
    @POST(NetApi.UPLOAD_FILE)
    Observable<ResponseBody> upLoadFile( @Part MultipartBody.Part file);
    /**
     * 上传带参Part
     * @param file
     * @return
     */
    @Multipart
    @POST(NetApi.UPLOAD_FILE)
    Observable<ResponseBody> upLoadFile(@Part("description")RequestBody description, @Part MultipartBody.Part file);

    /**
     * 带参上传
     * @param map
     * @param file
     * @return
     */

    @Multipart
    @POST(NetApi.UPLOAD_FILE)
    Observable<ResponseBody> upLoadFile(@QueryMap Map<String,String> map, @Part MultipartBody.Part file);

    /**
     * 上传多文件
     * @param map
     * @return
     */
    @Multipart
    @POST(NetApi.UPLOAD_FILE)
    Observable<ResponseBody> upLoadMutiFile(@PartMap Map<String, RequestBody> map);

    /**
     * 断点续传下载接口
    */
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<File> downloadFile(@Url String fileUrl, @Header(FileConverter.SAVE_PATH) String path);

    @GET
    Call<File> downloadFileCall(@Url String fileUrl, @Header(FileConverter.SAVE_PATH) String path);
}
