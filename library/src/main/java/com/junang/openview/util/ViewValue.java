package com.junang.openview.util;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by junius-ang on 7/18/15.
 */
public final class ViewValue {
    //Lazy instantiation
    private static class ViewValueHolder {
        public static ViewValue value = new ViewValue();
    }
    public static ViewValue get(){
        return ViewValueHolder.value;
    }

    //hold primitive typed value here for easy access and increasing performance by reducing any repeating calculation
    private boolean isGenerated = false;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private float screenDensity = 0;
    private float animationScale = 1f;
    private int isDontKeepActivitiesOn;

    public void init(Context mContext){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        screenDensity = mContext.getResources().getDisplayMetrics().density;

        try {
            //this will check animator setting, use this to disable animation if animation is weird as user set this value from
            //developer options to 0
            if(Build.VERSION.SDK_INT >= 17) {
                animationScale = Settings.Global.getFloat(mContext.getContentResolver(),
                        Settings.Global.ANIMATOR_DURATION_SCALE, 1);
                isDontKeepActivitiesOn = Settings.System.getInt(mContext.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
            }
            else{
                animationScale = Settings.System.getFloat(mContext.getContentResolver(), "animator_duration_scale", 1f);
                isDontKeepActivitiesOn = Settings.System.getInt(mContext.getContentResolver(), "always_finish_activities", 0);
            }
        } catch (Exception e) {
            animationScale = 1f;
            e.printStackTrace();
        }
        isGenerated = true;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getScreenDensity() {
        return screenDensity;
    }

    public void setScreenDensity(float screenDensity) {
        this.screenDensity = screenDensity;
    }

    public boolean getIsDontKeepActivitiesOn() {
        return isDontKeepActivitiesOn == 0 ? false : true;
    }
}
