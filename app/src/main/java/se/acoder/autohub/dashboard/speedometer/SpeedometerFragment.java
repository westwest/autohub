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
    private float lastSpeed, speedLimit;

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
        boolean isLimitEnabled = speedLimit != 0;
        if(isLimitEnabled)
            speedLimit = -0f;
        else
            speedLimit = lastSpeed;
        speedometer.setLimited(!isLimitEnabled);
    }

    @Override
    protected void newLocation(Location location) {
        LimitState state = LimitState.NO_LIMIT;
        if(speedLimit > 0f){
            if(location.getSpeed() <= speedLimit)
                state = LimitState.BELOW_LIMIT;
            else if(location.getSpeed() > speedLimit){
                state = LimitState.ABOVE_LIMIT;
            }
        }

        speedometer.setSpeed(location.getSpeed(), state);
        lastSpeed = location.getSpeed();
    }

    protected enum LimitState {
        NO_LIMIT, ABOVE_LIMIT, BELOW_LIMIT;
    }
}
