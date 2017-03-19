package se.acoder.autohub;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import se.acoder.autohub.hub.HubMenuFragment;
import se.acoder.autohub.hub.products.Trip.TripManager;
import se.acoder.autohub.dashboard.DayInfoView.DayInfoView;
import se.acoder.autohub.dashboard.TravelInfoView.TravelInfoView;

public class HubApp extends AppCompatActivity {
    private DayInfoView dayInfo;
    private TravelInfoView travelInfo;

    private FragmentManager FM;
    private LocationManager LM;
    private LocationListener LS;
    private TripManager TM;

    //Permission request constants
    private final int GPS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        HubMenuFragment mainMenu = new HubMenuFragment();
        FT.add(R.id.mainView, mainMenu);
        FT.commit();

        dayInfo = (DayInfoView) findViewById(R.id.diView);
        travelInfo = (TravelInfoView) findViewById(R.id.tiView);

        LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TM = new TripManager();

        LS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                TM.computeLocation(location);
                travelInfo.update(TM.getTripDistance(), TM.getTripTime());
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
        registerGPS();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LM.removeUpdates(LS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TM.endTrip();
    }

    private void registerGPS(){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, GPS_REQUEST);
            return;
        }
        LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LS);
    }

    /**
     * GPS-request w mTime and mDistance set to 0.
     * @param LM
     * @param LS
     */
    public void requestGPS(LocationManager LM, LocationListener LS){
        requestGPS(LM, 0, 0, LS);
    }

    public void requestGPS(LocationManager LM, int mTime, int mDistance, LocationListener LS){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, GPS_REQUEST);
            return;
        }
        LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, mTime, mDistance, LS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case GPS_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    registerGPS();
                return;
            }
        }
    }

    public abstract class FeatureService extends Service {

    }
}
