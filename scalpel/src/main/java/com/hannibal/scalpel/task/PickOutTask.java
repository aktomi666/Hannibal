package com.hannibal.scalpel.task;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.service.BiopsyService;

import java.util.Locale;

public class PickOutTask {

    private static PickOutTask instance;

    public static PickOutTask getInstance() {
        if (null == instance) {
            synchronized (PickOutTask.class) {
                if (null == instance) {
                    instance = new PickOutTask();
                }
            }
        }
        return instance;
    }

    private DiseasedTissueBean collectDataFromException(Throwable e) {

        DiseasedTissueBean report = new DiseasedTissueBean();

        String stackTraceInString = null;
        stackTraceInString = getStackTraceAsString(e);
        if (!TextUtils.isEmpty(stackTraceInString) && e.getCause() != null) {
            stackTraceInString = getStackTraceAsString(e.getCause());
        }

        report.setStackTrace(stackTraceInString);
        //report.setExceptionType(e.getClass().getSimpleName());



        report.setMessage(e.getMessage());


//        if (CommonUtils.isUsing2GNetworkConnection(mContext)) {
//            report.network = "2G";
//            report.model = String.format("%s; %s", Build.MODEL, report.network);
//        } else {
//            report.model = Build.MODEL;
//        }

        String appVersionName = null;
        report.setOsVersion(getVersion(appVersionName));
        report.setManufacturer(Build.MANUFACTURER);
        report.setTimestamp(CommonUtils.getCurrentTime(false));
        //report.imsiNo = TelephonyUtils.getInstance(mContext).getImsi(0);

        return report;
    }

    private static TissueSampleBean collectDataFromLog(String str) {

        TissueSampleBean report = new TissueSampleBean();
        report.setType(1);
        report.setSamplePath(str);

//        if (CommonUtils.isUsing2GNetworkConnection(mContext)) {
//            report.network = "2G";
//            report.model = String.format("%s; %s", Build.MODEL, report.network);
//        } else {
//            report.model = Build.MODEL;
//        }

        String appVersionName = null;
        report.setOsVersion(getVersion(appVersionName));
        report.setManufacturer(Build.MANUFACTURER);
        report.setTimestamp(CommonUtils.getCurrentTime(false));
        //report.imsiNo = TelephonyUtils.getInstance(mContext).getImsi(0);


        return report;
    }

    public void collectData(Throwable e) {
        DiseasedTissueBean diseasedTissueBean = collectDataFromException(e);
        DiseasedTissueBeanExtensions.create(Hannibal.getInstance(), diseasedTissueBean);

        CommonUtils.openService(Hannibal.getInstance(), BiopsyService.class);
    }

    private void collectData(String str) {
        TissueSampleBean tissueSampleBean = collectDataFromLog(str);
        TissueSampleBeanExtensions.create(Hannibal.getInstance(), tissueSampleBean);

        //CommonUtils.openService(Hannibal.getInstance(), BiopsyService.class);
    }

    private static String getVersion(String appVersionName) {

        String versionString = null;
        if (TextUtils.isEmpty(appVersionName)) {
            versionString = Build.VERSION.RELEASE;
        } else {
            versionString = String.format("%s; %s", Build.VERSION.RELEASE, appVersionName);
        }
        return versionString;
    }

    private String getSimpleClassName(String fullClassName) {

        int lastDotIndex = fullClassName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fullClassName.substring(lastDotIndex + 1);
        } else {
            return fullClassName;
        }
    }

    private String getStackTraceAsString(Throwable e) {

        if (e == null) {
            return null;
        }

        StringBuilder stacktraceBuilder = new StringBuilder();
        StackTraceElement[] stacks = e.getStackTrace();
        String parentPackageName = PickOutTask.class.getPackage().getName();
        int lastDotIndex = parentPackageName.lastIndexOf(".");
        parentPackageName = parentPackageName.substring(0, lastDotIndex);

        for (StackTraceElement s: stacks) {

            if (s.getClassName().startsWith(parentPackageName)) {

                stacktraceBuilder.append(String.format(Locale.CHINA,"%s::%s %d \r\n",
                        getSimpleClassName(s.getClassName()),
                        s.getMethodName(),
                        s.getLineNumber()));
            }
        }

        return stacktraceBuilder.toString();
    }


    /**
     * 接收触摸埋点数据
     * @param t
     * @param v
     * @param n
     */
    public static void hookOnTouchEvents(Object t, Object v, Object n) {

        String content = (null == t ? "" : t.toString())
                        + "/" + (null == v ? "" : v.toString())
                        + "/" + (null == n ? "" : n.toString());

        if (t instanceof View) {
            View view = (View) t;
            while (null != view.getParent()) {
                ViewParent viewParent = view.getParent();
                //CommonUtils.printDevLog(viewParent);
            }
            //view.getParent().getParent().getParent().getParent().getParent()
        }

        CommonUtils.printDevLog(n + " " + v + " " + t);

        PickOutTask.getInstance().collectData(content);
    }

    /**
     * 接收点击埋点数据
     * @param t
     * @param v
     * @param n
     */
    public static void hookOnClickEvents(Object t, Object v, Object n) {

        String content = (null == t ? "" : t.toString())
                + "/" + (null == v ? "" : v.toString())
                + "/" + (null == n ? "" : n.toString());

        CommonUtils.printDevLog(n + " " + v + " " + t);

        PickOutTask.getInstance().collectData(content);
    }

}