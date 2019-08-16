package com.hannibal.scalpel.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;


import com.hannibal.scalpel.bean.DiseasedTissueBean;
import com.hannibal.scalpel.task.ExceptionsHandlingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiseasedTissueDao extends DataAccessObject {

	private DiseasedTissueBean readDiseasedTissue(Cursor cursor) {

		DiseasedTissueBean crashReport = new DiseasedTissueBean();

		crashReport.setId(cursor.getInt(cursor.getColumnIndex("ID")));
		crashReport.setMessage(cursor.getString(cursor.getColumnIndex("Message")));
		crashReport.setStackTrace(cursor.getString(cursor.getColumnIndex("StackTrace")));
		crashReport.setImsiNo(cursor.getString(cursor.getColumnIndex("IMSI")));
		crashReport.setOsVersion(cursor.getString(cursor.getColumnIndex("OsVersion")));
		crashReport.setAppVersion(cursor.getString(cursor.getColumnIndex("APPVersion")));
		crashReport.setManufacturer(cursor.getString(cursor.getColumnIndex("Manufacturer")));
		crashReport.setModel(cursor.getString(cursor.getColumnIndex("Model")));
		crashReport.setNetwork(cursor.getString(cursor.getColumnIndex("Network")));
		crashReport.setRemark(cursor.getString(cursor.getColumnIndex("Remark")));
		crashReport.setTimestamp(cursor.getString(cursor.getColumnIndex("Timestamp")));

		return crashReport;
	}

	public long create(DiseasedTissueBean report) {

		ContentValues values = new ContentValues();

		values.put("Message", report.getMessage());
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

		return getWritableDatabase().insert(Database.DBTABLE_DiseasedTissueTask, null, values);
	}

	public long delete(int id) {

		return getWritableDatabase().delete(Database.DBTABLE_DiseasedTissueTask,
				"ID=?",
				new String[] { String.valueOf(id) });
	}

	public DiseasedTissueBean getDiseasedTissue(int offset) {

		String query = String.format(Locale.CHINA,
				"SELECT * FROM %s ORDER BY Timestamp ASC LIMIT 1 OFFSET %d;",
				Database.DBTABLE_DiseasedTissueTask,
				offset);

		Cursor cursor = null;
		try {
			cursor = getReadableDatabase().rawQuery(query, null);
			if (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
				return readDiseasedTissue(cursor);
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
