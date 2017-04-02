package se.acoder.autohub.hub.products.Phone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.Product;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class PhoneProduct extends Product {

    public PhoneProduct(){
        super("Telephone", R.drawable.ic_phone);
    }

    @Override
    public Fragment bootstrap() {
        return new PhoneHubFragment();
    }

    @Override
    public boolean ensureGatePermission(Context context) {
        if(ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    (Activity)context,new String[] { Manifest.permission.READ_CONTACTS }, HubApp.PHONE_GATE_REQUEST);
            return false;
        }
        return true;
    }
}
