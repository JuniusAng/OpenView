package com.junang.openview.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.junang.openview.delegation.CoreInputLayoutWidget;

/**
 * Created by junius.ang on 9/20/2016.
 */

public class DefaultInputLayoutWidget extends CoreInputLayoutWidget {
    public DefaultInputLayoutWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultInputLayoutWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(AttributeSet attrs, int defStyleAttrs) {
        super.init(attrs, defStyleAttrs);

    }
}
