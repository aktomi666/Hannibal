package com.hannibal.scalpel.task;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.Util.TelephonyUtils;
import com.hannibal.scalpel.bean.CrashReportBean;
import com.hannibal.scalpel.http.HttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hannibal.scalpel.Constant.DevLogTag;

public class PickOutTask {

    private Context mContext;

    public PickOutTask() {

    }

    public PickOutTask(Context context) {
        this.mContext = context;
    }

    private CrashReportBean collectDataFromException(Throwable e) {

        CrashReportBean report = new CrashReportBean();

        String stackTraceInString = null;
        stackTraceInString = getStackTraceAsString(e);
        if (!TextUtils.isEmpty(stackTraceInString) && e.getCause() != null) {
            stackTraceInString = getStackTraceAsString(e.getCause());
        }

        report.stackTrace = stackTraceInString;
        report.exceptionType = e.getClass().getSimpleName();



        report.message = e.getMessage();


        if (CommonUtils.isUsing2GNetworkConnection(mContext)) {
            report.network = "2G";
            report.model = String.format("%s; %s", Build.MODEL, report.network);
        } else {
            report.model = Build.MODEL;
        }

        String appVersionName = null;
        report.osVersion = getVersion(appVersionName);
        report.manufacturer = Build.MANUFACTURER;
        report.timestamp = CommonUtils.getCurrentTime(false);
        report.imsiNo = TelephonyUtils.getInstance(mContext).getImsi(0);


        // waiting for redesign.
		/*HttpResult<WebApiResponse> result = WebApi.executePostJson(WebApi.JLR_CrashReport, report, false, WebApiResponse.class);

		if (AllianzRescueApplication.getInstance() != null && result.getResultType() != HttpResultType.Succeeded) {

			CrashReportDaoExtensions.create(AllianzRescueApplication.getInstance(), report);
		}*/

        return report;
    }

    private CrashReportBean collectDataFromLog(String str) {

        CrashReportBean report = new CrashReportBean();
        report.type = 1;
        report.message = str;

        if (CommonUtils.isUsing2GNetworkConnection(mContext)) {
            report.network = "2G";
            report.model = String.format("%s; %s", Build.MODEL, report.network);
        } else {
            report.model = Build.MODEL;
        }

        String appVersionName = null;
        report.osVersion = getVersion(appVersionName);
        report.manufacturer = Build.MANUFACTURER;
        report.timestamp = CommonUtils.getCurrentTime(false);
        report.imsiNo = TelephonyUtils.getInstance(mContext).getImsi(0);


        // waiting for redesign.
		/*HttpResult<WebApiResponse> result = WebApi.executePostJson(WebApi.JLR_CrashReport, report, false, WebApiResponse.class);

		if (AllianzRescueApplication.getInstance() != null && result.getResultType() != HttpResultType.Succeeded) {

			CrashReportDaoExtensions.create(AllianzRescueApplication.getInstance(), report);
		}*/

        return report;
    }

    private synchronized void upload(CrashReportBean crashReportBean) {

        HttpManager.getHttpService().uploadCrashReport(crashReportBean)
        .enqueue(new Callback<CrashReportBean>() {

            @Override
            public void onResponse(Call<CrashReportBean> call, Response<CrashReportBean> response) {
                Log.e(DevLogTag, "发送成功");
            }

            @Override
            public void onFailure(Call<CrashReportBean> call, Throwable t) {
                Log.e(DevLogTag, "发送失败");
            }
        });
    }

    public void collectDataAndUpload(Throwable e) {
        CrashReportBean crashReportBean = collectDataFromException(e);
        upload(crashReportBean);
    }

    public void collectDataAndUpload(String str) {
        CrashReportBean crashReportBean = collectDataFromLog(str);
        upload(crashReportBean);
    }

    private String getVersion(String appVersionName) {

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

                stacktraceBuilder.append(String.format("%s::%s %d \r\n",
                        getSimpleClassName(s.getClassName()),
                        s.getMethodName(),
                        s.getLineNumber()));
            }
        }

        return stacktraceBuilder.toString();
    }


    public static void hookClickEvents(View v) {
        Log.e("hookXM",  "hookClickEvents " + v);
        //Log.e("hookXM", andThis.toString() + " b");
    }

    public static void hookOnEvents(Object t, Object v, Object n) {

        String content = (null == t ? "" : t.toString())
                        + "/" + (null == v ? "" : v.toString())
                        + "/" + (null == n ? "" : n.toString());

        PickOutTask pickOutTask = new PickOutTask();
        pickOutTask.collectDataAndUpload(content);
        Log.e("hookXM", n + " " + v + " " + t);

        //Log.e("hookXM", andThis.toString() + " b");
    }

}
