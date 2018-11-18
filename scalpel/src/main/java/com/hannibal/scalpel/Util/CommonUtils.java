package com.hannibal.scalpel.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hannibal.scalpel.BuildConfig;
import com.hannibal.scalpel.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {


    /**
     * 打印开发日志
     * @param format
     * @param objects
     */
    public static void printDevLog(String format, Object... objects) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(format)) {
            if (objects != null && objects.length > 0)
                Log.e(Constant.DevLogTag, String.format(format, objects));
            else
                Log.e(Constant.DevLogTag, format);
        }
    }

    /**
     * Check if the user is using the 2G mobile network connection.
     *
     * @param context
     * @return
     */
    public static boolean isUsing2GNetworkConnection(Context context) {
        ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectionManager.getActiveNetworkInfo();
        final int[] _2GNetworkTypes = {
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN
        };

        if (netInfo != null) {
            int networkSubType = netInfo.getSubtype();
            for (int networkType: _2GNetworkTypes) {
                if (networkType == networkSubType) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前时间 格式yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat df = getDateFormat(true);
        return df.format(new Date());
    }

    public static String getCurrentTime(boolean utc) {
        SimpleDateFormat df = getDateFormat(utc);
        return df.format(new Date());
    }

    private static SimpleDateFormat getDateFormat(boolean utc) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        if (utc) {
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return df;
    }

    /**
     * 字符串判空
     * @param str
     * @return
     */
    public static String checkNull(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }


    /**
     * 判断服务是否正在运行
     * @param service
     * @param context
     * @return
     */
    public static boolean isServiceRunning(String service, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) manager.getRunningServices(300);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(service)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 开启服务
     * @param context
     * @param tClass
     */
    public static void openService(Context context, Class<?> tClass) {
        String service = tClass.toString();
        if (!isServiceRunning(service, context)) {
            Intent intent = new Intent(context, tClass);
            context.startService(intent);
        }
    }

}
