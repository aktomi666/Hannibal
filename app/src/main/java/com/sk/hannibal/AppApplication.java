package com.sk.hannibal;

import android.app.Application;

import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.task.PickOutTask;

/**
 * Created by ola_sk on 2018/11/23.
 * Email: magicbaby810@gmail.com
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hannibal.init(this);
    }
}
