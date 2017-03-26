package se.acoder.autohub.dashboard.WeatherInfo;

import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johannes Westlund on 2017-03-23.
 */

public class WeatherInfo {
    private double absTemp;
    private double windSpeed;
    private int visibility;
    private Drawable icon;

    private WeatherInfo(double absTemp, double windSpeed, int visibility, Drawable icon ){
        this.absTemp = absTemp;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.icon = icon;
    }

    public static WeatherInfo parseWeatherData(JSONObject weatherdata){
        try {
            Log.d("TEST", weatherdata.toString());

            JSONObject weather = weatherdata.getJSONArray("weather").getJSONObject(0);
            Drawable icon = loadIcon(weather.getString("icon"));

            JSONObject main = weatherdata.getJSONObject("main");
            double absTemp = main.getDouble("temp");

            int visibility = weatherdata.getInt("visibility");

            JSONObject wind = weatherdata.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed");
            return new WeatherInfo(absTemp, windSpeed, visibility, icon);
        } catch (JSONException JSONE){
            Log.d(WeatherInfo.class.getName(), JSONE.getCause().toString());
            JSONE.printStackTrace();
        }
        return null;
    }

    private static Drawable loadIcon(String iconName){
        String iconUrl = WeatherInfoManager.imageUrl + iconName;
        return null;
    }
}
