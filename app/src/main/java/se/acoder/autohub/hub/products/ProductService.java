package se.acoder.autohub.hub.products;

import android.app.Service;
import android.os.Binder;

/**
 * Created by Johannes Westlund on 2017-03-21.
 */

public abstract class ProductService extends Service {

    @Override
    public int hashCode(){
        return getClass().toString().hashCode();
    }

    /**
     * A specific ProductService is unique, so if types match
     * they are considered equal.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass();
    }

    public class ProductServiceBinder extends Binder {
        public ProductService getBaseService(){
            return ProductService.this;
        }
    }
}
