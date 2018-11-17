package com.hannibal.scalpel;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.hannibal.scalpel.task.ExceptionsHandlingService;

import static com.hannibal.scalpel.Constant.DevLogTag;

public class Hannibal extends Application {

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
                ExceptionsHandlingService.handleException(e);
            }
        });

        Log.i(DevLogTag, "init crash success");
    }

    public static void testCrash() {
        try {
            String ss = null;
            ss = ss.trim();
        } catch (Exception e) {
            ExceptionsHandlingService.handleException(e);
            Log.i(DevLogTag, "test crash success");
        }
    }

}
