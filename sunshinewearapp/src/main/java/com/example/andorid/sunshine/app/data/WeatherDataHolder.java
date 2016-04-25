package com.example.andorid.sunshine.app.data;

/**
 * Created by DragooNArT-PC on 4/25/2016.
 */
public class WeatherDataHolder {
    private int weatherId;

    private int low_temp;

    private int high_temp;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHigh_temp() {
        return high_temp;
    }

    public void setHigh_temp(int high_temp) {
        this.high_temp = high_temp;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getLow_temp() {
        return low_temp;
    }

    public void setLow_temp(int low_temp) {
        this.low_temp = low_temp;
    }
}
