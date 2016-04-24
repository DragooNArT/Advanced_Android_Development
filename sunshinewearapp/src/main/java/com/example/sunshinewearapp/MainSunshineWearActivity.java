package com.example.sunshinewearapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class MainSunshineWearActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sunshine_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey(SunshineDataReceiver.WEATHER_ID_KEYR)) {
            int weatherId = extras.getInt(SunshineDataReceiver.WEATHER_ID_KEY);
            double high = extras.getInt(SunshineDataReceiver.WEATHER_HIGH_KEY);
            double low = extras.getInt(SunshineDataReceiver.WEATHER_LOW_KEY);
        } else {
            //TODO ask mobile device for data
        }
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.currentDate);
            }
        });
    }
}
