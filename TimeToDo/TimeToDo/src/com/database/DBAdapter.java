package com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String DATABASE_NAME = "timetodo.sql";

	private static final int DATABASE_VERSION = 23;

	private final Context context;

	private static DatabaseHelper DBHelper;
	private static SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL(DATABASE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// public static boolean insert_account(String tableName, String name,
	// String weekly_allowance, String pay_day, String amount,
	// String email_address, String currentDateTime) {
	//
	// db = DBHelper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put("name", name);
	// values.put("weekly_allowance", weekly_allowance);
	// values.put("pay_day", pay_day);
	// values.put("amount", amount);
	// values.put("email_address", email_address);
	// values.put("date", currentDateTime);
	//
	// db.insert(tableName, null, values);
	// return true;
	// }
	//
	// public Cursor update_account(String id, String name,
	// String weekly_allowance, String pay_day, String amount,
	// String email_address) {
	//
	// Cursor mCursor = db.rawQuery("update account SET name='" + name
	// + "',weekly_allowance='" + weekly_allowance + "',pay_day='"
	// + pay_day + "',amount='" + amount + "',email_address='"
	// + email_address + "' where id='" + id + "'", null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	//
	// mCursor.close();
	// return mCursor;
	//
	// }
	//
	// public Cursor get_name_count(String name) {
	//
	// Cursor mCursor = db.rawQuery("select * FROM account where name='"
	// + name + "'", null);
	//
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor getaccount_detail(String id) {
	//
	// Cursor mCursor = db.rawQuery("select * FROM account where id='" + id
	// + "'", null);
	//
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//

	public Cursor gettask_list() throws SQLException {

		open();

		Cursor mCursor = db.rawQuery(
				"select * from timetodo ORDER BY task_date ASC", null);

		if (mCursor != null) {
			if (mCursor.getCount() > 0) {

				mCursor.moveToFirst();

				// Log.e(" SUB COUNTSS", Integer.toString(mCursor.getCount()));
			}
		}

		close();

		return mCursor;
	}

	public static boolean insert_account_enrty(String tableName,
			String task_name, String task_des, String task_date,
			String task_time, String task_type) {

		Log.e("", "msg open insert data");
		db = DBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("task_name", task_name);
		values.put("task_des", task_des);
		values.put("task_date", task_date);
		values.put("task_time", task_time);
		values.put("task_type", task_type);

		Log.e("", "msg time" + task_time);

		db.insert(tableName, null, values);
		return true;
	}

	public Cursor select_id(String task_name) {

		// Log.e("", "id====" + title);
		Cursor mCursor = db.rawQuery(
				"select task_id FROM timetodo where task_name='" + task_name
						+ "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor update_task(String task_id, String task_name,
			String task_des, String task_date, String task_time,
			String task_type) {

		Cursor mCursor = db.rawQuery("update timetodo SET task_name='"
				+ task_name + "',task_des='" + task_des + "',task_date='"
				+ task_date + "',task_time='" + task_time + "',task_type='"
				+ task_type + "' where task_id='" + task_id + "'", null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		mCursor.close();
		return mCursor;

	}

	public Cursor delete_task(String task_id) {

		Cursor mCursor = db.rawQuery("delete FROM timetodo where task_id='"
				+ task_id + "'", null);
		if (mCursor != null) {
			if (mCursor.getCount() > 0) {

			}
		}
		mCursor.close();
		return mCursor;
	}
	// public static int sum_amount() throws SQLException {
	// db = DBHelper.getReadableDatabase();
	//
	// Cursor mCursor = db.rawQuery(
	// "select sum amount from transection ORDER BY date DESC", null);
	//
	// ContentValues values = new ContentValues();
	// values.get("amount");
	//
	// int Sum = 0,i=0;
	// int amount=(Integer) values.get("amount");
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// Sum = amount+Sum;
	// i =Sum;
	// mCursor.moveToFirst();
	//
	// // Log.e(" SUB COUNTSS", Integer.toString(mCursor.getCount()));
	// }
	// }
	//
	//
	//
	// return i;
	// }

	// public Cursor getaccount_detail(String fr_date, String ls_date,
	// String account_id) {
	//
	// Cursor mCursor = db.rawQuery(
	// "select * FROM account_entry where date >='" + fr_date
	// + "' and date <='" + ls_date + "' and account_id='"
	// + account_id + "' ORDER BY id DESC", null);
	//
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public static boolean insert_recurring(String tableName, String title,
	// String amount, String type, String when1, String when2,
	// String account_id) {
	//
	// db = DBHelper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	//
	// values.put("title", title);
	// values.put("amount", amount);
	// values.put("type", type);
	// values.put("when1", when1);
	// values.put("when2", when2);
	// values.put("account_id", account_id);
	//
	// db.insert(tableName, null, values);
	// return true;
	// }
	//
	// public Cursor update_recurring(String id, String amount, String when1,
	// String when2) {
	//
	// Cursor mCursor = db.rawQuery("update recurring SET amount='" + amount
	// + "',when1='" + when1 + "',when2='" + when2 + "' where id='"
	// + id + "'", null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	//
	// mCursor.close();
	// return mCursor;
	//
	// }
	//
	// public Cursor get_recu_name(String title, String ac_id) {
	//
	// Cursor mCursor = db.rawQuery("select * FROM recurring where title='"
	// + title + "' and account_id='" + ac_id + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor get_reg_name(String title, String ac_id) {
	//
	// Cursor mCursor = db.rawQuery("select * FROM regular where title='"
	// + title + "' and account_id='" + ac_id + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor delete_recu_entry(String title, String rc_id, String date)
	// {
	//
	// Cursor mCursor = db.rawQuery("delete FROM account_entry where title='"
	// + title + "' and for='" + rc_id + "' and date>='" + date + "'",
	// null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor delete_recuuring(String rc_id) {
	//
	// Cursor mCursor = db.rawQuery("delete FROM recurring where id='" + rc_id
	// + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor get_recuring_detail() {
	//
	// Cursor mCursor = db.rawQuery("select * FROM recurring", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public static boolean insert_regular(String tableName, String title,
	// String amount, String type, String account_id) {
	//
	// db = DBHelper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	//
	// values.put("title", title);
	// values.put("amount", amount);
	// values.put("type", type);
	// values.put("account_id", account_id);
	//
	// db.insert(tableName, null, values);
	// return true;
	// }
	//
	// public Cursor update_regular(String id, String title, String amount) {
	//
	// Cursor mCursor = db.rawQuery("update regular SET title='" + title
	// + "',amount='" + amount + "' where id='" + id + "'", null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	//
	// mCursor.close();
	// return mCursor;
	//
	// }
	//
	// public Cursor update_account_entry(String id, String title, String
	// amount) {
	//
	// Cursor mCursor = db.rawQuery("update account_entry SET title='" + title
	// + "',amount='" + amount + "' where id='" + id + "'", null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	//
	// mCursor.close();
	// return mCursor;
	//
	// }
	//
	// public Cursor get_regular_name(String title) {
	//
	// Cursor mCursor = db.rawQuery("select * FROM regular where title='"
	// + title + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor get_regular_detail() {
	//
	// Cursor mCursor = db.rawQuery("select * FROM regular", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	//
	// return mCursor;
	// }
	//
	// public Cursor delete_regular_edit(String id) {
	//
	// Cursor mCursor = db.rawQuery("delete FROM regular where id='" + id
	// + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	// mCursor.close();
	// return mCursor;
	// }
	//
	// public Cursor delete_account_entry(String id) {
	//
	// Cursor mCursor = db.rawQuery("delete FROM account_entry where id='"
	// + id + "'", null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	// mCursor.close();
	// return mCursor;
	// }
	//
	// public Cursor delete_account(String ac_id) {
	//
	// Cursor mCursor = db.rawQuery(
	// "delete FROM account_entry where account_id='" + ac_id + "'",
	// null);
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// }
	// }
	// Cursor mCursor1 = db.rawQuery("delete FROM account where id='" + ac_id
	// + "'", null);
	// if (mCursor1 != null) {
	// if (mCursor1.getCount() > 0) {
	//
	// }
	// }
	// Cursor mCursor2 = db.rawQuery(
	// "delete FROM recurring where account_id='" + ac_id + "'", null);
	// if (mCursor2 != null) {
	// if (mCursor2.getCount() > 0) {
	//
	// }
	// }
	// Cursor mCursor3 = db.rawQuery("delete FROM regular where account_id='"
	// + ac_id + "'", null);
	// if (mCursor3 != null) {
	// if (mCursor3.getCount() > 0) {
	//
	// }
	// }
	// mCursor.close();
	// mCursor1.close();
	// mCursor2.close();
	// mCursor3.close();
	// return mCursor;
	// }
	//
	// public Cursor delete_week_allow(String ac_id) {
	//
	// Cursor mCursor1 = db.rawQuery(
	// "delete FROM account_entry where title='"
	// + "Weekly Allowance Addes" + "' and account_id='"
	// + ac_id + "'", null);
	// if (mCursor1 != null) {
	// if (mCursor1.getCount() > 0) {
	//
	// }
	// }
	//
	// mCursor1.close();
	// return mCursor1;
	// }
	//
	// public static boolean insert_email(String tableName, String email) {
	//
	// db = DBHelper.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put("email", email);
	//
	// db.insert(tableName, null, values);
	// return true;
	// }
	//
	// public Cursor getemail_list() throws SQLException {
	//
	// open();
	//
	// Cursor mCursor = db.rawQuery("select * from Email", null);
	//
	// if (mCursor != null) {
	// if (mCursor.getCount() > 0) {
	//
	// mCursor.moveToFirst();
	//
	// // Log.e(" SUB COUNTSS", Integer.toString(mCursor.getCount()));
	// }
	// }
	//
	// close();
	//
	// return mCursor;
	// }
	//
	// public Cursor delete_email(String email) {
	// Cursor mCursor = db.rawQuery("delete FROM Email where email='" + email
	// + "'", null);
	//
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	//
	// return mCursor;
	// }

}
