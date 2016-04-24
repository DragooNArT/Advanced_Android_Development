package com.example.sunshinewearapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class SunshineDataReceiver extends WearableListenerService {

    public static final String WEATHER_ID_KEY = "WEATHER_ID_DRAWABLE";

    public static final String WEATHER_LOW_KEY = "WEATHER_LOW_KEY";

    public static final String WEATHER_HIGH_KEY = "WEATHER_HIGH_KEY";
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
      for(DataEvent event : dataEvents) {
          if(event.getType() == DataEvent.TYPE_CHANGED) {
              DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
              String path = event.getDataItem().getUri().getPath();
              if(path.equals("/weather-data")) {
                  int weatherDrawableId = getArtResourceForWeatherCondition(dataMap.getInt("WEATHER_ID_KEY"));
                  Intent intent = new Intent( this, MainSunshineWearActivity.class );
                  intent.putExtra(WEATHER_ID_KEY,weatherDrawableId);
                  intent.putExtra(WEATHER_HIGH_KEY,dataMap.getDouble("WEATHER_HIGH_KEY"));
                  intent.putExtra(WEATHER_LOW_KEY,dataMap.getDouble("WEATHER_LOW_KEY"));
                  intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                  startActivity( intent );
              }
          }
      }
    }


    private int getArtResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
       if(messageEvent.getPath().equals("path defined on other side")) {
           //TODO do stuff with msg
       }
    }

}
