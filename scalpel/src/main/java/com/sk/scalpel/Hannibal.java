package com.sk.scalpel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.sk.scalpel.Util.CommonUtils;

import static com.sk.scalpel.Constant.DevLogTag;

public class Hannibal {

    private static Context mContext;

    private static Hannibal crashHandler = new Hannibal();

    public static Hannibal getInstance() {
        return crashHandler;
    }

    public static void init(final Application context) {
        mContext = context;

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ExceptionsHandlingService.handleException(context, e);
            }
        });

        Log.i(DevLogTag, "init crash success");
    }

    public static void testCrash() {
        try {
            String ss = null;
            ss = ss.trim();
        } catch (Exception e) {
            ExceptionsHandlingService.handleException(mContext, e);
            Log.i(DevLogTag, "test crash success");
        }
    }

}
