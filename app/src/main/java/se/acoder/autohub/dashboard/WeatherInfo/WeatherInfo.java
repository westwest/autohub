package se.acoder.autohub.dashboard.WeatherInfo;

import android.graphics.drawable.Drawable;

/**
 * Created by Johannes Westlund on 2017-03-23.
 */

public class WeatherInfo {
    private double absTemp;
    private double windSpeed;
    private int visibility;
    private Drawable icon;

    public WeatherInfo(double absTemp, double windSpeed, int visibility, Drawable icon ){
        this.absTemp = absTemp;
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.icon = icon;
    }

    public double getTempKelvin(){
        return absTemp;
    }
    public double getTempMetric(){
        return absTemp-273.15;
    }

    public double getWindSpeedt(){
        return windSpeed;
    }

    public int getVisibility() {
        return visibility;
    }

    public Drawable getIcon() {
        return icon;
    }
}
