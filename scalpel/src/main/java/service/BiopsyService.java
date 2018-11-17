package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.task.DiseasedTissueBeanExtensions;
import com.hannibal.scalpel.task.PhotoUploadingWorker;
import com.hannibal.scalpel.task.TaskUploadWorkerBase;

/**
 *
 * 上传日志
 *
 * Created by ola_sk ON 2018/11/17.
 * Email: magicbaby810@gmail.com
 */
public class BiopsyService extends IntentService {

    private static final long delayTime = 60 * 1000;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BiopsyService(String name) {
        super(name);
    }


    @Override protected void onHandleIntent(Intent intent) {

        int counter = 1;
        DiseasedTissueBean diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(this, 0);

        while (diseasedTissueTask != null) {
            TaskUploadWorkerBase uploadWorker = new PhotoUploadingWorker();
            uploadWorker.setContext(getApplicationContext());
            uploadWorker.doUpload(diseasedTissueTask);

            diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(this, counter ++);
        }
    }

}
