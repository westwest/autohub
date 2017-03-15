package se.acoder.autohub.frontend.CustomViews.TravelInfoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Target;

import se.acoder.autohub.R;
import se.acoder.autohub.frontend.CustomViews.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-14.
 */

public class Grid extends DrawView {
    private int speedWidth = 200;
    private int vMid = 30;

    private Paint linePaint, bgPaint;

    public Grid(Context context) {
        super(context);
        init(context);
    }

    public Grid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        linePaint.setStrokeWidth(4);
        linePaint.setStyle(Paint.Style.STROKE);

        bgPaint = new Paint();
        bgPaint.setColor(ContextCompat.getColor(context, android.R.color.background_dark));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(parentWidth, (int) toPx(vMid*2, getResources()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources r = getResources();

        Path dash = new Path();
        dash.moveTo(toPx(-10, r), toPx(vMid, r));
        dash.lineTo(toPx(getWidth()+10, r), toPx(vMid, r));
        dash.lineTo(toPx(getWidth()+10, r), toPx(2*vMid+10, r));
        dash.lineTo(toPx(-10, r), toPx(2*vMid+10, r));
        dash.close();

        canvas.drawPath(dash, bgPaint);
        canvas.drawPath(dash, linePaint);

        Path speedMeter = new Path();
        int startX = getWidth()/2;
        speedMeter.moveTo(startX, 0);
        speedMeter.lineTo(startX + toPx(speedWidth, r)/2, 0);
        speedMeter.lineTo(startX + toPx(speedWidth, r)/2+ toPx(50, r), toPx(vMid, r));
        speedMeter.lineTo(startX + toPx(speedWidth, r)/2, toPx(2*vMid, r));
        speedMeter.lineTo(startX - toPx(speedWidth, r)/2, toPx(2*vMid, r));
        speedMeter.lineTo(startX - toPx(speedWidth, r)/2- toPx(50, r), toPx(vMid, r));
        speedMeter.lineTo(startX - toPx(speedWidth, r)/2, 0);
        speedMeter.close();

        canvas.drawPath(speedMeter, bgPaint);
        canvas.drawPath(speedMeter, linePaint);
    }
}
