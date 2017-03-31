package se.acoder.autohub.hub.products.Phone;

import android.support.v4.app.Fragment;

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
}
