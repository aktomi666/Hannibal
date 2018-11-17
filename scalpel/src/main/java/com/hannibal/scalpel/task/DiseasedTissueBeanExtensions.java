package com.hannibal.scalpel.task;

import android.content.Context;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.database.DiseasedTissueDao;


public class DiseasedTissueBeanExtensions {
	
	
	public static synchronized void create(Context context, DiseasedTissueBean report) {

		DiseasedTissueDao dao = null;
		try {
			dao = new DiseasedTissueDao();
			dao.open(context);
			dao.create(report);
		} catch (Exception e) {
			ExceptionsHandlingService.handleException(e);
		} finally {
			if (dao != null) {
				dao.close();
			}
		}
	}
	
	public static void delete(Context context, int id) {
		DiseasedTissueDao dao = null;
		try {
			dao = new DiseasedTissueDao();
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
	
	public static DiseasedTissueBean getDiseasedTissue(Context context, int offset) {

		DiseasedTissueDao dao = null;
		try {
			dao = new DiseasedTissueDao();
			dao.open(context);
			return dao.getDiseasedTissue(offset);
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
