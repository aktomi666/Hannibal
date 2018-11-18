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
	
	public static final String DBTABLE_Settings = "AR_Settings"; 
	
	public static final String DBTABLE_TempTakenPhoto = "AR_TempTakenPhoto";
	
	public static final String DBTABLE_UploadFailedTasks = "UploadFailedFailedTasks";       
	
	public static final String DBTABLE_UploadingTask = "UploadingTasks"; 
	
	public static final String DBTABLE_TaskLocationHistory = "AR_TaskLocationHistory"; 
	
	public static final String DBTABLE_FaultInfo = "FaultInfo";
	
	public static final String DBTABLE_ExceptionLog = "ExceptionLog";
	
	@Override public void onCreate(SQLiteDatabase db) {
		// Cursed shitty legacy table table defintion.
		/*
		 * add Address, DestAddress, CaseNumber, RepaireShop
		 */
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
					+ DBTABLE_DiseasedTissueTask
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT," 
					+ "ClaimID,"
					+ "CaseNumber,"
					+ "JobNumber TEXT,"
					+ "UserPhoneNumber TEXT,"
					+ "BenefitId TEXT,"
					+ "EventReason," 
					+ "EventAddress," 
					+ "Machine,"
					+ "SendDate," 
					+ "TransmitNum," 
					+ "ReceiveDate," 
					+ "CaseType,"
					+ "IsRevocation," 
					+ "CaseKey," 
					+ "RegistrationNumber,"
					+ "RescueType," 
					+ "Province," 
					+ "City," 
					+ "County,"
					+ "CustomerName," 
					+ "CustomerPhoneNumber," 
					+ "DealerName,"
					+ "RepaireShop,"
					+ "DestProvince," 
					+ "DestCity," 
					+ "DestCounty," 
					+ "Status,"
					+ "ArriveSceneDate," 
					+ "LuxiuDate," 
					+ "LoadOnTrailerTime,"
					+ "Arrive4SDate,"
					+ "ConnectDate," 
					+ "ArriveSceneDis," 
					+ "Arrive4SDis,"
					+ "Address,"             
					+ "DestAddress,"
					+ "Position,"
					+ "ComponentCode," 
					+ "FaultCode," 
					+ "CompletionsCode,"
					+ "Comment,"
					+ "PolicyHolderTel,"
					+ "Longitude FLOAT,"
					+ "Latitude FLOAT,"
					+ "TaskSource INTEGER CHECK(TaskSource>0) NOT NULL)");
		
		// Cursed shitty legacy table table defintion.
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
					+ DBTABLE_TissueSampleTask
					+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "RescueTaskId INTEGER, "
					+ "SamplePath TEXT,"
					+ "ClaimID TEXT," 
					+ "CaseKey TEXT," 
					+ "PhotoType," 
					+ "PhotoPath TEXT,"
					+ "RecordingTime,"
					+ "RecordingAdress," 
					+ "UploadTime,"
					+ "Status,"
					+ "IsUploadFailed INTEGER  DEFAULT(0),"
					+ "Longitude," 
					+ "Latitude," 
					+ "TaskType)");
	
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (NAME TEXT UNIQUE, VALUE TEXT)", DBTABLE_Settings));

		
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
				+ DBTABLE_TaskLocationHistory
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " RescueTaskId INTEGER, "
				+ " RescueTaskStepId INTEGER, "
				+ " Latitude NUMERIC, "
				+ " Longitude NUMERIC, "
				+ " ReportTime NUMERIC)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " 
				+ DBTABLE_ExceptionLog
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " TaskId TEXT, "
				+ " ExceptionType TEXT, "
				+ " Message TEXT, "
				+ " StackTrace TEXT, "
				+ " IMSI TEXT, "
				+ " APPVersion TEXT, "
				+ " OsVersion TEXT, "
				+ " Manufacturer TEXT, "
				+ " Model TEXT, "
				+ " Network TEXT, "
				+ " Remark TEXT, " 
				+ " Timestamp TEXT)");
		
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
