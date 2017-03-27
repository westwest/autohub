package se.acoder.autohub;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.HashMap;

import se.acoder.autohub.dashboard.WeatherInfo.WeatherInfo;
import se.acoder.autohub.dashboard.WeatherInfo.WeatherInfoManager;
import se.acoder.autohub.hub.HubMenuFragment;
import se.acoder.autohub.hub.products.ProductService;
import se.acoder.autohub.hub.products.Trip.TripManager;
import se.acoder.autohub.dashboard.TravelInfoView.TravelInfoView;

public class HubApp extends AppCompatActivity {
    private TravelInfoView travelInfo;

    private SessionRequestReceiver SRReceiver;
    private boolean srrRegistered = false;
    private HashMap<Integer,IBinder> servicePool = new HashMap<>();

    private FragmentManager FM;
    private LocationManager LM;
    private LocationListener LS;
    private TripManager TM;
    private WeatherInfoManager WIM;
    private WeatherInfoManager.WeatherInfoReceiver WIR;

    //Permission request constants
    private final int GPS_REQUEST = 1;

    //Intent-keys
    public static final String SERVICE_REQUEST_INTENT = "service_request_intent";
    public static final String SERVICE_CREATION_INTENT = "service_creation_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hub);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FM = getSupportFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        HubMenuFragment mainMenu = new HubMenuFragment();
        FT.add(R.id.mainView, mainMenu);
        FT.commit();

        travelInfo = (TravelInfoView) findViewById(R.id.tiView);

        SRReceiver = new SessionRequestReceiver();
        SRReceiver.registerSelf();
        LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        WIM = new WeatherInfoManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        WIM.requestUpdates(new WeatherInfoManager.WeatherInfoReceiver() {
            @Override
            public void onInfoUpdate(WeatherInfo weatherInfo) {

            }
        });

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
    protected void onResume() {
        super.onResume();
        if(!srrRegistered) {
            SRReceiver.registerSelf();
            srrRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(SRReceiver);
        srrRegistered = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        LM.removeUpdates(LS);
        // WIM.endUpdates(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TM.endTrip();
    }

    public IBinder getServiceInterface(int serviceHash){
        return servicePool.get(serviceHash);
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

    public void unregisterGPS(LocationManager LM, LocationListener LS){
        LM.removeUpdates(LS);
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

    private class SessionRequestReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceCreator = intent.getParcelableExtra(SERVICE_CREATION_INTENT);
            bindService(serviceCreator, new GenericServiceConnection(), BIND_AUTO_CREATE);
        }
        private void registerSelf(){
            registerReceiver(SRReceiver, new IntentFilter(getPackageName() + "." + SERVICE_REQUEST_INTENT));
        }
    }

    private class GenericServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ProductService boundService = ((ProductService.ProductServiceBinder)service).getBaseService();
            servicePool.put(boundService.hashCode(), service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
