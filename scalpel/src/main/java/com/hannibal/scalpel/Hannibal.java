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
import com.taobao.android.dexposed.DexposedBridge;
import com.taobao.android.dexposed.XC_MethodHook;
import com.taobao.android.dexposed.utility.Logger;

public class Hannibal extends Application {

    private static Context mContext;
    private static Hannibal handler = new Hannibal();
    private static final int JOB_ID = 1000;

    public static Context getInstance() {
        return mContext;
    }

    public static void init(Context context) {
        mContext = context;

        // 全局捕获Exce
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                ExceptionsHandlingService.handleException(e);
            }
        });

        DexposedBridge.findAndHookMethod(ViewGroup.class, "dispatchTouchEvent", MotionEvent.class, new MotionEventMethodHook());

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
