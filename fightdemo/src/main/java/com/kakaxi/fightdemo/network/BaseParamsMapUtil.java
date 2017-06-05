package com.kakaxi.fightdemo.network;

import com.kakaxi.fightdemo.app.FightApplication;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by leixiaoliang on 2017/4/13.
 * 邮箱：lxliang1101@163.com
 */

public class BaseParamsMapUtil {
    /**
     * 公共参数
     * @return
     */
    public static Map<String, String> getParamsMap() {
        Map<String, String> paramsmap = new LinkedHashMap<>();

        String from = "2";//不可空，1、IOS  2、安卓  3、PC
        String sys_version = android.os.Build.VERSION.RELEASE + "";//
        String nunix = FightApplication.getPreferenceManager().getSaveNunix();//unix时间戳	不可空
        String version = "1.0.1";//APP版本名称
        paramsmap.put("from", from);
        paramsmap.put("sys_version", sys_version);
        paramsmap.put("version", version);
        paramsmap.put("nunix", nunix);
        return paramsmap;
    }
}
