package se.acoder.autohub.hub.products.NavigationProduct;

import android.support.v4.app.Fragment;

import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.Product;

/**
 * Created by Johannes on 2017-03-17.
 */

public class NavigationProduct extends Product {

    public NavigationProduct(){
        super("Navigation", R.drawable.ic_navigation);
    }

    @Override
    public Fragment bootstrap() {
        return null;
    }
}
