package com.hannibal.scalpel.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.http.HttpManager;
import com.hannibal.scalpel.task.DiseasedTissueBeanExtensions;
import com.hannibal.scalpel.task.TissueSampleBeanExtensions;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hannibal.scalpel.Constant.DevLogTag;

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
    public BiopsyService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        // ---------------- 埋点数据的上传  ----------------
        int counter = 1;
        TissueSampleBean tissueSampleBean = TissueSampleBeanExtensions.getTissueSample(this, 0);

        while (tissueSampleBean != null) {
            upload(tissueSampleBean);
            tissueSampleBean = TissueSampleBeanExtensions.getTissueSample(this, counter ++);
        }

        // ---------------- exception数据的上传  ----------------
        counter = 1;
        DiseasedTissueBean diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(this, 0);

        while (diseasedTissueTask != null) {
            upload(diseasedTissueTask);
            diseasedTissueTask = DiseasedTissueBeanExtensions.getDiseasedTissue(this, counter ++);
        }
    }

    private void upload(final TissueSampleBean tissueSampleBean) {
        HttpManager.getHttpService().uploadTissueSample(tissueSampleBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TissueSampleBean>>() {

                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Response<TissueSampleBean> diseasedTissueBeanResponse) {
                        TissueSampleBeanExtensions.delete(BiopsyService.this, tissueSampleBean.id);
                        Log.e(DevLogTag, "发送成功");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(DevLogTag, "发送失败");
                    }

                    @Override
                    public void onComplete() {

                    }

                });
    }

    private void upload(final DiseasedTissueBean diseasedTissueBean) {
        HttpManager.getHttpService().uploadDiseasedTissue(diseasedTissueBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<DiseasedTissueBean>>() {

                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Response<DiseasedTissueBean> diseasedTissueBeanResponse) {
                        DiseasedTissueBeanExtensions.delete(BiopsyService.this, diseasedTissueBean.id);
                        Log.e(DevLogTag, "发送成功");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(DevLogTag, "发送失败");
                    }

                    @Override
                    public void onComplete() {

                    }

                 });
    }

}
