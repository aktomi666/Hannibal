package com.hannibal.scalpel.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtils {



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

}
