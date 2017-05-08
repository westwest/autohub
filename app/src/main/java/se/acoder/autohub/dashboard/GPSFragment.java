package se.acoder.autohub.dashboard;

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
import se.acoder.autohub.dashboard.speedometer.SpeedometerView;

/**
 * Created by johves on 2017-05-08.
 */

public abstract class GPSFragment extends Fragment {

    private LocationManager LM;
    private LocationListener LS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initGPS();
        return super.onCreateView(inflater,container,savedInstanceState);

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
                newLocation(location);
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

    protected abstract void newLocation(Location location);
}