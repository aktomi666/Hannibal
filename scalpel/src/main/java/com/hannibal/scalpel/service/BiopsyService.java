package com.hannibal.scalpel.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.asynctask.DiseasedTissueTask;
import com.hannibal.scalpel.asynctask.TissueSampleTask;
import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.task.DiseasedTissueBeanExtensions;
import com.hannibal.scalpel.task.TissueSampleBeanExtensions;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;


/**
 *
 * 上传日志
 *
 * Created by sk ON 2018/11/17.
 * Email: magicbaby810@gmail.com
 */
public class BiopsyService extends IntentService {



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BiopsyService() {
        super("BiopsyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        CommonUtils.printDevLog("BiopsyService start working!");
        doWork();
    }

    private void doWork() {

        // ---------------- 埋点数据的上传  ----------------
        int counter = 0;
        TissueSampleBean tissueSampleBean = TissueSampleBeanExtensions.getTissueSample(Hannibal.getInstance(), 0);

        while (tissueSampleBean != null) {
            new TissueSampleTask().execute(tissueSampleBean);
            tissueSampleBean = TissueSampleBeanExtensions.getTissueSample(Hannibal.getInstance(), counter ++);
        }

        // ---------------- exception数据的上传  ----------------
        counter = 0;
        DiseasedTissueBean diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(Hannibal.getInstance(), 0);

        while (diseasedTissueTask != null) {

            new DiseasedTissueTask().execute(diseasedTissueTask);
            diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(Hannibal.getInstance(), counter ++);
        }
        CommonUtils.printDevLog("BiopsyService is done!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.printDevLog("BiopsyService is destroy!");
    }

}
