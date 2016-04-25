package com.example.andorid.sunshine.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.example.andorid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainSunshineWearActivity extends Activity implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextView;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sunshine_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        Bundle extras = getIntent().getExtras();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
        if(extras!=null && extras.containsKey(SunshineDataReceiver.WEATHER_ID_KEY)) {
            int weatherId = extras.getInt(SunshineDataReceiver.WEATHER_ID_KEY);
            double high = extras.getInt(SunshineDataReceiver.WEATHER_HIGH_KEY);
            double low = extras.getInt(SunshineDataReceiver.WEATHER_LOW_KEY);
        } else {
            //TODO ask mobile device for data
            Wearable.MessageApi.sendMessage(mGoogleApiClient, "nekvoId",
                    "/request-weather-data", null).setResultCallback(
                    new ResultCallback() {
                        @Override
                        public void onResult(Result result) {
                            if (!result.getStatus().isSuccess()) {
                                // Failed to send message
                            }
                        }


                    });
        }
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.currentDate);
            }
        });
    }

    private void updateUI(DataMap map) {
        int weatherDrawableId = SunshineDataReceiver.getArtResourceForWeatherCondition(map.getInt("WEATHER_ID_KEY"));
        double low_temp = map.getDouble("WEATHER_HIGH_KEY");
        double high_temp = map.getDouble("WEATHER_LOW_KEY");

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        //TODO do something with the data
        for(DataEvent event : dataEventBuffer) {
            if(event.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                String path = event.getDataItem().getUri().getPath();
                if(path.equals("/weather-data")) {
                    updateUI(dataMap);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient != null) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
