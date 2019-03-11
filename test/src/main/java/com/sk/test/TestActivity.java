package com.sk.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by ola_sk on 2019/3/4.
 * Email: magicbaby810@gmail.com
 */
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton testBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        testBtn = findViewById(R.id.test_btn);
        testBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

    }
}
