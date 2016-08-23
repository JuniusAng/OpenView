package com.junang.openview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by junius.ang on 8/23/16.
 */
public class PullToFlipView extends ViewGroup {
    //Attribute & constant
    private static final float PULL_RATE = 0.5f;
    private static final float DECELERATE_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final int PULL_DIRECTION_LEFT = 0;
    private static final int PULL_DIRECTION_UP = 1;
    private static final int PULL_DIRECTION_RIGHT = 2;
    private static final int PULL_DIRECTION_DOWN = 3;

    protected int PULL_MAX_DISTANCE = 200;
    protected int ANIMATION_OFFSET_DURATION = 350;


    public PullToFlipView(Context context) {
        super(context);
    }

    public PullToFlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToFlipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
