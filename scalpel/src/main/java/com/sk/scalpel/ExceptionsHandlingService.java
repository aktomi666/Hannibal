package com.sk.scalpel;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.sk.scalpel.Util.CommonUtils;
import com.sk.scalpel.Util.TelephonyUtils;
import com.sk.scalpel.bean.CrashReportBean;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

public class ExceptionsHandlingService {

    /**
     * Display exception to log console if in development mode.
     * If param reportToServer is true, report the exception to backend server.
     *
     * @param exception
     *
     */

    public static void handleException(Context context, Throwable exception) {
        if (exception == null || shouldExceptionBeIgnored(exception)) {
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.e(Constant.DevLogTag, exception.getMessage(), exception);
        } else {
            reportExceptionToServer(context, exception);
        }
    }

    /**
     * Check if the type of exception is in the list of exception types to be ignored.
     *
     * @param exception
     * @return
     */
    private static boolean shouldExceptionBeIgnored(Throwable exception) {
        final Class<?>[] ignoreList = {
                ConnectException.class,
                UnknownHostException.class,
                ConnectTimeoutException.class,
                SocketException.class,
                SocketTimeoutException.class
        };
        for (Class<?> clazz: ignoreList) {
            if (clazz.equals(exception.getClass())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Invoke the async task to submit the exception infomation to backend server.
     * @param context
     * @param e
     */
    private static void reportExceptionToServer(Context context, Throwable e) {

        CrashReportTask crashReportTask = new CrashReportTask(context);
        crashReportTask.collectDataAndUpload(e);
    }

}
