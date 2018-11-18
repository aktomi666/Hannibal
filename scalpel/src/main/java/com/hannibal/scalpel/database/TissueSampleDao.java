package com.hannibal.scalpel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.bean.TissueSampleBean;
import com.hannibal.scalpel.task.ExceptionsHandlingService;

import java.util.Locale;

public class TissueSampleDao extends DataAccessObject {

	private TissueSampleBean readTissueSample(Cursor cursor) {

		TissueSampleBean tissueSampleBean = new TissueSampleBean();

		tissueSampleBean.setId(cursor.getInt(cursor.getColumnIndex("ID")));
		tissueSampleBean.setTaskId(cursor.getString(cursor.getColumnIndex("TaskId")));
		tissueSampleBean.setExceptionType(cursor.getString(cursor.getColumnIndex("ExceptionType")));
		tissueSampleBean.setSamplePath(cursor.getString(cursor.getColumnIndex("SamplePath")));
		tissueSampleBean.setStackTrace(cursor.getString(cursor.getColumnIndex("StackTrace")));
		tissueSampleBean.setImsiNo(cursor.getString(cursor.getColumnIndex("IMSI")));
		tissueSampleBean.setOsVersion(cursor.getString(cursor.getColumnIndex("OsVersion")));
		tissueSampleBean.setAppVersion(cursor.getString(cursor.getColumnIndex("APPVersion")));
		tissueSampleBean.setManufacturer(cursor.getString(cursor.getColumnIndex("Manufacturer")));
		tissueSampleBean.setModel(cursor.getString(cursor.getColumnIndex("Model")));
		tissueSampleBean.setNetwork(cursor.getString(cursor.getColumnIndex("Network")));
		tissueSampleBean.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
		tissueSampleBean.setTimestamp(cursor.getString(cursor.getColumnIndex("Timestamp")));

		return tissueSampleBean;
	}

	public long create(TissueSampleBean report) {

		ContentValues values = new ContentValues();

		values.put("TaskId", report.getTaskId());
		values.put("ExceptionType", report.getExceptionType());
		values.put("SamplePath", report.getSamplePath());
		values.put("StackTrace", report.getStackTrace());
		values.put("IMSI", report.getImsiNo());
		values.put("OsVersion", report.getOsVersion());
		values.put("APPVersion", report.getAppVersion());
		values.put("Manufacturer", report.getManufacturer());
		values.put("Model", report.getModel());
		values.put("Network", report.getNetwork());

		if (!TextUtils.isEmpty(report.getRemark())) {
			values.put("Remark", report.getRemark());
		}
		values.put("Timestamp", report.getTimestamp());

		return getWritableDatabase().insert(Database.DBTABLE_TissueSampleTask, null, values);
	}

	public long delete(int id) {

		return getWritableDatabase().delete(Database.DBTABLE_TissueSampleTask,
				"ID=?",
				new String[] { String.valueOf(id) });
	}

	public TissueSampleBean getTissueSample(int offset) {

		String query = String.format(Locale.CHINA,
				"SELECT * FROM %s ORDER BY Timestamp ASC LIMIT 1 OFFSET %d;",
				Database.DBTABLE_TissueSampleTask,
				offset);

		Cursor cursor = null;
		try {
			cursor = getReadableDatabase().rawQuery(query, null);
			if (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
				return readTissueSample(cursor);
			}
		} catch (Exception except) {
			ExceptionsHandlingService.handleException(except);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
}
