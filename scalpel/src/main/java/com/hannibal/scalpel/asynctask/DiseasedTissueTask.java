package com.hannibal.scalpel.asynctask;

import android.os.AsyncTask;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.http.HttpManager;

import java.util.HashMap;

public class DiseasedTissueTask extends AsyncTask<DiseasedTissueBean, Void, Void> {


    @Override
    protected Void doInBackground(DiseasedTissueBean... diseasedTissueBeans) {

        DiseasedTissueBean diseasedTissueBean = diseasedTissueBeans[0];

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", diseasedTissueBean.getMessage());

        HttpManager.post(hashMap);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
