package com.sk.hannibal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;


import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.service.BiopsyService;
import com.sk.hannibal.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends BaseActivity {

    @BindView(R.id.sdsdds)
    AppCompatTextView sss;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();

        try {
            new OkHttpClient.Builder()
                    .build()
                    .newCall(new Request.Builder()
                            .url("http://www.baidu.com")
                            .build()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.i("hi", "code:" + response.code());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.sdsdds)
    public void onClick(View v) {

    }

}

