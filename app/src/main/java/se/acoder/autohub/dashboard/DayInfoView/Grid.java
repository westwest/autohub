package se.acoder.autohub.dashboard.DayInfoView;

import android.annotation.TargetApi;
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
 * Created by johves on 2017-03-15.
 */

public class Grid extends DrawView {
    private int height = 50;
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
        linePaint.setStrokeWidth(6);
        linePaint.setStyle(Paint.Style.STROKE);

        bgPaint = new Paint();
        bgPaint.setColor(ContextCompat.getColor(context, android.R.color.background_dark));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(parentWidth, (int) toPx(height+2, getResources()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources r = getResources();

        Path dash = new Path();
        dash.moveTo(toPx(-10, r),toPx(-10, r));
        dash.lineTo(toPx(-10, r),toPx(height/2, r));
        dash.lineTo(toPx(0, r), toPx(height/2, r));
        dash.lineTo(toPx(40, r), toPx(height, r));
        dash.lineTo(getWidth()-toPx(40,r), toPx(height, r));
        dash.lineTo(getWidth(), toPx(height/2, r));
        dash.lineTo(getWidth()+ toPx(10, r), toPx(height/2, r));
        dash.lineTo(getWidth()+ toPx(10, r), toPx(-10, r));
        dash.close();

        canvas.drawPath(dash, bgPaint);
        canvas.drawPath(dash, linePaint);

    }
}
