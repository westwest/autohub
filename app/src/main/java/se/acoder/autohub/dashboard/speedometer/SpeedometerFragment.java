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

/**
 * Created by Johannes Westlund on 2017-03-19.
 */

public class SpeedometerFragment extends Fragment {
    private SpeedometerView speedometer;

    private LocationManager LM;
    private LocationListener LS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        speedometer = new SpeedometerView(getContext());
        initGPS();
        return speedometer;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HubApp) getActivity()).requestGPS(LM, LS);

    }

    @Override
    public void onPause() {
        super.onPause();
        LM.removeUpdates(LS);
    }

    private void initGPS() {
        LM = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                speedometer.setSpeed(location.getSpeed());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
}
