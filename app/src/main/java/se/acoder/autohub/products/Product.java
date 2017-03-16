package se.acoder.autohub.products;

import android.support.v4.app.Fragment;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public abstract class Product {
    int iconResId;
    String name;

    public Product(String name, int resId){
        this.name = name;
        iconResId = resId;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return iconResId;
    }

    public abstract Fragment bootstrap();
}
