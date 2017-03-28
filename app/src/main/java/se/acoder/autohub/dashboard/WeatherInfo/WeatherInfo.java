package se.acoder.autohub.dashboard.WeatherInfo;

import android.graphics.drawable.Drawable;

/**
 * Created by Johannes Westlund on 2017-03-23.
 */

public class WeatherInfo {
    private double absTemp;
    private double windSpeed;
    private int visibility;
    private String location;
    private Drawable icon;

    public WeatherInfo(double absTemp, double windSpeed, int visibility, String location, Drawable icon ){
        this.absTemp = absTemp;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.icon = icon;
        this.location = location;
    }

    public double getTempKelvin(){
        return absTemp;
    }
    public int getTempMetric(){
        return (int)Math.round(absTemp-273.15);
    }

    public double getWindSpeed(){
        return windSpeed;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getLocation() {
        return location;
    }

    public Drawable getIcon() {
        return icon;
    }
}
