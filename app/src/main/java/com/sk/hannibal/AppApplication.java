package com.sk.hannibal;

import android.app.Application;

import com.hannibal.scalpel.Hannibal;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Hannibal.init(this);
        //Hannibal.testCrash();
    }
}
