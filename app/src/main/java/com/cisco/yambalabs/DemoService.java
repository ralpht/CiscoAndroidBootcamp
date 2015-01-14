package com.cisco.yambalabs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by ralph on 1/13/15.
 */
public class DemoService extends Service {
    public static final String SPECIAL_DEMO_VALUE = "SPECIAL_DEMO_VALUE";

    private static final String TAG = "DemoService";

    public Intent createIntent(Context context, String specialValue) {
        Intent i = new Intent(context, DemoService.class);
        i.putExtra(SPECIAL_DEMO_VALUE, specialValue);
        return i;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent.hasExtra(SPECIAL_DEMO_VALUE)) {
            Log.d(TAG, "DemoService special value: " + intent.getStringExtra(SPECIAL_DEMO_VALUE));
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
