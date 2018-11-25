package com.hannibal.scalpel.task;

import android.content.Context;

import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.database.DiseasedTissueDao;
import com.hannibal.scalpel.database.TissueSampleDao;


public class TissueSampleBeanExtensions {
	
	
	public static synchronized void create(Context context, TissueSampleBean report) {

		TissueSampleDao dao = null;
		try {
			dao = new TissueSampleDao();
			dao.open(context);
			long isSuccess = dao.create(report);
			CommonUtils.printDevLog(isSuccess+"");
		} catch (Exception e) {
			ExceptionsHandlingService.handleException(e);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	
	public static void delete(Context context, int id) {
		TissueSampleDao dao = null;
		try {
			dao = new TissueSampleDao();
			dao.open(context);
			dao.delete(id);
		} catch (Exception e) {
			ExceptionsHandlingService.handleException(e);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	
	public static TissueSampleBean getTissueSample(Context context, int offset) {

		TissueSampleDao dao = null;
		try {
			dao = new TissueSampleDao();
			dao.open(context);
			return dao.getTissueSample(offset);
		} catch (Exception e) {
			ExceptionsHandlingService.handleException(e);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
		return null;
	}
	
}
