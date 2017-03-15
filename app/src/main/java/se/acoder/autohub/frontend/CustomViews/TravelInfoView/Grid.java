package se.acoder.autohub.frontend.CustomViews.TravelInfoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Target;

/**
 * Created by Johannes Westlund on 2017-03-14.
 */

public class Grid extends View {
    private int speedWidth = 500;
    private int vMid = 70;

    private Paint linePaint;

    public Grid(Context context) {
        super(context);
        init();
    }

    public Grid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(4);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(parentWidth, vMid*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0,vMid,getWidth()/2-(speedWidth/2)-100, vMid, linePaint);
        canvas.drawLine(getWidth()/2+(speedWidth/2)+100, vMid, getWidth(), vMid, linePaint);

        Path speedMeter = new Path();
        int startX = getWidth()/2;
        speedMeter.moveTo(startX, 0);
        speedMeter.lineTo(startX + speedWidth/2, 0);
        speedMeter.lineTo(startX + speedWidth/2+100, vMid);
        speedMeter.lineTo(startX + speedWidth/2, 2*vMid);
        speedMeter.lineTo(startX - speedWidth/2, 2*vMid);
        speedMeter.lineTo(startX - speedWidth/2-100, vMid);
        speedMeter.lineTo(startX - speedWidth/2, 0);
        speedMeter.close();
        canvas.drawPath(speedMeter, linePaint);
    }
}
