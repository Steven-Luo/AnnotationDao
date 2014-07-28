package com.cn.naive.library.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public abstract class AbstractDao<T> {

	public static final boolean DEBUG = true;
	private static final String TAG = AbstractDao.class.getSimpleName();
	private DatabaseHelper dbHelper;
	private Class<T> entityClass;
	private String table;

	public AbstractDao(Context context, Class<T> entityClass) {
		dbHelper = DatabaseHelper.getInstance(context);
		this.entityClass = entityClass;
	}

	/**
	 * Delete one record in database. Attentation please, if and only if there
	 * is a column named id typed integer in the table, this method will work.
	 * If there exists more than one record matched with the id value, all of
	 * these records would be removed.
	 * 
	 * @param id
	 * @return the number of rows affected.
	 */
	public int delete(int id) {
		int result = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			result = db.delete(table, "id=?", new String[] { String.valueOf(id) });
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}

	/**
	 * Delete objects according to SQL statement.
	 * 
	 * @param sql
	 *            The statement to delete the object.
	 */
	public void delete(String sql) {
		if (DEBUG) {
			Log.d(TAG, sql);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}

	/**
	 * Delete record. This method is quite like
	 * {@link android.database.sqlite.SQLiteDatabase#delete(String, String, String[])}
	 * 
	 * @param table
	 *            the table to delete from
	 * @param whereClause
	 *            the optional WHERE clause to apply when deleting. Passing null
	 *            will delete all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		int result = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			result = db.delete(table, whereClause, whereArgs);
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}

	public abstract int delete(T t);

	/**
	 * Drop the table. Attention please, if this method is invoked, all the
	 * objects saved in database will be deleted, either the table.
	 */
	public void drop() {
		StringBuilder sqlBuilder = new StringBuilder("DROP TABLE ");
		sqlBuilder.append(getTableName());
		String sql = sqlBuilder.toString();
		// DROP TABLE SQL statement
		if (DEBUG) {
			Log.d(TAG, sql);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql);
		db.close();
	}

	protected abstract String getCreateSql();

	/**
	 * Get the DatabaseHelper instance. Using this instance, you can get and
	 * instance typed SQLiteDatabase.
	 * 
	 * @see DatabaseHelper
	 * 
	 * @return
	 */
	protected DatabaseHelper getDatabaseHelper() {
		return dbHelper;
	}

	protected Class<T> getEntityClass() {
		return entityClass;
	}

	public String getTableName() {
		return table;
	}

	/**
	 * Convenience method for inserting an object
	 * 
	 * @param t
	 *            the instance of T
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insert(T t) {
		long result = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			result = db.insert(table, null, newContentValues(t));
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return result;
	}

	/**
	 * List all of the objects in the table.
	 * 
	 * @return A {@link java.util.List} of objects typed T. If there is no
	 *         records in the table, a List of size 0 will be returned.
	 */
	public List<T> list() {
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(table, null, null, null, null, null, null);

			while (cursor.moveToNext()) {
				list.add(newInstance(cursor));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}

		return list;
	}

	/**
	 * Query the given table, returning a {@link java.util.List} over the result
	 * set. This method is quite like
	 * {@link android.database.sqlite.SQLiteDatabase#query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) }
	 * 
	 * @param columns
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param groupBy
	 *            A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having
	 *            A filter declare which row groups to include in the cursor, if
	 *            row grouping is being used, formatted as an SQL HAVING clause
	 *            (excluding the HAVING itself). Passing null will cause all row
	 *            groups to be included, and is required when row grouping is
	 *            not being used.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @param limit
	 *            Limits the number of rows returned by the query, formatted as
	 *            LIMIT clause. Passing null denotes no LIMIT clause.
	 * @return A Cursor object, which is positioned before the first entry. Note
	 *         that Cursors are not synchronized, see the documentation for more
	 *         details.
	 */
	public List<T> list(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<T> list = new ArrayList<T>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

			while (cursor.moveToNext()) {
				list.add(newInstance(cursor));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}

		return list;
	}

	/**
	 * Subclass should implement this method, mapping object typed T to
	 * key/value pairs and putting in {@link android.content.ContentValues}
	 * 
	 * @param t
	 *            The instance of object typed T
	 * @return
	 * @see android.content.ContentValues
	 */
	protected abstract ContentValues newContentValues(T t);

	/**
	 * Subclass should implement this method, mapping cursor to object.
	 * 
	 * @param cursor
	 * @return instance of type T
	 * @see android.database.Cursor
	 */
	protected abstract T newInstance(Cursor cursor);

	/**
	 * Query one record in database. Attentation please, if and only if there is
	 * an unique column named id typed integer in the table, this method will
	 * work.
	 * 
	 * @param id
	 * @return The instance of type T matched with id
	 */
	public T query(int id) {
		T t = null;

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(table, null, "id=?", new String[] { String.valueOf(id) }, null, null, null);

			if (cursor.moveToNext()) {
				t = newInstance(cursor);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}

		return t;
	}

	/**
	 * Query one record. Attention please, only the first matched record will be
	 * returned.
	 * 
	 * @param sql
	 *            The complete SQL statement, you can get the table name using
	 *            {@link #getTableName()}
	 * @return The first matched object mapped from database.
	 */
	public T query(String sql) {
		T t = null;

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);

			if (cursor.moveToNext()) {
				t = newInstance(cursor);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}

		return t;
	}

	/**
	 * Query the given table, returning an instance of type T over the result
	 * set. This method is quite like this method
	 * {@link android.database.sqlite.SQLiteDatabase#query(boolean, String, String[], String, String[], String, String, String, String)}
	 * 
	 * @param columns
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param groupBy
	 *            A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having
	 *            A filter declare which row groups to include in the cursor, if
	 *            row grouping is being used, formatted as an SQL HAVING clause
	 *            (excluding the HAVING itself). Passing null will cause all row
	 *            groups to be included, and is required when row grouping is
	 *            not being used.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @param limit
	 *            Limits the number of rows returned by the query, formatted as
	 *            LIMIT clause. Passing null denotes no LIMIT clause.
	 * @see android.database.sqlite.SQLiteDatabase#query(boolean, String,
	 *      String[], String, String[], String, String, String, String)
	 * @return an instance of type T over the result set.
	 */
	public T query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		T t = null;

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

			if (cursor.moveToNext()) {
				t = newInstance(cursor);
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			db.close();
		}

		return t;
	}

	/**
	 * Set name of the table in SQLite.
	 * 
	 * @param table
	 */
	public void setTableName(String table) {
		this.table = table;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (!dbHelper.tableExists(db, getTableName())) {
			String sql = getCreateSql();
			Log.d(TAG, sql);
			db.execSQL(sql);
		}
		db.close();
	}

	/**
	 * Subclass should implement this method and define how to update a record.
	 * 
	 * @param t
	 *            The instance of object typed T
	 * @return the number of rows affected.
	 */
	public abstract int update(T t);
}
