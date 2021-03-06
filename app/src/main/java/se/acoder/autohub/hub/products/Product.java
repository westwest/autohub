package se.acoder.autohub.hub.products;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by Johannes Westlund on 2017-03-16.
 */

public abstract class Product {
    private int iconResId;
    private String name;

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

    public abstract boolean ensureGatePermission(Context context);

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product){
            Product p = (Product) obj;
            return this.name.equals(p.name);
        }
        return false;
    }
}
