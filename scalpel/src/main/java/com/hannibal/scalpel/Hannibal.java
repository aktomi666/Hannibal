package com.hannibal.scalpel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.multidex.MultiDexApplication;

import dalvik.system.DexFile;

import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.task.ExceptionsHandlingService;

/**
 * @author sk
 */
public class Hannibal {


    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    public static void init(Context context) {
        mContext = context;

        // 全局捕获Exce
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                //ExceptionsHandlingService.handleException(e);
            }
        });

        CommonUtils.printDevLog("init hannibal success");
    }

    public static void testCrash() {
        try {
            String ss = null;
            ss = ss.trim();
        } catch (Exception e) {
            ExceptionsHandlingService.handleException(e);
            CommonUtils.printDevLog( "test crash success!");
        }
    }

}
