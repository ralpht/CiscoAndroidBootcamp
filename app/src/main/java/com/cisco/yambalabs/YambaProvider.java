package com.cisco.yambalabs;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class YambaProvider extends ContentProvider {
	private static final String TAG = "YambaProvider";
	
	private static final int STATUS_ITEM_TYPE = 1;
	private static final int STATUS_DIR_TYPE = 2;

	private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Map<String, String> PROJECTION_MAP = new HashMap<String, String>();

	static {
		/* Match content://com.cisco.yambalabs/status --> Return int STATUS_DIR_TYPE */
		MATCHER.addURI(YambaContract.AUTHORITY, YambaContract.Status.TABLE,
                STATUS_DIR_TYPE);
		/* Match content://com.cisco.yambalabs/status/# --> Return int STATUS_ITEM_TYPE */
		MATCHER.addURI(YambaContract.AUTHORITY, YambaContract.Status.TABLE
				+ "/#", STATUS_ITEM_TYPE);
		/* We can later use these its in if or switch-statements to handle requests. */

		/* Maps content labels to database columns. */
		PROJECTION_MAP.put(YambaContract.Status.Column.ID,
				YambaContract.Status.Column.ID);
        PROJECTION_MAP.put(YambaContract.Status.Column.SERVER_ID,
                YambaContract.Status.Column.SERVER_ID);
		PROJECTION_MAP.put(YambaContract.Status.Column.USER,
				YambaContract.Status.Column.USER);
		PROJECTION_MAP.put(YambaContract.Status.Column.TIMESTAMP,
				YambaContract.Status.Column.TIMESTAMP);
		PROJECTION_MAP.put(YambaContract.Status.Column.MESSAGE,
				YambaContract.Status.Column.MESSAGE);
		PROJECTION_MAP.put(YambaContract.Status.Column.MAX_ID, "max ("
				+ YambaContract.Status.Column.SERVER_ID + ")");
	}

	private YambaDBHelper mDbHelper;

	@Override
	public boolean onCreate() {
		mDbHelper = new YambaDBHelper(getContext());

		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (MATCHER.match(uri)) {
		case STATUS_ITEM_TYPE:
			return YambaContract.Status.ITEM_TYPE;

		case STATUS_DIR_TYPE:
			return YambaContract.Status.DIR_TYPE;

		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (MATCHER.match(uri)) {
		case STATUS_ITEM_TYPE:
		case STATUS_DIR_TYPE:
			break;
        default:
            throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = 0;
		
		try {
			count = db.delete(YambaContract.Status.TABLE, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
		} finally {
			db.close();
		}
		
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (MATCHER.match(uri)) {
		case STATUS_ITEM_TYPE:
		case STATUS_DIR_TYPE:
			return null;
		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		switch (MATCHER.match(uri)) {
		case STATUS_DIR_TYPE:
			break;

		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}
		
		int count = 0;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		try {
			db.beginTransaction();
		
			for (ContentValues cv : values) {
                db.insert(YambaContract.Status.TABLE, null, cv);
				count++;
			}
			
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}

        getContext().getContentResolver().notifyChange(uri, null);
		
		Log.d(TAG, "Bulk insert of " + count + " posts");
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		long id = -1;

		switch (MATCHER.match(uri)) {
		case STATUS_ITEM_TYPE:
			id = ContentUris.parseId(uri);

		case STATUS_DIR_TYPE:
			break;

		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		qb.setTables(YambaContract.Status.TABLE);
		qb.setProjectionMap(PROJECTION_MAP);

		if (id != -1) {
			qb.appendWhere(YambaContract.Status.Column.ID + " = " + id);
		}

        qb.setStrict(true);

		Cursor c = qb.query(mDbHelper.getReadableDatabase(), projection,
				selection, selectionArgs, null, null, sortOrder);
		
		c.setNotificationUri(getContext().getContentResolver(), uri);

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (MATCHER.match(uri)) {
		case STATUS_ITEM_TYPE:
		case STATUS_DIR_TYPE:
			return 0;
		default:
			throw new IllegalArgumentException("Unrecognized uri: " + uri);
		}
	}

}
