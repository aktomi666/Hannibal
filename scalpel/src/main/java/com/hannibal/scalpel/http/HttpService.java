package com.hannibal.scalpel.http;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Response;
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
    Flowable<Response<DiseasedTissueBean>> uploadDiseasedTissue(@Body DiseasedTissueBean diseasedTissueBean);

    /**
     * uoload 埋点 log
     * @param tissueSampleBean
     * @return
     */
    @FormUrlEncoded
    @POST("users/uploadTissueSample")
    Flowable<Response<TissueSampleBean>> uploadTissueSample(@Body TissueSampleBean tissueSampleBean);
}
