package com.kakaxi.fightdemo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class GetSign {
    private static final String TAG="GetSign";



    public static String giveSign(Map map) {
        StringBuffer sb = new StringBuffer();
        Collection<String> keyset = map.keySet();
        List list = new ArrayList<String>(keyset);
        Collections.sort(list);
        //这种打印出的字符串顺序和微信官网提供的字典序顺序是一致的
        for (int i = 0; i < list.size(); i++) {
//            String infoStr = map.get(list.get(i)).toString();
//            if(!TextUtils.isEmpty(infoStr)){
            if(map.get(list.get(i))!=null&&map.get(list.get(i)).toString()!="" ) {
                try {
                    System.out.println(list.get(i) + "=" + URLEncoder.encode(String.valueOf(map.get(list.get(i))), "utf-8"));
                    sb.append(list.get(i) + "=" + URLEncoder.encode(String.valueOf(map.get(list.get(i))), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        android.util.Log.i(TAG,"info="+ sb.toString());
        String smal = sb.toString().trim().toLowerCase();
        System.out.println(smal);
        String sign = MD5Utils.MD5(smal + MD5Utils._KEY).toLowerCase();
        System.out.println(sign);
        list.clear();
        return sign;
    }
}
