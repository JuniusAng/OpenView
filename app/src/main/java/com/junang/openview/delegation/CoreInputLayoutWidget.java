package com.junang.openview.delegation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.junang.openview.R;
import com.junang.openview.util.ViewUtil;

/**
 * Created by junius.ang on 9/9/2016.
 */
public class CoreInputLayoutWidget extends TextInputLayout {
    protected Context mContext;
    protected Paint mPaint;
    protected EditText vEditText;
    protected boolean isHintFloating = true;
    protected boolean isErrorBottom = true;

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
        if(!isErrorBottom){
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
                if (!isHintFloating) {
                    vEditText.setHint(hint);
                } else {
                    setHintEnabled(hint != null);
                    super.setHint(hint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CoreInputLayoutWidget setIsHintFloating(boolean isShowfloating){
        isHintFloating = isShowfloating;
        return this;
    }

    public CoreInputLayoutWidget setIsErrorBottom(boolean isErrorBottom){
        this.isErrorBottom = isErrorBottom;
        return this;
    }

    protected void init(AttributeSet attrs, int defStyleAttrs){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        vEditText = new AppCompatEditText(mContext, attrs);

        vEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        vEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Some logic here.
                    return true; // Focus will do whatever you put in the logic.
                }
                return false;  // Focus will change according to the actionId
            }
        });
        vEditText.setMaxLines(1);
        vEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        setHintTextAppearance(R.style.CoreText_Floating);
//        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle_black_24dp);
//        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//        vEditText.setError("testing", d);
        this.addView(vEditText);

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

    protected void setListener(){

    }

    public EditText getEditText(){
        return vEditText;
    }
}
