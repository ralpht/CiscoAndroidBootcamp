package com.cisco.yambalabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class YambaDBHelper extends SQLiteOpenHelper {
	private static final String DB_FILE = "yamba.db";
	// version 1 was the base
	private static final int VERSION = 1;

	public YambaDBHelper(Context context) {
		super(context, DB_FILE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + YambaContract.Status.TABLE + " (" +
				YambaContract.Status.Column.ID + " INTEGER PRIMARY KEY," +
                YambaContract.Status.Column.SERVER_ID + " INTEGER NOT NULL," +
				YambaContract.Status.Column.TIMESTAMP + " INTEGER NOT NULL," +
				YambaContract.Status.Column.USER + " TEXT NOT NULL," +
				YambaContract.Status.Column.MESSAGE + " TEXT NOT NULL)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
