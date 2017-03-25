package se.acoder.autohub.dashboard.spedometer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;

import se.acoder.autohub.R;
import se.acoder.autohub.dashboard.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-19.
 */

public class SpeedometerView extends DrawView {
    String speed = "0";
    String unit = "km/h";

    int textSize = 28;
    float speedOffset, textHeight;

    Paint bgPaint, linePaint;
    TextPaint textPaint;

    public SpeedometerView(Context context) {
        super(context);
        init(context);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SpeedometerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
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
