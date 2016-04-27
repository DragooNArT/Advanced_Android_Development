package com.example.andorid.sunshine.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.SurfaceHolder;

import com.example.andorid.R;
import com.example.andorid.sunshine.app.data.WeatherDataHolder;

import java.util.Random;

/**
 * Created by DragooNArT-PC on 4/24/2016.
 */
public class SunshineWatchFaceService extends CanvasWatchFaceService {


    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }


    private class Engine extends CanvasWatchFaceService.Engine {

        private boolean mLowBitAmbient;
        private boolean burnInProtection;
        Bitmap currentWeatherIcon = null;
        Paint weatherIconPaint = new Paint();
        Paint tempsPaint = new Paint();
        Paint datePaint = new Paint();
        Paint linePaint = new Paint();
        int backgroundColor = 0;
        private int burnInProtectionOffset = 0;

        private WeatherDataHolder dataHolder;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SunshineWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setHotwordIndicatorGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .setShowSystemUiTime(true)
                    .build());


            setupNormalMode();
            tempsPaint.setTextSize(25.0f);
            tempsPaint.setColor(Color.WHITE);
            datePaint.setTextSize(30.0f);
            datePaint.setColor(Color.WHITE);
            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(3.5f);
            linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
            linePaint.setAlpha(65);
            dataHolder = SunshineDataReceiver.getLatestWeatherData();
            if (dataHolder != null) {
                BitmapDrawable tagetWeather = ((BitmapDrawable) getResources().getDrawable(SunshineDataReceiver.getArtResourceForWeatherCondition(dataHolder.getWeatherId()), null));
                currentWeatherIcon = tagetWeather == null ? ((BitmapDrawable) getResources().getDrawable(R.drawable.art_clear, null)).getBitmap() : tagetWeather.getBitmap();
            } else {

                currentWeatherIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.art_clear, null)).getBitmap();
            }
        }

        private void setupNormalMode() {
            tempsPaint.setAntiAlias(true);
            datePaint.setAntiAlias(true);
            linePaint.setAntiAlias(true);
            weatherIconPaint.setAntiAlias(true);
            backgroundColor = getResources().getColor(R.color.activity_background);
        }

        private void setupAmbientMode() {
            tempsPaint.setAntiAlias(false);
            datePaint.setAntiAlias(false);
            linePaint.setAntiAlias(false);

            weatherIconPaint.setAntiAlias(false);
            backgroundColor = Color.BLACK;
        }

        private Bitmap ResizeIcon(Bitmap icon, Rect surface, Boolean filter) {
            float maxImageSize = Math.max(surface.height(), surface.width()) / 3;

            float ratio = Math.min(
                     maxImageSize / icon.getWidth(),
                     maxImageSize / icon.getHeight());
            int width = Math.round( ratio * icon.getWidth());
            int height = Math.round( ratio * icon.getHeight());

            return Bitmap.createScaledBitmap(icon, width,
                    height, filter);
        }
        Random r = new Random();

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            dataHolder = SunshineDataReceiver.getLatestWeatherData();
            if (dataHolder != null) {
                currentWeatherIcon = ((BitmapDrawable) getResources().getDrawable(SunshineDataReceiver.getArtResourceForWeatherCondition(dataHolder.getWeatherId()), null)).getBitmap();

            }
//            else {
//                //TODO remove dataHolder instantiation(for testing purposes)
//                dataHolder = new WeatherDataHolder();
//                Random rand = new Random();
//
//                dataHolder.setLow_temp(rand.nextInt(10));
//                dataHolder.setHigh_temp(dataHolder.getLow_temp() + rand.nextInt(10));
//                dataHolder.setDate("Today, April " + rand.nextInt(30));
//            }
            if(burnInProtection) {
                burnInProtectionOffset = r.nextInt(10)-5;
            }
            invalidate();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {

            canvas.drawColor(backgroundColor);
            if (dataHolder != null) {
                canvas.drawBitmap(ResizeIcon(currentWeatherIcon, bounds, true), bounds.left + bounds.width() / 20 + burnInOffset(), bounds.centerY() + burnInOffset(), weatherIconPaint);
                int tempHorizontal = (int) (bounds.left + bounds.width() / 2.5);
                canvas.drawText(dataHolder.getHigh_temp() + MainSunshineWearActivity.WEATHER_CELSISUS_PREFIX, tempHorizontal + burnInOffset(), bounds.centerY() + bounds.height() / 7 + burnInOffset(), tempsPaint);
                canvas.drawText(dataHolder.getLow_temp() + MainSunshineWearActivity.WEATHER_CELSISUS_PREFIX, tempHorizontal + burnInOffset(), (float) (bounds.centerY() + bounds.height() / 4 + burnInOffset()), tempsPaint);
                canvas.drawLine(bounds.left + bounds.width() / 10, bounds.centerY(), bounds.right - bounds.width() / 6, bounds.centerY(), linePaint);
                canvas.drawText(dataHolder.getDate(), bounds.left + bounds.width() / 10 + burnInOffset(), (float) (bounds.top + bounds.height() / 2.2) + burnInOffset(), datePaint);
            } else {
                canvas.drawText("Weather unavailable...", bounds.left + burnInOffset(), bounds.exactCenterY() + burnInOffset(), datePaint);
            }
            super.onDraw(canvas, bounds);
        }


        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        private int burnInOffset() {
            return burnInProtection ? burnInProtectionOffset : 0;
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            //if it's needed
            burnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT);
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            if (inAmbientMode) {
                //TODO stuff
                burnInProtection = true;
                setupAmbientMode();
            } else {

                burnInProtection = false;
                setupNormalMode();
            }

            invalidate();//refresh UI
        }
    }
}
