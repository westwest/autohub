package se.acoder.autohub.hub.products.InternetRadio;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import se.acoder.autohub.HubApp;
import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.Product;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class InternetRadioProduct extends Product {

    public InternetRadioProduct(Context context){
        super("Radio", R.drawable.ic_radio);
        Intent requestServiceStart = new Intent(context.getPackageName() + "." + HubApp.SERVICE_REQUEST_INTENT);
        Intent startService = new Intent(context, RadioService.class);
        requestServiceStart.putExtra(HubApp.SERVICE_CREATION_INTENT, startService);
        context.sendBroadcast(requestServiceStart);
    }

    @Override
    public Fragment bootstrap() {
        return new ChannelListFragment();
    }

    protected enum KEYS {
        WAS_PLAYING
    }


}
