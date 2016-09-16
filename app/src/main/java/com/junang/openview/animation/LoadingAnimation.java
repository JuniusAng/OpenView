package com.junang.openview.animation;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.junang.openview.util.ViewUtil;

/**
 * Created by junius-ang on 7/5/15.
 */
public class LoadingAnimation extends Animation {
    public static final int LOADING_DURATION = 1000;
    public static final Interpolator LOADING_INTERPOLATOR = PathInterpolatorCompat.create(0.455f, 0.03f, 0.515f, 0.955f);
    private LoadingContract loadingContract;
    private boolean isReverse;
    public float interpolated, interpolated1, interpolated2;
    private int radius, centerX, centerY;
    private View v;

    public interface LoadingContract  {
        void onUpdate();
    }

    public LoadingAnimation() {
        super();
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        setDuration(LOADING_DURATION);
        setInterpolator(LOADING_INTERPOLATOR);
        isReverse = false;
        setAnimationListener(selfListener);
    }

    public LoadingAnimation(int centerX, int centerY){
        this();
        this.radius = (int)ViewUtil.dp_px(5);
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public LoadingAnimation(View v){
        this();
        this.v = v;
        this.radius = (int)ViewUtil.dp_px(5);
        this.centerX = v.getWidth() / 2;
        this.centerY = v.getHeight() / 2;
    }

    public LoadingAnimation(int radius, View v, int centerX, int centerY){
        this();
        this.v = v;
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if(isReverse) {
            if (interpolatedTime >= 0.6f) {
                interpolated = (interpolatedTime - 0.6f) / 0.4f;
            } else if (interpolatedTime < 0.7f && interpolatedTime >= 0.3f) {
                interpolated1 = (interpolatedTime - 0.3f) / 0.4f;
            } else if (interpolatedTime < 0.4f && interpolatedTime >= 0f) {
                interpolated2 = interpolatedTime / 0.4f;
            }
        }
        else{
            if (interpolatedTime <= 0.4f) {
                interpolated = interpolatedTime / 0.4f;
            }
            if (interpolatedTime > 0.3f && interpolatedTime <= 0.7f) {
                interpolated1 = (interpolatedTime - 0.3f) / 0.4f;
            }
            if (interpolatedTime > 0.6f && interpolatedTime <= 1f) {
                interpolated2 = (interpolatedTime - 0.6f) / 0.4f;
            }

        }
        v.invalidate();
    }

    public void setLoadingContract(LoadingContract loadingContract) {
        this.loadingContract = loadingContract;
    }

    private AnimationListener selfListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if(radius == 0){
                throw new IllegalStateException("Radius is not set");
            }
            if(v == null){
                throw new NullPointerException("View is not set");
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            isReverse = !isReverse;
        }
    };

    public void drawLoading(Canvas canvas, Paint paint){
        if(centerX <=0 || centerY <= 0){
            centerX = v.getMeasuredWidth() / 2;
            centerY = v.getMeasuredHeight() / 2;
        }
        float doubleRadius = (radius * 2f);
        int padding = (int) ViewUtil.dp_px(4);
        canvas.drawCircle(centerX - doubleRadius - padding, centerY, radius * interpolated, paint);
        canvas.drawCircle(centerX, centerY, radius * interpolated1, paint);
        canvas.drawCircle(centerX + doubleRadius + padding, centerY, radius * interpolated2, paint);
    }

    public void setCircleRadius(int radius) {
        this.radius = radius;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
}
