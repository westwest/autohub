package se.acoder.autohub.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class ProductFragment extends Fragment {
    private final String STORE_NAME = "storedProductStates";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected SharedPreferences getStoredProductStates(){
        return getContext().getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
    }
}
