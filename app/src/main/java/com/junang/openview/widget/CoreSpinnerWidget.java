package com.junang.openview.widget;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.junang.openview.R;
import com.junang.openview.animation.LoadingAnimation;
import com.junang.openview.util.ViewUtil;

import java.util.ArrayList;

import static android.R.attr.animation;
import static android.R.attr.right;

/**
 * Created by junius.ang on 9/17/2016.
 */

public class CoreSpinnerWidget extends AppCompatSpinner {
    private static final int DURATION = 250;
    private static final int NORMAL = 0;
    private static final int HINT = 1;
    private static final int ERROR = 2;

    protected Paint mHintTextPaint;
    protected Paint mLinePaint;

    //CONDITIONAL STATE
    //this will also set the hint to be the first item in adapter,
    protected boolean isAnimatingHint;
    protected boolean isFloating;
    protected boolean isShowError;

    protected String hintText;
    protected String errorText;

    //calculate everything from top, don't rely on measured height
    protected Drawable triangleDrawable;
    protected Bitmap bitmap;
    protected float floatingBottom;
    protected float inlineBottom;
    protected float left;
    protected float right;
    protected float height;
    protected float sizeFloating;
    protected float sizeInline;

    protected int errorColor;
    protected int hintColor;
    protected int normalColor;

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
        initListener();
    }

    /**
     *
     * @param list
     * @param addHintAsFirstItem endorsed to be true, because there is an impl for this
     */
    public void setItems(ArrayList<String> list, boolean addHintAsFirstItem){
        if(addHintAsFirstItem) {
            list.add(0, getHintText());
        }
        ArrayAdapter<String> adapter = getArrayAdapter(list);
        //this method will set separate spinner textview, so we can modify spinner text
        adapter.setDropDownViewResource(R.layout.widget_dropdown_spinner_text);
        setAdapter(adapter);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if(adapter != null && adapter.getCount() != 0){
            animateHint(true);
        }
        super.setAdapter(adapter);

    }

    public void init(){
        setPadding(0, (int)ViewUtil.dp_px(8), 0, 0);
        errorColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        hintColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        sizeInline = 14f;
        sizeFloating = 12f;
        height = ViewUtil.dp_px(40);
        floatingBottom = ViewUtil.dp_px(16);
        triangleDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_drop_down_black_24dp);
        right = ViewUtil.dp_px(30);
        left = ViewUtil.dp_px(8);
        mHintTextPaint = new Paint();
        mHintTextPaint.setAntiAlias(true);
        paintLine.setStrokeWidth(ViewUtil.dp_px(3));
        paintLine.setColor(Color.RED);
        mHintTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                12,
                Resources.getSystem().getDisplayMetrics()));
        mHintTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    public void initListener(){
        setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(isAnimatingHint) {
                    isShowError=false;
                    paintLine.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    if (position == 0) {
                        getSpinnerTextView().setVisibility(INVISIBLE);
                        animateHint(false);
                    } else if(position == 2){
                        isShowError=true;
                        paintLine.setColor(Color.RED);
                        getSpinnerTextView().setTextAppearance(getContext(), R.style.BaseText_Error);
//                        tv.setDuplicateParentStateEnabled(true);
                    } else if (position > 0 && !isFloating) {
                        animateHint(true);

                    }
                }
                else{
                    if (position == 0) {
                        getSpinnerTextView().setVisibility(INVISIBLE);
                        moveToInlineWithoutAnimation();
                    }
                    else{
                        moveToFloatingWithoutAnimation();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing for now
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN && !isFloating) {
            animateHint(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void setSpinnerTextStyle(int type){
        if(type == HINT){

        }
        else if(type == ERROR){

        }
        else{

        }
    }

    /**
     * Override this method to create your own impl
     * @param items
     * @return
     */
    protected ArrayAdapter<String> getArrayAdapter(ArrayList<String> items){
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getContext(), R.layout.widget_spinner_text, items){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLUE);
                    if(tv.getLayoutParams() != null) {
                        tv.getLayoutParams().height = (int) ViewUtil.dp_px(30);
                    }
                }
                else {
                    tv.setTextColor(Color.BLACK);
                    if(tv.getLayoutParams() != null) {
                        tv.getLayoutParams().height = (int) height;
                    }
                }
                return view;
            }
        };
        return arrayAdapter;
    }

    public void animateHint(boolean floating){
        float start,end, bottomOld, bottomNew;
        start = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, floating ? sizeFloating : sizeInline, Resources.getSystem().getDisplayMetrics());
        end = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, floating? sizeInline : sizeFloating, Resources.getSystem().getDisplayMetrics());
        bottomOld = floating? getMeasuredHeight() - floatingBottom : floatingBottom;
        bottomNew = floating? floatingBottom : getMeasuredHeight() - floatingBottom;
        ValueAnimator animation = ValueAnimator.ofFloat(start, end);
        animation.setInterpolator(LoadingAnimation.LOADING_INTERPOLATOR);
        animation.setDuration(DURATION);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) (animation.getAnimatedValue())).floatValue();
                mHintTextPaint.setTextSize(value);
                invalidate();
            }
        });
        ValueAnimator move = ValueAnimator.ofFloat(bottomOld, bottomNew);
        move.setDuration(DURATION);
        move.setInterpolator(new AccelerateInterpolator());
        move.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottom = ((Float) (animation.getAnimatedValue())).floatValue();
            }
        });
        isFloating = floating;
        animation.start();
        move.start();
    }

    public void moveToInlineWithoutAnimation(){
        bottom = getMeasuredHeight() - floatingBottom;
        invalidate();
    }

    public void moveToFloatingWithoutAnimation(){
        bottom = floatingBottom;
        invalidate();
    }

    float bottom = 40;

    Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(hintText != null) {
            canvas.drawText(hintText, 8, bottom, mHintTextPaint);
        }
        if(triangleDrawable != null) {
            if(bitmap == null){
                bitmap = drawableToBitmap(triangleDrawable);
            }
            canvas.drawBitmap(bitmap, getMeasuredWidth() - right, getMeasuredHeight() / 2 - ViewUtil.dp_px(12), mHintTextPaint);
        }
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), paintLine);
        super.dispatchDraw(canvas);
    }

    public Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color triangleDrawable will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        if(Build.VERSION.SDK_INT >= 21) {
            drawable.setTint(ContextCompat.getColor(getContext(), isShowError ? R.color.text_dark : R.color.colorAccent));
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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

    public void setAnimatingHint(boolean animatingHint) {
        isAnimatingHint = animatingHint;
    }

    public void setShowError(boolean showError) {
        isShowError = showError;
    }

    public TextView getSpinnerTextView(){
        return (TextView) findViewById(R.id.spinner_text);
    }

    public void setSizeInline(float sizeInline) {
        this.sizeInline = sizeInline;
    }

    public void setSizeFloating(float sizeFloating) {
        this.sizeFloating = sizeFloating;
    }
}
