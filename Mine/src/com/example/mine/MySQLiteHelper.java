package com.example.mine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MySQLiteHelper extends SQLiteOpenHelper {
	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE MEMO_TABLE(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "MEMO TEXT," + "CHECKED INTEGER DEFAULT 0," + "DETAIL TEXT," + "DEL INTEGER DEFAULT 0," + "LISTID INTEGER DEFAULT 0)";
		db.execSQL(sql);

		String sql2 = "CREATE TABLE MEMO_LIST_TABLE(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "LISTNAME TEXT," + "DEL INTEGER DEFAULT 0)";
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
