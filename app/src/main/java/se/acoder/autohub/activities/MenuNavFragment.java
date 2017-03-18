package se.acoder.autohub.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-18.
 */

public class MenuNavFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView backButton = new TextView(getContext());
        backButton.setText("Back");
        backButton.setTextSize(24);
        Drawable backIcon = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back);
        backButton.setCompoundDrawablesWithIntrinsicBounds(backIcon, null, null, null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
            }
        });
        return backButton;
    }
}
