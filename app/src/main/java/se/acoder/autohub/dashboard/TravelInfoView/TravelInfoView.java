package se.acoder.autohub.dashboard.TravelInfoView;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import se.acoder.autohub.R;

/**
 * Created by Johannes Westlund on 2017-03-14.
 */
public class TravelInfoView extends ConstraintLayout {
    private View rootView;
    private TextView vDistance, vTime;
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
        vTime = (TextView) findViewById(R.id.ti_time);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void update(Float distance, Long time){
        if(distance != null)
            vDistance.setText(formatDistance(distance));
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