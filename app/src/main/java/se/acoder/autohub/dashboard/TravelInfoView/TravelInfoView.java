package se.acoder.autohub.dashboard.TravelInfoView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import se.acoder.autohub.R;
import se.acoder.autohub.dashboard.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-14.
 */
public class TravelInfoView extends DrawView {
    private String distance;
    private String time;

    private final int textSize = 20;
    private int textHeight;

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
        update(0.0f, 0l);
        textPaint.setTextSize(toPx(textSize,getResources()));

        Rect bounds = new Rect();
        String dimText = "000";
        textPaint.getTextBounds(dimText,0,dimText.length(),bounds);
        textHeight = bounds.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0,getWidth(),getHeight(), bgPaint);
        canvas.drawLine(0,0,getWidth(),0, linePaint);

        canvas.drawText(distance, 56,  getHeight()/2+textHeight/2, textPaint);
        canvas.drawText(time, 300,  getHeight()/2+textHeight/2, textPaint);
    }

    public void update(Float distance, Long time){
        if(distance != null)
            this.distance = formatDistance(distance);
        if(time != null){
            this.time = formatTime(time);
        }
        invalidate();
    }

    private String formatDistance(float distance){
        return (distance/1000+"").substring(0,3) + " km";
    }

    private String formatTime(long time){
        float tInMin = time/(60*1000);
        int fullHours = (int) tInMin/60;
        int fullMin = (int) tInMin - fullHours*60;
        return fullHours + "h " + fullMin + "min";
    }
}