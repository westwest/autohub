package se.acoder.autohub.hub.products;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-17.
 */

public class ProductFragment extends Fragment {
    private final String STORE_NAME = "storedProductStates";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_view_layout, container, false);
        rootView.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return rootView;
    }

    protected SharedPreferences getStoredProductStates(){
        return getContext().getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
    }
}
