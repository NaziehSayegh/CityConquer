package com.mohammad_nazieh_amro.cityconquer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mohammad_nazieh_amro.cityconquer.service.LocationTrackingService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, LocationTrackingService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}