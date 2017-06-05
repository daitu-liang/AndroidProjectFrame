package com.kakaxi.fightdemo.network.config;


import com.kakaxi.fightdemo.network.ApiException;
import com.kakaxi.fightdemo.network.HttpResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by leixiaoliang on 2017/4/12.
 * 邮箱：lxliang1101@163.com
 */

public class HttpConfig {
    public static boolean DEBUG=true;
    //    Base地址
    public static final String BASE_URL = "https://webapi.hsuperior.com";
//    public static String BASE_URL="http://capi.douyucdn.cn";





    /**
     * rxjava进行线程切换复用，以及数据流转化
     * @param <T>
     * @return
     */
    public  static <T>ObservableTransformer<HttpResult<T>,T> toTransformer(){
      return   new ObservableTransformer<HttpResult<T>,T>(){
          @Override
          public ObservableSource<T> apply(Observable<HttpResult<T>> upstream) {
              return upstream.subscribeOn(Schedulers.io())
                      .unsubscribeOn(Schedulers.io())
                      .map(new Function<HttpResult<T>, T>() {//在这里把HttpResult<T>类型处理，返回T类型
                          @Override
                          public T apply(@NonNull HttpResult<T> tHttpResult) throws Exception {
                              if (tHttpResult.getErrcode()!=200) {
                                  throw new ApiException(tHttpResult.getErrcode(),tHttpResult.getErrmsg());
                              }
                              return tHttpResult.getResult();
                          }
                      })
                      .observeOn(AndroidSchedulers.mainThread());
          }
      };
    }



}
