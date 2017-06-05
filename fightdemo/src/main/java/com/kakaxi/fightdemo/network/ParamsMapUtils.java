package com.kakaxi.fightdemo.network;

import com.kakaxi.fightdemo.utils.GetSign;

import java.util.Map;

/**
 * Created by leixiaoliang on 2017/4/13.
 * 邮箱：lxliang1101@163.com
 */

public class ParamsMapUtils extends BaseParamsMapUtil {

    private static Map<String, String> mapParams;

    /**
     * 默认参数
     * @return
     */
    public static Map<String, String> getDefaultParams() {
        return getParamsMap();
    }

    public static Map<String, String> setLoginParams(String phone,String pwd,String screen_size,String dev_num,String dev_name,String id) {
        mapParams = getDefaultParams();
        mapParams.put("dev_name", dev_name);
        mapParams.put("dev_num", dev_num);
        mapParams.put("ip", id);
        mapParams.put("mobile", phone);
        mapParams.put("password", pwd);
        mapParams.put("screen_size", screen_size);
        mapParams.put("sign", GetSign.giveSign(mapParams));
        return mapParams;
    }

    public static Map<String, String> setUpLoad(String screen_size,String id) {
        mapParams = getDefaultParams();

        mapParams.put("u_guid", id);
        mapParams.put("screen_size", screen_size);
        mapParams.put("sign", GetSign.giveSign(mapParams));
        return mapParams;
    }
}
