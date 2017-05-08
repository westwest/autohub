package se.acoder.autohub.dashboard.speedometer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.dashboard.GPSFragment;

/**
 * Created by Johannes Westlund on 2017-03-19.
 */

public class SpeedometerFragment extends GPSFragment {
    private SpeedometerView speedometer;
    private int speedLimit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        speedometer = new SpeedometerView(getContext());
        speedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLimiter();
            }
        });
        return speedometer;
    }

    private void toggleLimiter(){
        boolean isLimitEnabled = speedLimit < 0;
        /*
        if(isLimitEnabled)
            initLimiter();
        else
            speedLimit = -1;*/
    }

    @Override
    protected void newLocation(Location location) {
        speedometer.setSpeed(location.getSpeed());
    }
}
