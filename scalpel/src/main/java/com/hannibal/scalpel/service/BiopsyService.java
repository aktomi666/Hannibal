package com.hannibal.scalpel.service;

import android.content.Context;

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


/**
 *
 * 上传日志
 *
 * Created by sk ON 2018/11/17.
 * Email: magicbaby810@gmail.com
 */
public class BiopsyService {

    private static final long PollingSleepIntervalInMilliSeconds = (long) 30 * 1000;

    private Context context;
    private Timer timer;
    private int count = 0;
    private boolean isRequesting = false;

    public BiopsyService() {

    }

    // 启动轮询
    public void startPolling() {
        if (null == timer) doWork();
    }

    // 停止派单轮询
    public void stopPolling() {
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    private void doWork() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override public void run() {

                if (CommonUtils.isNetworkOnline() && !isRequesting)
                    upload();
            }

        }, 0, PollingSleepIntervalInMilliSeconds);
    }


    private void upload() {
        isRequesting = true;

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

        isRequesting = false;
    }

}
