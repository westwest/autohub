package se.acoder.autohub.frontend.CustomViews.DayInfoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import se.acoder.autohub.R;

/**
 * Created by johves on 2017-03-15.
 */

public class Grid extends View {
    private int height = 110;
    private Paint linePaint;
    private Paint bgPaint;

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
        setMeasuredDimension(parentWidth, height+2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path dash = new Path();
        dash.moveTo(-10,-10);
        dash.lineTo(-10,height/2);
        dash.lineTo(0, height/2);
        dash.lineTo(100, height);
        dash.lineTo(getWidth()-100, height);
        dash.lineTo(getWidth(), height/2);
        dash.lineTo(getWidth()+10, height/2);
        dash.lineTo(getWidth()+10, -10);
        dash.close();

        canvas.drawPath(dash, bgPaint);
        canvas.drawPath(dash, linePaint);

    }
}
