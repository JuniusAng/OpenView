package com.junang.openview.animation;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.junang.openview.util.ViewUtil;

/**
 * Created by junius-ang on 7/5/15.
 */
public class FloatingTextAnimation extends Animation {
    public static final int LOADING_DURATION = 1000;
    public static final Interpolator LOADING_INTERPOLATOR = PathInterpolatorCompat.create(0.455f, 0.03f, 0.515f, 0.955f);
    public static final Interpolator TEXT_INTERPOLATOR = new AccelerateInterpolator();
    private AnimationContract animationContract;
    private boolean isEndPointFloating;
    private boolean isReverse;
    private View v;
    float floatingLeft = 10;
    float floatingBottom = 40;
    float inlineLeft = 20;
    public float inlineBottom;
    float currentBottom;
    float currentTextSize;
    float currentLeft;
    float referenceTextSize;

    public FloatingTextAnimation() {
        super();
        referenceTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                Resources.getSystem().getDisplayMetrics());
        setRepeatMode(REVERSE);
        setRepeatCount(INFINITE);
        setDuration(LOADING_DURATION);
        setInterpolator(new AccelerateDecelerateInterpolator());
        isEndPointFloating = false;
        isReverse = false;
        setAnimationListener(selfListener);
    }

    public FloatingTextAnimation(View v){
        this();
        this.v = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if(isReverse) {
            //going from inline to floating
            currentTextSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    14 - (2 * interpolatedTime),
                    Resources.getSystem().getDisplayMetrics());
            //14 --> 12    1-->0
            currentBottom =  inlineBottom - ((inlineBottom - floatingBottom) * interpolatedTime);
            currentLeft =  inlineLeft - ((inlineLeft - floatingLeft) * interpolatedTime);
        }
        else{
            //12 --> 14
            currentTextSize = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    12 + (2 * interpolatedTime),
                    Resources.getSystem().getDisplayMetrics());
            currentBottom =  floatingBottom + ((inlineBottom - floatingBottom) * interpolatedTime);
            currentLeft =  floatingLeft + ((inlineLeft - floatingLeft) * interpolatedTime);
        }
        v.invalidate();
    }

    public void setAnimationContract(AnimationContract animationContract) {
        this.animationContract = animationContract;
    }

    private AnimationListener selfListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            if(inlineBottom == 0){
                inlineBottom = v.getMeasuredHeight() - 45;
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

    public void drawText(Canvas canvas, float left, float bottom, Paint paint){
        paint.setTextSize(currentTextSize);
        canvas.drawText("Android", currentLeft, currentBottom,paint);
    }
}
