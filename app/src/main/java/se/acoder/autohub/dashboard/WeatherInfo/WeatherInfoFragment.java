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
    private WeatherInfoManager WIM;
    private WeatherInfoManager.WeatherInfoListener WIL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WIM = new WeatherInfoManager(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weatherinfo_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        WIM.requestUpdates(new WeatherInfoManager.WeatherInfoListener() {
            @Override
            public void onInfoUpdate(WeatherInfo weatherInfo) {
                Log.d("TEST", weatherInfo.toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        WIM.unregisterUpdates(WIL);
    }
}
