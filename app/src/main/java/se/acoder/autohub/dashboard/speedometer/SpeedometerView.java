package se.acoder.autohub.dashboard.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import se.acoder.autohub.dashboard.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-19.
 */

public class SpeedometerView extends DrawView {
    private String speed = "0";
    private String unit = "km/h";

    private final int textSize = 28;
    private float speedOffset, textHeight;

    public SpeedometerView(Context context) {
        super(context);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context){
        super.init(context);
        textPaint.setTextSize(toPx(textSize,getResources()));
        textPaint.setTextAlign(Paint.Align.RIGHT);

        Rect bounds = new Rect();
        String dimText = "000";
        textPaint.getTextBounds(dimText,0,dimText.length(),bounds);
        speedOffset = bounds.width();
        textHeight = bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources r = getResources();

        Path speedMeter = new Path();
        float skewDeltaX = getWidth()*0.20f;
        float height = getHeight()-toPx(30,r);

        speedMeter.moveTo(0, getHeight()/2);
        speedMeter.lineTo(skewDeltaX, getHeight()/2-height);
        speedMeter.lineTo(getWidth()-skewDeltaX, getHeight()/2-height);
        speedMeter.lineTo(getWidth(), getHeight()/2);
        speedMeter.lineTo(getWidth()-skewDeltaX, getHeight()/2+height);
        speedMeter.lineTo(skewDeltaX, getHeight()/2+height);
        speedMeter.close();

        canvas.drawPath(speedMeter, bgPaint);
        canvas.drawPath(speedMeter, linePaint);

        canvas.drawText(speed,skewDeltaX+speedOffset, getHeight()/2+textHeight/2, textPaint);
        canvas.drawText(unit,getWidth()-skewDeltaX, getHeight()/2+textHeight/2, textPaint);
    }

    public void setSpeed(float speed){
        this.speed = Math.round(speed * 3.6) + "";
        this.invalidate();
    }
}
