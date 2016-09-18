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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.junang.openview.R;
import com.junang.openview.animation.AnimationContract;
import com.junang.openview.animation.FloatingTextAnimation;
import com.junang.openview.animation.LoadingAnimation;
import com.junang.openview.util.ViewUtil;

import static android.R.attr.animation;
import static android.R.attr.logo;
import static android.R.attr.value;
import static android.R.transition.move;

/**
 * Created by junius.ang on 9/17/2016.
 */

public class CoreSpinnerWidget extends AppCompatSpinner {
    Paint mPaint;
    FloatingTextAnimation floatingTextAnimation;
    //this will also set the hint to be the first item in adapter,
    private boolean isAnimatingHint;
    String hintText;
    Float size = 12f;
    float left;

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
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position > 0) {
                    moveToFloatingHint();
                }
                else{
                    moveToInlineHint();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }



    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        Log.d("setAdapter","aad");
        if(adapter != null && adapter.getCount() != 0){
            moveToFloatingHint();
        }
        super.setAdapter(adapter);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("height", (b - t )+"");
    }

    public void init(){
        setPadding(0, (int)ViewUtil.dp_px(5), 0, 0);
        left = ViewUtil.dp_px(8);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12,
                Resources.getSystem().getDisplayMetrics()));
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        floatingTextAnimation = new FloatingTextAnimation(this);
        Log.d("heightNeeded", "heigh");
        floatingTextAnimation.setAnimationContract(new AnimationContract() {
            @Override
            public void onUpdate() {

            }
        });
//        startAnimation(floatingTextAnimation);


    }

    public void moveToFloatingHint(){
        float start = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14f,
                Resources.getSystem().getDisplayMetrics());
        float end = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                Resources.getSystem().getDisplayMetrics());
        ValueAnimator animation = ValueAnimator.ofFloat(start, end);
        animation.setInterpolator(LoadingAnimation.LOADING_INTERPOLATOR);
        animation.setDuration(500);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                mPaint.setTextSize(value);
                invalidate();
            }
        });
        ValueAnimator move = ValueAnimator.ofFloat(getMeasuredHeight() - 42, 40);
        move.setDuration(500);
        move.setInterpolator(new AccelerateInterpolator());
        Log.d("heightPost", getMeasuredHeight()+"");
        move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottom = ((Float) (animation.getAnimatedValue())).floatValue();
            }
        });
        move.start();
    }

    public void moveToInlineHint(){
        float start = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12f,
                Resources.getSystem().getDisplayMetrics());
        float end = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                14f,
                Resources.getSystem().getDisplayMetrics());
        ValueAnimator animation = ValueAnimator.ofFloat(start, end);
        animation.setInterpolator(LoadingAnimation.LOADING_INTERPOLATOR);
        animation.setDuration(500);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                mPaint.setTextSize(value);
                invalidate();
            }
        });
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
    float bottom = 40;

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(hintText != null) {
            canvas.drawText(hintText, 8, bottom, mPaint);
        }
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }
}
