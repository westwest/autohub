package se.acoder.autohub.hub.products.Trip;

import android.location.Location;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class TripManager {
    private boolean ongoing;
    private float distance;
    private long time;
    private Location lastLocation;
    private Location lastSignificantLocation;
    private Location start;

    private final int STATIONARY_DELTA = 20*1000*60;

    public TripManager(){
        resetValues();
    }

    public void computeLocation(Location location){
        if(!ongoing && lastSignificantLocation != null &&
                lastSignificantLocation.distanceTo(location) > 50)
            ongoing = true;

        if(ongoing) {
            if(lastSignificantLocation.distanceTo(location) > 50){
                lastSignificantLocation = location;
            }
            if(location.getTime() - lastSignificantLocation.getTime() > STATIONARY_DELTA){
                endTrip();
                return;
            }
            distance += lastLocation.distanceTo(location);
            time += location.getTime() - lastLocation.getTime();
        } else if(lastSignificantLocation != null){
            if(lastSignificantLocation.distanceTo(location) > 50){
                start = lastSignificantLocation;
                ongoing = true;
            } else if (lastSignificantLocation.getTime() > 10*1000){
                lastSignificantLocation = null;
            } else {
                lastSignificantLocation = location;
            }
        }
        lastLocation = location;
    }

    public void endTrip(){
        if(ongoing) {
            saveTrip();
            resetValues();
        }
    }

    private void resetValues(){
        lastSignificantLocation = null;
        lastLocation = null;
        time = 0;
        distance = 0;
        ongoing = false;
    }

    private void saveTrip(){
        //TODO: Unimplemented method
    }

    public float getTripDistance(){
        return distance;
    }

    public long getTripTime(){
        return time;
    }
}
