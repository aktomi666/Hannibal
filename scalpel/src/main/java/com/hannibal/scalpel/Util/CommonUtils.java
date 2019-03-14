package com.hannibal.scalpel.Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.hannibal.scalpel.BuildConfig;
import com.hannibal.scalpel.Constant;

import java.io.IOException;
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
     * 检测网络连通性（是否能访问网络）
     * @return
     */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 223.5.5.5");
            int exitValue = ipProcess.waitFor();
            printDevLog("isNetworkOnline exitValue " + exitValue);
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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

            CommonUtils.printDevLog("BiopsyService start running!");
        } else {
            CommonUtils.printDevLog("BiopsyService is running!");
        }
    }


    /**
     * 判断是不是wifi网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isWifi(Context paramContext) {
        return "2".equals(getNetType(paramContext)[0]);
    }

    /**
     * 判断是不是2/3G网络状态
     *
     * @param paramContext
     * @return
     */
    public static boolean isMobile(Context paramContext) {
        return "1".equals(getNetType(paramContext)[0]);
    }

    /**
     * 网络是否可用
     *
     * @param paramContext
     * @return
     */
    public static boolean isNetAvailable(Context paramContext) {
        if ("1".equals(getNetType(paramContext)[0]) || "2".equals(getNetType(paramContext)[0])) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前网络状态 返回2代表wifi,1代表2G/3G
     *
     * @param paramContext
     * @return
     */
    public static String[] getNetType(Context paramContext) {
        String[] arrayOfString = {"Unknown", "Unknown"};
        PackageManager localPackageManager = paramContext.getPackageManager();
        if (localPackageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", paramContext.getPackageName()) != 0) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
        if (localConnectivityManager == null) {
            arrayOfString[0] = "Unknown";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo1 = localConnectivityManager.getNetworkInfo(1);
        if (localNetworkInfo1 != null && localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "2";
            return arrayOfString;
        }

        NetworkInfo localNetworkInfo2 = localConnectivityManager.getNetworkInfo(0);
        if (localNetworkInfo2 != null && localNetworkInfo2.getState() == NetworkInfo.State.CONNECTED) {
            arrayOfString[0] = "1";
            arrayOfString[1] = localNetworkInfo2.getSubtypeName();
            return arrayOfString;
        }

        return arrayOfString;
    }

}
