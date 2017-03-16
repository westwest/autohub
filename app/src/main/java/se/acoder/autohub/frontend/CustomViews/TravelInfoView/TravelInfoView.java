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
        grid = new Grid(context, attrs, defStyle);
        addView(grid);

        rootView = inflate(context, R.layout.travel_info_layout, this);
        vDistance = (TextView) findViewById(R.id.ti_distance);
        vSpeed = (TextView) findViewById(R.id.ti_speed);
        vTime = (TextView) findViewById(R.id.ti_time);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void update(Float distance, Float speed, Long time){
        if(distance != null)
            vDistance.setText(formatDistance(distance));
        if(speed != null)
            vSpeed.setText(formatSpeed(speed));
        if(time != null){
            vTime.setText(formatTime(time));
        }
    }

    private String formatDistance(float distance){
        return (distance/1000+"").substring(0,3) + " km";
    }

    private String formatSpeed(float speed){
        double km = speed * 3.6;
        return Double.toString(km).substring(0,3) + " km/h";
    }

    private String formatTime(long time){
        float tInMin = time/(60*1000);
        int fullHours = (int) tInMin/60;
        int fullMin = (int) tInMin - fullHours*60;
        return fullHours + "h " + fullMin + "min";
    }
}