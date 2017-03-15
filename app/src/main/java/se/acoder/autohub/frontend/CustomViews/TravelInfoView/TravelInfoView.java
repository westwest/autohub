package se.acoder.autohub.frontend.CustomViews.TravelInfoView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-14.
 */
public class TravelInfoView extends ConstraintLayout {
    private View rootView;
    private TextView vDistance, vSpeed, vTime;
    private Grid grid;

    private int mainColor;


    public TravelInfoView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TravelInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TravelInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        /*
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TravelInfoView, defStyle, 0);

        mainColor = a.getColor(
                R.styleable.TravelInfoView_exampleColor,
                mainColor);
        a.recycle();*/

        rootView = inflate(context, R.layout.travel_info_layout, this);

        grid = new Grid(context, attrs, defStyle);
        addView(grid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}