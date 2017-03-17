package se.acoder.autohub.products.InternetRadio;

import android.support.v4.app.Fragment;

import se.acoder.autohub.R;
import se.acoder.autohub.products.Product;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class InternetRadioProduct extends Product {

    public InternetRadioProduct(){
        super("Radio", R.drawable.ic_radio);
    }

    @Override
    public Fragment bootstrap() {
        return null;
    }
}
