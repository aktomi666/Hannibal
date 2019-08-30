package com.hannibal.scalpel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import dalvik.system.DexFile;

import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.hook.MotionEventMethodHook;
import com.hannibal.scalpel.task.ExceptionsHandlingService;
import com.sk.commons.BaseApplication;

/**
 * @author sk
 */
public class Hannibal extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        // 全局捕获Exce
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ExceptionsHandlingService.handleException(e);
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
