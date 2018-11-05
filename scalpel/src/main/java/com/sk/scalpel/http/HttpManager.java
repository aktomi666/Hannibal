package com.sk.scalpel.http;


import com.sk.scalpel.BuildConfig;
import com.sk.scalpel.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {

    private final static int CONNECT_TIMEOUT = 15;
    private final static int READ_TIMEOUT = 20;
    private final static int WRITE_TIMEOUT = 20;

    private static HttpService mHttpServiceApi;

    private static Retrofit getRetrofit() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // Log
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient okHttpClient = okHttpClientBuilder.addInterceptor(logging)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                //.retryOnConnectionFailure(true)//错误重连
                //.cache(cache)
                .build();


        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL);

        return builder.build();
    }


    /**
     * 获取api实例
     */
    public static HttpService getHttpService() {
        if (mHttpServiceApi == null) {
            mHttpServiceApi = getRetrofit().create(HttpService.class);
        }
        return mHttpServiceApi;
    }



}
