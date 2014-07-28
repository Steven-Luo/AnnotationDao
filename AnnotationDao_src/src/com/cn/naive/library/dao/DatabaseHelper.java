package com.cn.naive.library.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The subclass of SQLiteOpenHelper encapsuled some convenient method.
 * 
 * @see android.database.sqlite.SQLiteOpenHelper
 * @author Steven.Luo
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "record.db";

	private static DatabaseHelper databaseHelper;

	public static DatabaseHelper getInstance(Context context) {
		if (databaseHelper == null)
			databaseHelper = new DatabaseHelper(context);
		return databaseHelper;
	}

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	/**
	 * Check if the table has exists.
	 * 
	 * @param db
	 *            Instance of SQLiteDatabase
	 * @param table
	 *            Table Name
	 * @see android.database.sqlite.SQLiteDatabase
	 * @return
	 */
	public boolean tableExists(SQLiteDatabase db, String table) {
		boolean result = false;
		String sql = "select count(*) xcount from sqlite_master where type='table' and name='" + table + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		if (cursor.getInt(0) > 0)
			result = true;
		cursor.close();
		return result;
	}
}
