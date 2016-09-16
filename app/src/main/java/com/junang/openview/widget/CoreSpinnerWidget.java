package com.junang.openview.widget;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.junang.openview.R;
import com.junang.openview.animation.AnimationContract;
import com.junang.openview.animation.FloatingTextAnimation;
import com.junang.openview.util.ViewUtil;

import static android.R.attr.animation;
import static android.R.attr.value;
import static android.R.transition.move;

/**
 * Created by junius.ang on 9/17/2016.
 */

public class CoreSpinnerWidget extends AppCompatSpinner {
    Paint mPaint;
    FloatingTextAnimation floatingTextAnimation;
    Float size = 12f;

    public CoreSpinnerWidget(Context context) {
        super(context);
        init();
    }

    public CoreSpinnerWidget(Context context, int mode) {
        super(context, mode);
        init();
    }

    public CoreSpinnerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if(adapter != null && adapter.getCount() != 0){

        }
        super.setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void init(){
        setPadding(0, (int)ViewUtil.dp_px(5), 0, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12,
                Resources.getSystem().getDisplayMetrics()));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        floatingTextAnimation = new FloatingTextAnimation(this);
        floatingTextAnimation.setAnimationContract(new AnimationContract() {
            @Override
            public void onUpdate() {

            }
        });
//        startAnimation(floatingTextAnimation);


    }

    protected void moveToFloatingHint(){
        float start = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                Resources.getSystem().getDisplayMetrics());
        float end = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14f,
                Resources.getSystem().getDisplayMetrics());
        ValueAnimator animation = ValueAnimator.ofFloat(start, end);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(500);
        animation.setStartDelay(2000);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                mPaint.setTextSize(value);
                invalidate();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator move = ValueAnimator.ofFloat(40, getMeasuredHeight() - 42);
                move.setDuration(500);
                move.setInterpolator(new AccelerateInterpolator());
                move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bottom = ((Float) (animation.getAnimatedValue())).floatValue();
                    }
                });
                move.start();
            }
        }, 2000);
    }

    protected void moveToInlineHint(){
        float start = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                Resources.getSystem().getDisplayMetrics());
        float end = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14f,
                Resources.getSystem().getDisplayMetrics());
        ValueAnimator animation = ValueAnimator.ofFloat(start, end);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(500);
        animation.setStartDelay(2000);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                mPaint.setTextSize(value);
                invalidate();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ValueAnimator move = ValueAnimator.ofFloat(40, getMeasuredHeight() - 42);
                move.setDuration(500);
                move.setInterpolator(new AccelerateInterpolator());
                move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        bottom = ((Float) (animation.getAnimatedValue())).floatValue();
                    }
                });
                move.start();
            }
        }, 2000);
    }
    float bottom = 40;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawText("Android", 10, bottom, mPaint);
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
