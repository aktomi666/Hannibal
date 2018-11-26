package com.hannibal.scalpel.asynctask;

import android.os.AsyncTask;

import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.http.HttpManager;
import com.hannibal.scalpel.task.DiseasedTissueBeanExtensions;
import com.hannibal.scalpel.task.TissueSampleBeanExtensions;

import java.util.HashMap;

public class DiseasedTissueTask extends AsyncTask<DiseasedTissueBean, Void, Integer> {


    @Override
    protected Integer doInBackground(DiseasedTissueBean... diseasedTissueBeans) {

        DiseasedTissueBean diseasedTissueBean = diseasedTissueBeans[0];

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", diseasedTissueBean.getMessage());

        if (HttpManager.post(hashMap))
            return diseasedTissueBean.getId();
        else
            return -1;
    }

    @Override
    protected void onPostExecute(Integer id) {
        super.onPostExecute(id);
        if (id != -1) DiseasedTissueBeanExtensions.delete(Hannibal.getInstance(), id);
    }
}
