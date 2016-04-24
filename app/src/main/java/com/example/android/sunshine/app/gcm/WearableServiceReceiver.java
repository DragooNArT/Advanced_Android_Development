package com.example.android.sunshine.app.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearableServiceReceiver extends WearableListenerService {
    public WearableServiceReceiver() {
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals("/request-weather")) {
            //TODO send data
        }
    }
}
