package com.hannibal.scalpel.http;

import com.hannibal.scalpel.bean.CrashReportBean;

import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpService {

    /**
     * uoload crash log
     * @param crashReportBean
     * @return
     */
    @FormUrlEncoded
    @POST("users/uploadCrashReport")
    Call<CrashReportBean> uploadCrashReport(@Body CrashReportBean crashReportBean);
}
