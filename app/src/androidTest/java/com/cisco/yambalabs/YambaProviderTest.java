package com.cisco.yambalabs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import java.util.Date;

/**
 * Created by ralph on 1/16/15.
 */
public class YambaProviderTest extends ProviderTestCase2<YambaProvider> {
    private ContentResolver mResolver;

    public YambaProviderTest() {
        super(YambaProvider.class, "com.cisco.yambalabs");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mResolver = getMockContentResolver();
    }

    public void testBulkInsert() {
        // clear the db
        mResolver.delete(YambaContract.Status.URI, null, null);

        Cursor c = mResolver.query(
                YambaContract.Status.URI,
                new String[]{YambaContract.Status.Column.ID},
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have an empty db", 0, c.getCount());

        // Insert test data
        long date1 = new Date().getTime();
        long date2 = new Date().getTime();

        ContentValues[] cvArray = new ContentValues[2];
        ContentValues cv = new ContentValues();

        cv.put(YambaContract.Status.Column.USER, "Robotium User");
        cv.put(YambaContract.Status.Column.MESSAGE, "Robotium test message 1");
        cv.put(YambaContract.Status.Column.TIMESTAMP, date1);
        cv.put(YambaContract.Status.Column.SERVER_ID, 1);
        cvArray[0] = cv;

        cv = new ContentValues();
        cv.put(YambaContract.Status.Column.USER, "Robotium User");
        cv.put(YambaContract.Status.Column.MESSAGE, "Robotium test message 2");
        cv.put(YambaContract.Status.Column.TIMESTAMP, date2);
        cv.put(YambaContract.Status.Column.SERVER_ID, 2);
        cvArray[1] = cv;

        mResolver.bulkInsert(YambaContract.Status.URI, cvArray);

        c = mResolver.query(
                YambaContract.Status.URI,
                new String[] {
                        YambaContract.Status.Column.USER,
                        YambaContract.Status.Column.MESSAGE,
                        YambaContract.Status.Column.TIMESTAMP,
                        YambaContract.Status.Column.SERVER_ID
                },
                null, null,
                YambaContract.Status.Column.SERVER_ID);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have 2 rows", 2, c.getCount());

        if (c.moveToFirst()) {
            assertEquals("Row 1 user mismatch", "Robotium User", c.getString(0));
            assertEquals("Row 1 message mismatch", "Robotium test message 1", c.getString(1));
            assertEquals("Row 1 date mismatch", date1, c.getLong(2));
            assertEquals("Row 1 server id mismatch", 1, c.getLong(3));
        }

        if (c.moveToNext()) {
            assertEquals("Row 2 user mismatch", "Robotium User", c.getString(0));
            assertEquals("Row 2 message mismatch", "Robotium test message 2", c.getString(1));
            assertEquals("Row 2 date mismatch", date2, c.getLong(2));
            assertEquals("Row 2 server id mismatch", 2, c.getLong(3));
        }
    }

    public void testDelete() {
        // add one row
        ContentValues cv = new ContentValues();

        cv.put(YambaContract.Status.Column.USER, "Robotium User");
        cv.put(YambaContract.Status.Column.MESSAGE, "Robotium test message 1");
        cv.put(YambaContract.Status.Column.TIMESTAMP, new Date().getTime());
        cv.put(YambaContract.Status.Column.SERVER_ID, Long.MAX_VALUE - 1);

        Uri uri = mResolver.insert(YambaContract.Status.URI, cv);

        assertNotNull("We should have a uri", uri);

        Cursor c = mResolver.query(
                YambaContract.Status.URI,
                new String[] {
                        YambaContract.Status.Column.USER,
                        YambaContract.Status.Column.MESSAGE,
                        YambaContract.Status.Column.TIMESTAMP,
                        YambaContract.Status.Column.SERVER_ID
                },
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertTrue("We should have rows", c.getCount() > 0);

        // clear db
        mResolver.delete(YambaContract.Status.URI, null, null);

        c = mResolver.query(
                YambaContract.Status.URI,
                new String[]{YambaContract.Status.Column.ID},
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have an empty db", 0, c.getCount());
    }

    public void testInsert() {
        // clear the db
        mResolver.delete(YambaContract.Status.URI, null, null);

        Cursor c = mResolver.query(
                YambaContract.Status.URI,
                new String[]{YambaContract.Status.Column.ID},
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have an empty db", 0, c.getCount());

        // add one row
        long date = new Date().getTime();
        ContentValues cv = new ContentValues();

        cv.put(YambaContract.Status.Column.USER, "Robotium User");
        cv.put(YambaContract.Status.Column.MESSAGE, "Robotium test message 1");
        cv.put(YambaContract.Status.Column.TIMESTAMP, date);
        cv.put(YambaContract.Status.Column.SERVER_ID, Long.MAX_VALUE - 1);

        Uri uri = mResolver.insert(YambaContract.Status.URI, cv);

        assertNotNull("We should have a uri", uri);

        c = mResolver.query(
                uri,
                new String[] {
                        YambaContract.Status.Column.USER,
                        YambaContract.Status.Column.MESSAGE,
                        YambaContract.Status.Column.TIMESTAMP,
                        YambaContract.Status.Column.SERVER_ID
                },
                null, null, null);

        assertNotNull("We should have a cursor", c);
        assertEquals("We should have 1 row", 1, c.getCount());

        if (c.moveToFirst()) {
            assertEquals("Row 1 user mismatch", "Robotium User", c.getString(0));
            assertEquals("Row 1 message mismatch", "Robotium test message 1", c.getString(1));
            assertEquals("Row 1 date mismatch", date, c.getLong(2));
            assertEquals("Row 1 server id mismatch", Long.MAX_VALUE - 1, c.getLong(3));
        }
    }
}
