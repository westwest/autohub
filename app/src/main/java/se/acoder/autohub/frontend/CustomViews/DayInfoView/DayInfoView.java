package se.acoder.autohub.frontend.CustomViews.DayInfoView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-15.
 */

public class DayInfoView extends RelativeLayout {
    private View rootView;
    private Grid grid;

    public DayInfoView(Context context) {
        super(context);
        init(context);
    }

    public DayInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DayInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        rootView = inflate(context, R.layout.day_info_layout,this);

        grid = new Grid(context);
        addView(grid);
    }
}
