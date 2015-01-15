package com.cisco.yambalabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ralph on 1/15/15.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final int YAMBA_SERVICE_ID = 900;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent yambaIntent = new Intent(context, YambaService.class);
        PendingIntent operation = PendingIntent.getService(
                context, YAMBA_SERVICE_ID, yambaIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        am.setInexactRepeating(AlarmManager.RTC, 0, 10000, operation);
    }
}
