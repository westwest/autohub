package se.acoder.autohub.hub.products.Phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.acoder.autohub.R;
import se.acoder.autohub.hub.products.ProductFragment;

/**
 * Created by Johannes Westlund on 2017-03-29.
 */

public class PhoneHubFragment extends ProductFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_view_layout, container, false);
        ViewGroup productView= (ViewGroup) rootView.findViewById(R.id.product_view);
        View phoneView = inflater.inflate(R.layout.phone_main, productView, false);
        productView.addView(phoneView);
        return rootView;
    }
}
