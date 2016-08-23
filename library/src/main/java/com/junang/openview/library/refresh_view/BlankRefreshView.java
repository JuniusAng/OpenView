package com.junang.openview.library.refresh_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.view.animation.Animation;

import com.junang.openview.library.PullToRefreshView;

/**
 * Created by Oleksii Shliama on 22/12/2014.
 * https://dribbble.com/shots/1650317-Pull-to-Refresh-Rentals
 */
public class BlankRefreshView extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private PullToRefreshView mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private boolean isRefreshing = false;

    public BlankRefreshView(Context context, final PullToRefreshView parent) {
        super(context, parent);
        mParent = parent;
        mMatrix = new Matrix();

        setupAnimations();
        parent.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(parent.getWidth());
            }
        });
    }

    public void initiateDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        mScreenWidth = viewWidth;
//        if(mParent.getDirection() ==PullToRefreshView.PULL_DIRECTION_DOWN){
//
//        }
//        else{
//            mSkyTopOffset = (mSkyHeight * 0.38f);
//            mSunTopOffset = getBottomScreenTop() + (mParent.getTotalDragDistance() * 0.1f);
//            mTownInitialTopOffset = getBottomScreenTop() + (mParent.getTotalDragDistance() - mTownHeight * TOWN_INITIAL_SCALE);
//            mTownFinalTopOffset = getBottomScreenTop() + (mParent.getTotalDragDistance() - mTownHeight * TOWN_FINAL_SCALE);
//        }

        mTop = -mParent.getTotalDragDistance();
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;
        final int saveCount = canvas.save();

        canvas.restoreToCount(saveCount);
    }

    public void setPercent(float percent) {

    }

    public void setRotate(float rotate) {

    }

    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    private void setupAnimations() {
    }
}