package com.example.andorid.sunshine.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andorid.R;
import com.example.andorid.sunshine.app.data.WeatherDataHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

public class MainSunshineWearActivity extends WearableActivity implements
        DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String WEATHER_CELSISUS_PREFIX = "°";

    private GoogleApiClient mGoogleApiClient;
    private WatchViewStub stub;

    private boolean hasData = false;

    private WeatherDataHolder latestData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sunshine_wear);
        setAmbientEnabled();
        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        Bundle extras = getIntent().getExtras();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
        if (extras != null && extras.containsKey(Constants.WEATHER_ID_KEY)) {
            //use data from intent to render UI
            final int weatherId = extras.getInt(Constants.WEATHER_ID_KEY);
            final int high = extras.getInt(Constants.WEATHER_HIGH_KEY);
            final int low = extras.getInt(Constants.WEATHER_LOW_KEY);

            final String date = extras.getString(Constants.WEATHER_CURRENT_DATE_KEY);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
                    dateField.setText(date);
                    ImageView image = (ImageView) stub.findViewById(R.id.weather_conditions);
                    image.setImageResource(SunshineDataReceiver.getArtResourceForWeatherCondition(weatherId));
                    TextView high_temp = (TextView) stub.findViewById(R.id.high_temp);
                    high_temp.setText(high + WEATHER_CELSISUS_PREFIX);
                    TextView low_temp = (TextView) stub.findViewById(R.id.temp_low);
                    low_temp.setText(low + WEATHER_CELSISUS_PREFIX);
                }
            });
            hasData = true;
        } else if (SunshineDataReceiver.getLatestWeatherData() != null) {
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    WeatherDataHolder holder = SunshineDataReceiver.getLatestWeatherData();
                    TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
                    dateField.setText(holder.getDate());
                    ImageView image = (ImageView) stub.findViewById(R.id.weather_conditions);
                    image.setImageResource(SunshineDataReceiver.getArtResourceForWeatherCondition(holder.getWeatherId()));
                    TextView high_temp = (TextView) stub.findViewById(R.id.high_temp);
                    high_temp.setText(holder.getHigh_temp() + WEATHER_CELSISUS_PREFIX);
                    TextView low_temp = (TextView) stub.findViewById(R.id.temp_low);
                    low_temp.setText(holder.getLow_temp() + WEATHER_CELSISUS_PREFIX);
                }
            });
            hasData = true;
        } else {

            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
                    GregorianCalendar calendar = new GregorianCalendar();
                    dateField.setText("Today," + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " + calendar.get(Calendar.DAY_OF_MONTH));
                }
            });
        }

    }

    @Override
    public void onEnterAmbient(Bundle ambientData) {
        super.onEnterAmbient(ambientData);
        String burnIn = ambientData.getString(EXTRA_BURN_IN_PROTECTION);
        String lowBit = ambientData.getString(EXTRA_LOWBIT_AMBIENT);
        TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
        dateField.getPaint().setAntiAlias(false);
        ImageView image = (ImageView) stub.findViewById(R.id.weather_conditions);
        TextView high_temp = (TextView) stub.findViewById(R.id.high_temp);
        high_temp.getPaint().setAntiAlias(false);
        TextView low_temp = (TextView) stub.findViewById(R.id.temp_low);
        low_temp.getPaint().setAntiAlias(false);
        stub.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        stub.setBackgroundColor(getResources().getColor(R.color.activity_background));
        TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
        dateField.getPaint().setAntiAlias(true);
        ImageView image = (ImageView) stub.findViewById(R.id.weather_conditions);
        TextView high_temp = (TextView) stub.findViewById(R.id.high_temp);
        high_temp.getPaint().setAntiAlias(true);
        TextView low_temp = (TextView) stub.findViewById(R.id.temp_low);
        low_temp.getPaint().setAntiAlias(true);
        stub.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateUI();
    }

    private void storeWeatherData(final DataMap map) {
        latestData = new WeatherDataHolder();
        latestData.setDate(map.getString(Constants.WEATHER_CURRENT_DATE_KEY));
        latestData.setWeatherId(map.getInt(Constants.WEATHER_ID_KEY));
        latestData.setLow_temp(map.getInt(Constants.WEATHER_LOW_KEY));
        latestData.setHigh_temp(map.getInt(Constants.WEATHER_HIGH_KEY));
    }

    private void updateUI() {
        if (latestData != null) {
            TextView dateField = (TextView) stub.findViewById(R.id.currentDate);
            dateField.setText(latestData.getDate());
            int weatherDrawableId = SunshineDataReceiver.getArtResourceForWeatherCondition(latestData.getWeatherId());

            ImageView image = (ImageView) stub.findViewById(R.id.weather_conditions);
            image.setImageResource(weatherDrawableId);
            TextView high_temp = (TextView) stub.findViewById(R.id.high_temp);
            high_temp.setText(latestData.getHigh_temp() + WEATHER_CELSISUS_PREFIX);
            TextView low_temp = (TextView) stub.findViewById(R.id.temp_low);
            low_temp.setText(latestData.getLow_temp() + WEATHER_CELSISUS_PREFIX);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        //TODO do something with the data
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                String path = event.getDataItem().getUri().getPath();
                if (path.equals("/weather-data")) {
                    storeWeatherData(dataMap);
                    if (!isAmbient()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        if (hasData) {
            //ask mobile device for data
            PutDataMapRequest dataMap = PutDataMapRequest.create("/request-weather-data");
            dataMap.getDataMap().putString("alwaysChanging", UUID.randomUUID().toString());
            Wearable.DataApi.putDataItem(mGoogleApiClient, dataMap.asPutDataRequest()).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                    if (!dataItemResult.getStatus().isSuccess()) {
                        Log.e("SendWearable", "Failed to send data to wearable device");
                    } else {
                        Log.d("SendWearable", "Sent data to wearable device successfully");
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
