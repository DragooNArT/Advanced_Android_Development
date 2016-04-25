package com.example.andorid.sunshine.app;

import com.example.andorid.R;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class SunshineDataReceiver extends WearableListenerService {

    public static final String WEATHER_ID_KEY = "WEATHER_ID_DRAWABLE";

    public static final String WEATHER_LOW_KEY = "WEATHER_LOW_KEY";

    public static final String WEATHER_HIGH_KEY = "WEATHER_HIGH_KEY";



//    @Override
//    public void onDataChanged(DataEventBuffer dataEvents) {
//      for(DataEvent event : dataEvents) {
//          if(event.getType() == DataEvent.TYPE_CHANGED) {
//              DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
//              String path = event.getDataItem().getUri().getPath();
//              if(path.equals("/weather-data")) {
//                  int weatherDrawableId = getArtResourceForWeatherCondition(dataMap.getInt("WEATHER_ID_KEY"));
//                  Intent intent = new Intent( this, MainSunshineWearActivity.class );
//                  intent.putExtra(WEATHER_ID_KEY,weatherDrawableId);
//                  intent.putExtra(WEATHER_HIGH_KEY,dataMap.getDouble("WEATHER_HIGH_KEY"));
//                  intent.putExtra(WEATHER_LOW_KEY,dataMap.getDouble("WEATHER_LOW_KEY"));
//                  intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//                  startActivity( intent );
//              }
//          }
//      }
//    }


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
