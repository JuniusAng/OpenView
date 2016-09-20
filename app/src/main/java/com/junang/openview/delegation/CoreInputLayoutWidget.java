package com.junang.openview.delegation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.junang.openview.R;

import static android.R.attr.max;
import static android.R.attr.maxLines;

/**
 * Created by junius.ang on 9/9/2016.
 */
public class CoreInputLayoutWidget extends TextInputLayout {
    protected Context mContext;
    protected Paint mPaint;
    protected AttributeSet mAttrs;
    protected TextInputEditText vEditText;
    protected boolean isInlineHint = false;
    protected boolean isBottomError = false;
    protected boolean isUsingCustomEditText;
    protected boolean isAnimatingTransition;
    protected int maxChar;
    protected int maxLine;

    public CoreInputLayoutWidget(Context context) {
        super(context);
        this.mContext = context;
    }

    public CoreInputLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
        setListener();

    }

    public CoreInputLayoutWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, 0);
        setListener();
    }

    @Override
    public void setError(CharSequence error){
        if(!isBottomError){
            vEditText.setError(error);
        }
        else{
            setErrorEnabled(error != null);
            super.setError(error);
        }
    }

    @Override
    public void setHint(CharSequence hint){
        try {
            if(getEditText() != null) {
                if (!isInlineHint) {
                    vEditText.setHint(hint);
                } else {
                    setHintEnabled(hint != null);
                    super.setHint(hint);
                }
            }
        } catch (Exception ignored) {}
    }

    public CoreInputLayoutWidget setIsHintFloating(boolean isShowfloating){
        isInlineHint = isShowfloating;
        return this;
    }

    public CoreInputLayoutWidget setIsErrorBottom(boolean isBottomError){
        this.isBottomError = isBottomError;
        return this;
    }

    protected void init(AttributeSet attrs, int defStyleAttrs){
        this.mAttrs = attrs;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        addDefaultEditText();

        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BaseInputLayoutWidget, defStyleAttrs, 0);
        for (int i=0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.BaseInputLayoutWidget_isAnimatingTransition) {
                isAnimatingTransition = (a.getBoolean(R.styleable.BaseInputLayoutWidget_isAnimatingTransition, false));
            } else if (attr == R.styleable.BaseInputLayoutWidget_isInlineHint) {
                isInlineHint = (a.getBoolean(R.styleable.BaseInputLayoutWidget_isInlineHint, false));
            } else if (attr == R.styleable.BaseInputLayoutWidget_isBottomError) {
                isBottomError = (a.getBoolean(R.styleable.BaseInputLayoutWidget_isBottomError, false));
            } else if (attr == R.styleable.BaseInputLayoutWidget_isUsingCustomEditText) {
                isUsingCustomEditText = (a.getBoolean(R.styleable.BaseInputLayoutWidget_isUsingCustomEditText, false));
            } else if (attr == R.styleable.BaseInputLayoutWidget_maxChar) {
                maxChar = (a.getInteger(R.styleable.BaseInputLayoutWidget_maxChar, -1));
            } else if(attr == R.styleable.BaseInputLayoutWidget_maxLines) {
                maxLine = (a.getInteger(R.styleable.BaseInputLayoutWidget_maxLines, -1));
            }
        }
        a.recycle();
        if(!isUsingCustomEditText){
            addDefaultEditText();
        }
        if(maxChar > 0){
            setCounterEnabled(true);
            setCounterMaxLength(maxChar);
        }
        if(maxLine > 0){
            vEditText.setMaxLines(maxLines);
        }
//        vEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        vEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
//                if (actionId == EditorInfo.IME_ACTION_NEXT) {
//                    // Some logic here.
//                    return true; // Focus will do whatever you put in the logic.
//                }
//                return false;  // Focus will change according to the actionId
//            }
//        });
//        vEditText.setMaxLines(1);
//        vEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
//        setHintTextAppearance(R.style.CoreText_Floating);
//        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle_black_24dp);
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        vEditText.setError("testing", d);


//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.CoreInputLayoutWidget, defStyleAttrs, 0);
//        for (int i=0; i < a.getIndexCount(); i++) {
//            int attr = a.getIndex(i);
//            if(attr == R.styleable.CoreInputLayoutWidget_bottomErrorTextApperance){
//
//            }
//            else if(attr == R.styleable.CoreButtonWidget_coreButtonDrawableStart){
//                setCompoundDrawablesWithIntrinsicBounds(a.getDrawable(R.styleable.CoreButtonWidget_coreButtonDrawableStart), null, null, null);
//            }
//            else if(attr == R.styleable.CoreButtonWidget_coreButtonDrawableEnd){
//                setCompoundDrawablesWithIntrinsicBounds(null, null, a.getDrawable(R.styleable.CoreButtonWidget_coreButtonDrawableEnd), null);
//            }
    }

    /**
     * call this except you want to use custom editText and this inputLayout act as common TextInputLayout
     */
    protected void addDefaultEditText(){
        vEditText = new TextInputEditText(mContext, mAttrs);
        this.addView(vEditText);
        isUsingCustomEditText = true;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if(isUsingCustomEditText){
        }
    }

    protected void setListener(){

    }

    public EditText getEditText(){
        return vEditText;
    }
}
