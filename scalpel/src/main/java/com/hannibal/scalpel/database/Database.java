package com.hannibal.scalpel.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public static final String DB_NAME = "Hannibal.db";
	
	public static final int DB_VERSION = 1;
	
	public Database(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	/** Table names */
	public static final String DBTABLE_DiseasedTissueTask = "DiseasedTissueInfo";
	
	public static final String DBTABLE_TissueSampleTask = "TissueSampleInfo";

	@Override public void onCreate(SQLiteDatabase db) {
		// Cursed shitty legacy table table defintion.
		/*
		 * add Address, DestAddress, CaseNumber, RepaireShop
		 */
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
					+ DBTABLE_DiseasedTissueTask
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT," 
					+ "ClaimID,"
					+ "ExceptionType TEXT,"
					+ "CaseNumber,"
					+ "JobNumber TEXT,"
					+ "StackTrace TEXT,"
					+ "Machine,"
					+ "SendDate," 
					+ "TransmitNum," 
					+ "ReceiveDate," 
					+ "OsVersion,"
					+ "IMSI,"
					+ "Network,"
					+ "Remark,"
					+ "DealerName,"
					+ "RepaireShop,"
					+ "DestProvince," 
					+ "DestCity," 
					+ "DestCounty," 
					+ "Status,"
					+ "ArriveSceneDate," 
					+ "LuxiuDate," 
					+ "Model,"
					+ "Address,"
					+ "DestAddress,"
					+ "Position,"
					+ "APPVersion,"
					+ "FaultCode," 
					+ "Manufacturer,"
					+ "Comment,"
					+ "PolicyHolderTel,"
					+ "Longitude FLOAT,"
					+ "Latitude FLOAT,"
					+ "Timestamp)");
		
		// Cursed shitty legacy table table defintion.
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
					+ DBTABLE_TissueSampleTask
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "RescueTaskId INTEGER, "
					+ "SamplePath TEXT,"
					+ "Timestamp,"
					+ "Network,"
					+ "IMEI,"
					+ "OsVersion,"
					+ "Manufacturer,"
					+ "APPVersion,"
					+ "Model,"
					+ "Status,"
					+ "Longitude,"
					+ "Latitude," 
					+ "TaskType)");
		
	}
	
	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// shit. 
		onCreate(db);
		addColumn(db, DBTABLE_DiseasedTissueTask, "BenefitId", "TEXT", true);
	}
	
	private void addColumn(SQLiteDatabase database,
							String tableName, 
							String columnName, 
							String dataType, 
							boolean nullable) {
		
		String query = String.format("SELECT * FROM %s WHERE 1=0;", tableName);
		Cursor cursor = null;
		String[] columns = null;

		try {
			cursor = database.rawQuery(query, null);
			if (cursor != null) {
				columns = cursor.getColumnNames();
			}
			
		} finally {
			if (cursor != null)
				cursor.close();
		}
		
		if (columns == null || columns.length <= 0) {
			return;
		}
		
		boolean isColumnExists = false;
		for (String colName: columns) {
			if (columnName.equalsIgnoreCase(colName)) {
				isColumnExists = true;
				break;
			}
		}
		
		if (!isColumnExists) {
			query = String.format("ALTER TABLE %s ADD COLUMN %s %s %s;", 
									tableName, 
									columnName, 
									dataType, 
									nullable ? "NULL" : "NOT NULL");
			database.execSQL(query);
		}
	}
}
