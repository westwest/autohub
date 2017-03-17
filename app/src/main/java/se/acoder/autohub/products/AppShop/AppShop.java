package se.acoder.autohub.products.AppShop;

import android.support.v4.app.Fragment;

import se.acoder.autohub.R;
import se.acoder.autohub.products.Product;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class AppShop extends Product {

    public AppShop(){
        super("App Store", R.drawable.ic_add_shopping_cart);
    }

    @Override
    public Fragment bootstrap() {
        return null;
    }
}
