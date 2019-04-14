package com.hannibal.scalpel.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.hannibal.scalpel.Hannibal;
import com.hannibal.scalpel.Util.CommonUtils;
import com.hannibal.scalpel.service.BiopsyService;

import java.util.concurrent.atomic.AtomicInteger;


public class DataAccessObject {

	protected static Database database;
	
	private static AtomicInteger usageCounter = new AtomicInteger(0);
	
	public static Database getDatabase() {
		return database;
	}

	public SQLiteDatabase getReadableDatabase() {
		return database.getReadableDatabase();
	}
	
	public SQLiteDatabase getWritableDatabase() {
		return database.getWritableDatabase();
	}
	 
	/**
	 * Clear all the data in database
	 */
	public void clearDatabase() {
		SQLiteDatabase sqliteDb = getWritableDatabase();
		sqliteDb.delete(Database.DBTABLE_TissueSampleTask, null, null);
		sqliteDb.delete(Database.DBTABLE_DiseasedTissueTask, null, null);
	}
	
	public void open(Context context) {
		int a = usageCounter.incrementAndGet();
		CommonUtils.printDevLog("open database: " + a);
		if (database == null) {
			database = new Database(context);
		}
	}  
	
	public void close() {
		int b = usageCounter.decrementAndGet();
		CommonUtils.printDevLog("close database: " + b);
		if (b <= 0) {
			if (database != null) {
				database.close();
				database = null;
			}
		}
	}
}
