package com.sk.scalpel;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.sk.scalpel.Util.CommonUtils;
import com.sk.scalpel.Util.TelephonyUtils;
import com.sk.scalpel.bean.CrashReportBean;
import com.sk.scalpel.http.HttpManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sk.scalpel.Constant.DevLogTag;

public class CrashReportTask {

    private Context mContext;

    public CrashReportTask(Context context) {
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

        String appVersionName = null;

        report.message = e.getMessage();


        if (CommonUtils.isUsing2GNetworkConnection(mContext)) {
            report.network = "2G";
            report.model = String.format("%s; %s", Build.MODEL, report.network);
        } else {
            report.model = Build.MODEL;
        }

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

    private void upload(CrashReportBean crashReportBean) {

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
        String parentPackageName = CrashReportTask.class.getPackage().getName();
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
}
