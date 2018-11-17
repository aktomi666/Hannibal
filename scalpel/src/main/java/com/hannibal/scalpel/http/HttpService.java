package com.hannibal.scalpel.http;

import com.hannibal.scalpel.bean.DiseasedTissueBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HttpService {

    /**
     * uoload crash log
     * @param diseasedTissueBean
     * @return
     */
    @FormUrlEncoded
    @POST("users/uploadDiseasedTissue")
    Call<DiseasedTissueBean> uploadDiseasedTissue(@Body DiseasedTissueBean diseasedTissueBean);
}
