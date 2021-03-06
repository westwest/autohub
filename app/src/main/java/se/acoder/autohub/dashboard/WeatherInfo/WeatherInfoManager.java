package se.acoder.autohub.dashboard.WeatherInfo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import se.acoder.autohub.BuildConfig;
import se.acoder.autohub.HubApp;

/**
 * Created by Johannes Westlund on 2017-03-23.
 */

public class WeatherInfoManager {
    private final String key = BuildConfig.open_weather_key;
    public static final String weatherUrl = "http://api.openweathermap.org/data/2.5/weather";
    public static final String iconBaseUrl = "http://api.openweathermap.org/img/w/";
    private final int minutesInMs = 1000*60;

    private static boolean isRunning;

    private Context context;

    private LocationManager LM;
    private LocationListener LS;
    private Location lastReqLocation;

    private static List<WeatherInfoListener> listeners = new ArrayList<>();

    public WeatherInfoManager(Context context){
        final HubApp activity = (HubApp) context;
        this.context = context;
        LM = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(lastReqLocation==null ||
                        lastReqLocation.distanceTo(location) > 10000 ||
                        location.getTime() - lastReqLocation.getTime() > 60*minutesInMs){
                    lastReqLocation = location;
                    new WeatherFetchTask().execute(location);
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    public void requestUpdates(WeatherInfoListener receiver){
        listeners.add(receiver);
        if(!isRunning){
            ((HubApp) context).requestGPS(LM,LS);
            isRunning = true;
        }
    }

    public void unregisterUpdates(WeatherInfoListener receiver){
        listeners.remove(receiver);
        if(listeners.size() == 0) {
            isRunning = false;
            ((HubApp) context).unregisterGPS(LM,LS);
        }
    }

    private class WeatherFetchTask extends AsyncTask<Location,Void,WeatherInfo> {
        @Override
        protected WeatherInfo doInBackground(Location... params) {
            Location l = params[0];
            try {
                HttpURLConnection weatherDataConnection = setupConnection(weatherUrl +
                    "?lat=" + l.getLatitude() + "&lon=" + l.getLongitude() + "&appid=" + key);

                JSONObject weatherData = new JSONObject(readWeatherJSON(weatherDataConnection));

                String iconId =  weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");
                HttpURLConnection iconConnection = setupConnection(iconBaseUrl + iconId + ".png");
                Drawable icon = downloadIcon(iconConnection);

                return new WeatherInfo(weatherData.getJSONObject("main").getDouble("temp"),
                                       weatherData.getJSONObject("wind").getDouble("speed"),
                                       weatherData.getInt("visibility"),
                                       weatherData.getString("name"),
                                       icon);
            } catch (MalformedURLException ME){
                Log.d(this.getClass().toString(), ME.getCause().toString());
                ME.printStackTrace();
            } catch (IOException IOE){
                Log.d(this.getClass().toString(), IOE.getCause().toString());
                IOE.printStackTrace();
            } catch (JSONException JSONE){
                Log.d(this.getClass().toString(), JSONE.getCause().toString());
                JSONE.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherInfo weatherInfo) {
            for(WeatherInfoListener l : listeners){ l.onInfoUpdate(weatherInfo); }
        }

        private HttpURLConnection setupConnection(String url) throws IOException{
            URL request = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) request.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(10000);
            httpCon.connect();
            return httpCon;
        }

        private String readWeatherJSON(HttpURLConnection httpCon) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        }

        private Drawable downloadIcon(HttpURLConnection httpCon) throws IOException {
            return new BitmapDrawable(context.getResources(),
                                      BitmapFactory.decodeStream(httpCon.getInputStream()));
        }
    }

    public interface WeatherInfoListener {
        public void onInfoUpdate(WeatherInfo weatherInfo);
    }
}
