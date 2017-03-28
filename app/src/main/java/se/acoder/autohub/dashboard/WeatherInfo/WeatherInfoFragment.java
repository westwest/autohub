package se.acoder.autohub.dashboard.WeatherInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-27.
 */

public class WeatherInfoFragment extends Fragment {
    private WeatherInfoView weatherInfoView;

    private WeatherInfoManager WIM;
    private WeatherInfoManager.WeatherInfoListener WIL;

    private boolean wCold, wWind, wVisibility;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WIM = new WeatherInfoManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherinfo_layout, container, false);
        weatherInfoView = (WeatherInfoView) rootView.findViewById(R.id.weatherDash);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        WIM.requestUpdates(new WeatherInfoManager.WeatherInfoListener() {
            @Override
            public void onInfoUpdate(WeatherInfo weatherInfo) {
                weatherInfoView.setTemp(weatherInfo.getTempMetric()+"");
                weatherInfoView.setIcon(weatherInfo.getIcon());
                weatherInfoView.setLocation(weatherInfo.getLocation());
                checkAlerts(weatherInfo);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        WIM.unregisterUpdates(WIL);
    }

    private void checkAlerts(WeatherInfo weatherInfo){
        if(!wCold && weatherInfo.getTempMetric() < 4){
            wCold = true;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Cold, wCold);
        } else if(wCold && weatherInfo.getTempMetric() > 4){
            wCold = false;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Cold, wCold);
        }
        if(!wWind && weatherInfo.getWindSpeed() > 18){
            wWind = true;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Wind, wWind);
        } else if(wWind && weatherInfo.getWindSpeed() < 18){
            wWind = false;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Wind, wWind);
        }
        if(!wVisibility && weatherInfo.getVisibility() < 500){
            wVisibility = true;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Visibility, wVisibility);
        } else if(wVisibility && weatherInfo.getVisibility() > 500){
            wVisibility = false;
            weatherInfoView.toggleAlert(WeatherInfoView.Alerts.Visibility, wVisibility);
        }
    }
}
