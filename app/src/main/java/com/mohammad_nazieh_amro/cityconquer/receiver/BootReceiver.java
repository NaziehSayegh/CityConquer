package com.androidkings.cityconquer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.androidkings.cityconquer.service.LocationTrackingService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, LocationTrackingService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}