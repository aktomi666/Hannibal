package com.sk.hannibal;

import android.app.Application;
import android.content.Context;

import com.hannibal.scalpel.Hannibal;

import java.net.MulticastSocket;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


/**
 * Created by ola_sk on 2018/11/23.
 * Email: magicbaby810@gmail.com
 */
public class AppApplication extends MultiDexApplication {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Hannibal.init(this);

    }
}
