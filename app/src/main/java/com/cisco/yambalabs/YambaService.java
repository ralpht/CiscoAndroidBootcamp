package com.cisco.yambalabs;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralph on 1/13/15.
 */
public class YambaService extends IntentService {
    private static final String TAG = "YambaService";

    public YambaService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        YambaClient client = new YambaClient("student", "password");

        try {
            List<YambaClient.Status> statuses = client.getTimeline(25);
            List<ContentValues> valueList = new ArrayList<ContentValues>();
            long max = getMax();

            for (YambaClient.Status status : statuses) {
                if (status.getId() <= max) {
                    continue;
                }

                ContentValues cv = new ContentValues();

                cv.put(YambaContract.Status.Column.MESSAGE, status.getMessage());
                cv.put(YambaContract.Status.Column.SERVER_ID, status.getId());
                cv.put(YambaContract.Status.Column.TIMESTAMP, status.getCreatedAt().getTime());
                cv.put(YambaContract.Status.Column.USER, status.getUser());

                valueList.add(cv);
            }

            int count = getContentResolver().bulkInsert(
                    YambaContract.Status.URI,
                    valueList.toArray(new ContentValues[valueList.size()]));

            Log.d(TAG, "Inserted " + count + " item(s)");
        } catch (YambaClientException e) {
            e.printStackTrace();
        }
    }

    private long getMax() {
        long max = 0;

        Cursor c = getContentResolver().query(
                YambaContract.Status.URI,
                new String[] { YambaContract.Status.Column.MAX_ID },
                null, null, null);

        try {
            if (c != null && c.moveToFirst()) {
                max = c.getLong(0);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return max;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
