package se.acoder.autohub.dashboard.WeatherInfo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.webkit.WebView;

import se.acoder.autohub.R;
import se.acoder.autohub.dashboard.DrawView;

/**
 * Created by Johannes Westlund on 2017-03-27.
 */

public class WeatherInfoView extends DrawView {
    private String temp = "-";
    private String tempUnit = "°C";
    private String location = "";
    private Drawable icon;

    private Drawable wIce, wWind, wVisibility;

    private int textBaseLine;

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
        icon = ContextCompat.getDrawable(getContext(),android.R.drawable.ic_menu_gallery);

        wIce = ContextCompat.getDrawable(context, R.drawable.ic_weather_cold);
        setColorForAlerts(wIce,false);
        wWind = ContextCompat.getDrawable(context, R.drawable.ic_weather_flag);
        setColorForAlerts(wWind,false);
        wVisibility = ContextCompat.getDrawable(context, R.drawable.ic_weather_visibility);
        setColorForAlerts(wVisibility,false);
    }

    public void setTemp(String temp){
        this.temp = temp;
        invalidate();
    }
    public void setLocation(String location){
        this.location = location;
        invalidate();
    }
    public void setIcon(Drawable icon){
        this.icon = icon;
        invalidate();
    }

    public void toggleAlert(Alerts alert, boolean toggle){
        switch (alert){
            case Cold:
                setColorForAlerts(wIce, toggle);
                break;
            case Wind:
                setColorForAlerts(wWind, toggle);
                break;
            case Visibility:
                setColorForAlerts(wVisibility, toggle);
                break;
        }
        invalidate();
    }

    private void setColorForAlerts(Drawable icon, boolean toggle){
        int on = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        int off = ContextCompat.getColor(getContext(), R.color.colorDashInscript);

        if(toggle)
            DrawableCompat.setTint(icon,on);
        else
            DrawableCompat.setTint(icon,off);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float skewDelta = getWidth()*0.05f;

        drawDash(canvas, skewDelta);

        int size = (int)toPx(30, getResources());
        int margin = (int)toPx(15, getResources());
        wVisibility.setBounds(getWidth()-(int)skewDelta-size-margin, getHeight()/2-size/2, getWidth()-(int)skewDelta-margin, getHeight()/2+size/2);
        wVisibility.draw(canvas);
        wWind.setBounds(getWidth()-(int)skewDelta-2*size-2*margin, getHeight()/2-size/2, getWidth()-(int)skewDelta-size-2*margin, getHeight()/2+size/2);
        wWind.draw(canvas);
        wIce.setBounds(getWidth()-(int)skewDelta-3*size-3*margin, getHeight()/2-size/2, getWidth()-(int)skewDelta-2*size-3*margin, getHeight()/2+size/2);
        wIce.draw(canvas);


        drawWeatherWidget(canvas, skewDelta);
    }

    private void drawWeatherWidget(Canvas canvas, float skewDelta){
        int left = (int)skewDelta;;
        int size = (int)toPx(40, getResources());
        int margin = (int)toPx(6, getResources());
        icon.setBounds(left, 0, left+size, size);
        icon.draw(canvas);

        textPaint.setTextSize(toPx(22,getResources()));
        canvas.drawText(temp+" "+tempUnit, skewDelta+size+margin, margin+toPx(20,getResources()), textPaint);
        textPaint.setTextSize(toPx(16,getResources()));
        canvas.drawText(location, skewDelta+margin, getHeight()/2+toPx(20, getResources()), textPaint);
    }

    private void drawDash(Canvas canvas, float skewDelta){
        Resources r = getResources();
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

    public enum Alerts {
        Cold, Wind, Visibility
    }
}
