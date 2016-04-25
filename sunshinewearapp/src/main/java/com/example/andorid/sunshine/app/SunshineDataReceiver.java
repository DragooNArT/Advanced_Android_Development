package com.example.andorid.sunshine.app;

import com.example.andorid.R;
import com.example.andorid.sunshine.app.data.WeatherDataHolder;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class SunshineDataReceiver extends WearableListenerService {


    private static WeatherDataHolder holder;

    public static WeatherDataHolder getLatestWeatherData() {
        return holder;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
      for(DataEvent event : dataEvents) {
          if(event.getType() == DataEvent.TYPE_CHANGED) {
              DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
              String path = event.getDataItem().getUri().getPath();
              if(path.equals("/weather-data")) {
                  holder = new WeatherDataHolder();
                  holder.setWeatherId(dataMap.getInt(Constants.WEATHER_ID_KEY));
                  holder.setHigh_temp(dataMap.getInt(Constants.WEATHER_HIGH_KEY));
                  holder.setLow_temp(dataMap.getInt(Constants.WEATHER_LOW_KEY));
                  holder.setDate(dataMap.getString(Constants.WEATHER_CURRENT_DATE_KEY));
              }
          }
      }
    }


    public static int getArtResourceForWeatherCondition(long weatherId) {
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
        if (messageEvent.getPath().equals("/notify-weather")) {
//            try {
                String weatherDataString = new String(messageEvent.getData());
                System.out.println("weatherDataString: " + weatherDataString);
//               Intent intent = new Intent(this, MainSunshineWearActivity.class);
//               intent.putExtra(SunshineDataReceiver.WEATHER_ID_KEY, dataHolder.getWeatherId());
//               intent.putExtra(SunshineDataReceiver.WEATHER_HIGH_KEY, dataHolder.getHigh_temp());
//               intent.putExtra(SunshineDataReceiver.WEATHER_LOW_KEY, dataHolder.getLow_temp());
//               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//               startActivity(intent);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        }
    }

}
