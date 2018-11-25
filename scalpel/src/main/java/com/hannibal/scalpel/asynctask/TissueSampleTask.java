package com.hannibal.scalpel.asynctask;

import android.os.AsyncTask;

import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.http.HttpManager;
import com.hannibal.scalpel.service.BiopsyService;
import com.hannibal.scalpel.task.TissueSampleBeanExtensions;

import java.util.HashMap;

public class TissueSampleTask extends AsyncTask<TissueSampleBean, Void, Integer> {


    @Override
    protected Integer doInBackground(TissueSampleBean... tissueSampleBeans) {

        TissueSampleBean tissueSampleBean = tissueSampleBeans[0];

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sample_path", tissueSampleBean.getSamplePath());

        if (HttpManager.post(hashMap))
            return tissueSampleBean.getId();
        else
            return -1;
    }

    @Override
    protected void onPostExecute(Integer id) {
        super.onPostExecute(id);
        if (id != -1) TissueSampleBeanExtensions.delete(Hannibal.getInstance(), id);
    }
}
