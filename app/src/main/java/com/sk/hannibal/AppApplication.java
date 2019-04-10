package com.sk.hannibal;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.hannibal.scalpel.Hannibal;

import java.net.MulticastSocket;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


/**
 * Created by ola_sk on 2018/11/23.
 * Email: magicbaby810@gmail.com
 */
public class AppApplication extends MultiDexApplication {

    private static AppApplication instance;

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
