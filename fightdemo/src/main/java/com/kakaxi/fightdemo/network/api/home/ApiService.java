package com.kakaxi.fightdemo.network.api.home;


import com.kakaxi.fightdemo.bean.LoginBean;
import com.kakaxi.fightdemo.network.HttpResult;
import com.kakaxi.fightdemo.network.api.NetApi;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by leixiaoliang on 2017/4/12.
 * 邮箱：lxliang1101@163.com
 */

public interface ApiService {

    @FormUrlEncoded
    @POST(NetApi.USER_LOGIN)
    Observable<HttpResult<LoginBean>> getUserInfo(@FieldMap() Map<String, String> map);


}
