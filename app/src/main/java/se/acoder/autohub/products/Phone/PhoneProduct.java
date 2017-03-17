package se.acoder.autohub.products.Phone;

import android.support.v4.app.Fragment;

import se.acoder.autohub.R;
import se.acoder.autohub.products.Product;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public class PhoneProduct extends Product {

    public PhoneProduct(){
        super("Telephone", R.drawable.ic_phone);
    }

    @Override
    public Fragment bootstrap() {
        return null;
    }
}
