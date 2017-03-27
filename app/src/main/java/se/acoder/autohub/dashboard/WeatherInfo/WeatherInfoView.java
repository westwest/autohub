package se.acoder.autohub.dashboard.WeatherInfo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import se.acoder.autohub.R;
import se.acoder.autohub.dashboard.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-27.
 */

public class WeatherInfoView extends DrawView {

    public WeatherInfoView(Context context) {
        super(context);
    }

    public WeatherInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WeatherInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WeatherInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context){
        super.init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources r = getResources();
        float skewDelta = getWidth()*0.05f;
        int clockWidth = 80;

        Path dash = new Path();
        dash.moveTo(toPx(-10, r),toPx(-10, r));
        dash.lineTo(toPx(-10, r),getHeight()/2);
        dash.lineTo(toPx(0, r), getHeight()/2);
        dash.lineTo(skewDelta, getHeight());
        dash.lineTo(getWidth()/2-skewDelta-toPx(clockWidth,r), getHeight());
        dash.lineTo(getWidth()/2-toPx(clockWidth,r), getHeight()-skewDelta/2);
        dash.lineTo(getWidth()/2+toPx(clockWidth,r), getHeight()-skewDelta/2);
        dash.lineTo(getWidth()/2+skewDelta+toPx(clockWidth,r), getHeight());
        dash.lineTo(getWidth()-skewDelta, getHeight());
        dash.lineTo(getWidth(), getHeight()/2);
        dash.lineTo(getWidth()+ toPx(10, r), getHeight()/2);
        dash.lineTo(getWidth()+ toPx(10, r), toPx(-10, r));
        dash.close();

        canvas.drawPath(dash, bgPaint);
        canvas.drawPath(dash, linePaint);
    }
}
