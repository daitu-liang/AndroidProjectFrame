package com.kakaxi.fightdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * <dl>
 * <dt>NetWorkUtil.java</dt>
 * <dd>Description:网络操作工具类</dd>

 */
public class NetWorkUtil {
    private static Logger log = Logger.getLogger("NetWorkUtil");
	//接入点APN名称
    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String NET_3G = "3gnet";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String UNINET = "uninet";
    
    //网络类型(电信3G网络\移动2G网络等)
    public static final int TYPE_NET_WORK_DISABLED = 0;
    public static final int TYPE_WIFI = 4;
    public static final int TYPE_DX_3G = 5;// 电信3G网络
    public static final int TYPE_YD_3G = 6;// 移动3G网络
    public static final int TYPE_LT_3G = 7;// 联通3G网络
    public static final int TYPE_DX_2G = 8;// 电信2G网络
    public static final int TYPE_YD_2G = 9;// 移动2G网络
    public static final int TYPE_LT_2G = 10;// 联通2G网络
    public static final int TYPE_LTE = 11;// 4G网络
    public static final int TYPE_OTHER = 12;// 其他
    
    //获取APN的所需地址URL
    public static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");
    
    //是否手动打开了wifi网络
    public static boolean isMyOpenWiFi = false;

    /**
     * 客户端提示网络类型
     */
    public static void checkNet() {
    }


    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return TYPE_NET_WORK_DISABLED;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return TYPE_WIFI;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
                    boolean is3G = isFastMobileNetwork(mContext);
                    
                    // 4G网络
                    if(is3G && is4GNetConnect(mContext)){
                    	return TYPE_LTE;
                    }
                    
                    Cursor c = null;
                    try {
                        c = mContext.getContentResolver().query(
                                PREFERRED_APN_URI, null, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                            final String user = c.getString(c
                                    .getColumnIndex("user"));
                            /*LogUtil.d("NetWorkUtil", "checkNetworkType",
                                    "user网络类型：" + user);*/
                            if (!TextUtils.isEmpty(user)) {
                                if (user.startsWith(CTWAP)
                                        || user.startsWith(CTNET)) {
                                    if (is3G) {
                                        c.close();
                                        c = null;
                                        return TYPE_DX_3G;
                                    } else {
                                        c.close();
                                        c = null;
                                        return TYPE_DX_2G;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.e("","查询user网络类型异常", e);
                    } finally {
                        if (c != null) {
                            c.close();
                            c = null;
                        }
                    }
                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断
                    String netMode = mobNetInfoActivity.getExtraInfo();
                    log.d("","netMode:" + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();
                        // 判断移动的网络类型
                        if (netMode.equals(CMWAP) || netMode.equals(CMNET)) {
                            if (is3G) {
                                return TYPE_YD_3G;
                            } else {
                                return TYPE_YD_2G;
                            }
                            // 判断联通的网络类型
                        } else if ((netMode.equals(NET_3G) || netMode
                                .equals(UNINET))
                                || (netMode.equals(WAP_3G) || netMode
                                        .equals(UNIWAP))) {
                            if (is3G) {
                                return TYPE_LT_3G;
                            } else {
                                return TYPE_LT_2G;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.e("","Exception", ex);
            return TYPE_OTHER;
        }
        return TYPE_OTHER;
    }

    private static boolean is4GNetConnect(Context context){
    	TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = telephonyManager.getNetworkType();
        if(TelephonyManager.NETWORK_TYPE_LTE == networkType){
        	return true;
        }    
        return false;
    }
    
    private static boolean isFastMobileNetwork(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = telephonyManager.getNetworkType();
        log.d("","networkType:" + networkType);

        switch (networkType) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
            // 电信2g
            return false; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
            // 移动2g
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
            // 版本0.（电信3g）
            return true; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
            // 版本A （电信3g）
            return true; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
            // 版本B（电信3g）
            return true; // ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
            // GPRS （联通2g）
            return false; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
            // HSDPA（联通3g）
            return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
            // UMTS（联通3g）
            return true; // ~ 400-7000 kbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
            return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
            return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_EHRPD:
            return true; // ~ 1-2 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPAP:
            return true; // ~ 10-20 Mbps
        case TelephonyManager.NETWORK_TYPE_IDEN:
            return false; // ~25 kbps
        case TelephonyManager.NETWORK_TYPE_LTE:
            return true; // ~ 10+ Mbps
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            return false;
        default:
            return true;
        }
    }

    /**
     * @Title: isNetworkConnected
     * @Description: 是否有网络连接
     * @param context
     * @return true 有网络连接；false 没有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 判断有网络连接，并且不是wifi
     */
    public static boolean isNotWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                // 有网络连接
                int netType = mNetworkInfo.getType();
                if (netType != ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @author: lihs
     * @Title: isWifiConnected
     * @Description: wifi 是否连接上
     * @param context
     * @return
     * @date: 2013-8-22 下午2:20:25
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                if (NetworkInfo.State.CONNECTED == mWiFiNetworkInfo.getState()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * @author: lihs
     * @Title: isMobileConnected
     * @Description:
     * @param context
     * @return
     * @date: 2013-8-2 下午2:00:03
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                if (NetworkInfo.State.CONNECTED == mMobileNetworkInfo
                        .getState()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * @author: lihs
     * @Title: getConnectedType
     * @Description: 获取当前网络类型
     * @param context
     * @return
     * @date: 2013-8-2 下午2:00:19
     */
    public static int getConnectedType(Context context) {
        if (context != null) {

            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }


    /**
     * @author: lihs
     * @Title: scanAroundInterNet
     * @Description: 扫描周围网络，扫描成功后系统会异步发送广播
     *               WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
     * @param context
     * @date: 2013-8-20 下午12:39:28
     */
    public static void scanAroundInterNet(Context context) {
        if (isNetworkConnected(context)) {
            if (!isWifiConnected(context)) {
                // 有网络连接但不是Wifi连接 打开Wifi开始扫描网络
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
                isMyOpenWiFi = true;
                wifiManager.startScan();
            }
        }
    }
    public static boolean WIFI_IS_ALERT=false;
    /**
     * @author: lihs
     * @Title: isAroundWifiInternet
     * @Description: 检测附近有没有wifi网络
     * @date: 2013-8-19 下午3:32:05
     */
    public static void isAroundWifiInternet(final Activity activity) {}

    /**
     *
     * @param activity
     */
    public static void networkError(final Activity activity) {}
    public static boolean isReachableByHost(String host, int timeout)
			throws UnknownHostException, IOException {
		return InetAddress.getByName(host).isReachable(timeout);
	}
}
