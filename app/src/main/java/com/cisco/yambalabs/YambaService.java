package com.cisco.yambalabs;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

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

            for (YambaClient.Status status : statuses) {
                Log.d(TAG, status.getId() + " " + status.getUser() + " "
                        + status.getMessage() + " " + status.getCreatedAt());
            }
        } catch (YambaClientException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
