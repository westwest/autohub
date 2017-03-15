package se.acoder.autohub.frontend.CustomViews.DayInfoView;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by johves on 2017-03-15.
 */

public class Grid extends View {
    private int height = 110;
    private Paint linePaint;

    public Grid(Context context) {
        super(context);
        init();
    }

    public Grid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public Grid(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(parentWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0,50,100,height,linePaint);
        canvas.drawLine(100,height,getWidth()-100, height, linePaint);
        canvas.drawLine(getWidth()-100, height, getWidth(), 50, linePaint);
    }
}
