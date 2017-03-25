package se.acoder.autohub.dashboard.WeatherInfo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import se.acoder.autohub.HubApp;

/**
 * Created by Johannes Westlund on 2017-03-23.
 */

public class WeatherInfoManager {
    private final String requestUrl = "api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}";
    private final int minutesInMs = 1000*60;

    private static boolean isRunning;

    private Context context;

    private LocationManager LM;
    private LocationListener LS;
    private Location lastReqLocation;

    private static List<WeatherInfoReceiver> listeners = new ArrayList<>();

    public WeatherInfoManager(Context context){
        final HubApp activity = (HubApp) context;
        LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(lastReqLocation.distanceTo(location) > 10000 ||
                        location.getTime() - lastReqLocation.getTime() > 60*minutesInMs){
                    requestWeather();
                }
                activity.unregisterGPS(LM, LS);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    public void requestUpdates(WeatherInfoReceiver receiver){
        listeners.add(receiver);
        if(!isRunning){
            ((HubApp) context).requestGPS(LM,LS);
            isRunning = true;
        }
    }

    public void endUpdates(WeatherInfoReceiver receiver){
        listeners.remove(receiver);
        if(listeners.size() == 0)
            isRunning = false;
    }

    private void requestWeather(){

    }

    private class WeatherFetchTask extends TimerTask {

        @Override
        public void run() {

        }
    }

    public interface WeatherInfoReceiver {
        public void onInfoUpdate(WeatherInfo weatherInfo);
    }
}
