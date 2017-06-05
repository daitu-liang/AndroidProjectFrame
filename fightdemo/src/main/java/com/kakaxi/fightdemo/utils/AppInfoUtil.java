package com.kakaxi.fightdemo.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by leixiaoliang  ddd on 2016/7/28.
 */
public class AppInfoUtil {

    private static final String TAG = AppInfoUtil.class.getSimpleName();
    private static Logger log = Logger.getLogger(TAG);

    /**
     * 判断 新版本 是否可用
     *
     * @param context     上下文对象
     * @param versionCode 新的版本号
     * @return
     */
    public static boolean isNewVersionAvailable(Context context, long versionCode) {
        long code = getVersionCode(context);
        return (versionCode > code);
    }

    public static String getVsersionName(Context context) {
        try {
            PackageInfo pi = getPackageInfo(context);
            return pi.versionName;
        } catch (Exception e) {
        	log.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * 返回当前包名
     *
     * @return
     */
    public static String getCurrentPkgName(Context context) {
        try {
            PackageInfo pi = getPackageInfo(context);
            return pi.packageName;
        } catch (Exception e) {
            log.e(TAG, "Exception " + e.toString());
        }
        return "";
    }

    public static long getVersionCode(Context context) {
        try {
            return getPackageInfo(context).versionCode;
        } catch (Exception e) {
            log.e(TAG, "判断版本号获取错误::" + e.toString());
        }
        return -1;
    }

    public static PackageInfo getPackageInfo(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        return pi;
    }

    /**
     * 获得manifase中得metadata数据
     * @param context
     * @param key 主键
     * @return 内容
     * @throws PackageManager.NameNotFoundException
     */
    public static String getMetaDate(Context context, String key) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(),
                        PackageManager.GET_META_DATA);
        return appInfo.metaData.getString(key);

    }
    
    /**
	 * 获取当前应用的code版本号android:versionCode
	 * @param context
	 * @return
	 */
	public static String getAppVersionCode(Context context) {
		try {
			String pkName = getAppPackageName(context);
			int versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            String mCode = String.valueOf(versionCode);
			return mCode;
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 获取当前应用包名
	 * @param context
	 * @return
	 */
	public static String getAppPackageName(Context context) {
		return context.getPackageName();
	}
    /**
     * 获取手机ip地址
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getAppDeviceName() {

        return android.os.Build.MODEL;
    }
    public static String getAppDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

}
