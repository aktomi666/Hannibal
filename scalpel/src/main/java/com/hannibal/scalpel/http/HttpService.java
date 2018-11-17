package com.hannibal.scalpel.http;

import com.hannibal.scalpel.bean.DiseasedTissueBean;

import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<DiseasedTissueBean> uploadCrashReport(@Body DiseasedTissueBean crashReportBean);
}
