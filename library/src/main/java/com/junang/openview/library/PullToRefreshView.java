package com.junang.openview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.junang.openview.library.refresh_view.BaseRefreshView;
import com.junang.openview.library.refresh_view.BlankRefreshView;
import com.junang.openview.util.ViewUtil;

import java.security.InvalidParameterException;

public class PullToRefreshView extends ViewGroup {

    private int DRAG_MAX_DISTANCE = 120;
    private static final float DRAG_RATE = .5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    public static final int PULL_DIRECTION_DOWN = 0;
    public static final int PULL_DIRECTION_UP = 1;

    public static final int STYLE_BLANK = 1;
    public int MAX_OFFSET_ANIMATION_DURATION = 350;

    private static final int INVALID_POINTER = -1;

    private PullToRefreshListener mPullToRefreshListener;

    private View mTarget;
    private ImageView mRefreshView;
    private Interpolator mDecelerateInterpolator;
    private int mTouchSlop;
    private int mTotalDragDistance;
    private BaseRefreshView mBaseRefreshView;
    private float mCurrentDragPercent;
    private int mCurrentOffsetTop;
    private boolean mRefreshing;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private int mFrom;
    private float mFromDragPercent;
    private boolean mNotify;
    private OnRefreshListener mListener;

    private int mTargetPaddingTop;
    private int mTargetPaddingBottom;
    private int mTargetPaddingRight;
    private int mTargetPaddingLeft;

    private int direction;
    private boolean isInteractionDisabled = false;

    public PullToRefreshView(Context context) {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshView);
        final int type = a.getInteger(R.styleable.PullToRefreshView_type, STYLE_BLANK);
        direction = a.getInteger(R.styleable.PullToRefreshView_pull_direction, 0);
        a.recycle();

        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTotalDragDistance = (int) ViewUtil.dp_px(DRAG_MAX_DISTANCE);

        mRefreshView = new ImageView(context);
        if (direction == PULL_DIRECTION_UP) {
            MAX_OFFSET_ANIMATION_DURATION = 350;
        }
        setRefreshStyle(type);

        addView(mRefreshView);

        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    public void setRefreshStyle(int type) {
        setRefreshing(false);
        switch (type) {
            case STYLE_BLANK:
                mBaseRefreshView = new BlankRefreshView(getContext(), this);
                break;
            default:
                throw new InvalidParameterException("Type does not exist");
        }
        mRefreshView.setImageDrawable(mBaseRefreshView);
    }

    /**
     * This method sets padding for the refresh (progress) view.
     */
    public void setRefreshViewPadding(int left, int top, int right, int bottom) {
        mRefreshView.setPadding(left, top, right, bottom);
    }

    public int getTotalDragDistance() {
        return mTotalDragDistance;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
                    mTargetPaddingBottom = mTarget.getPaddingBottom();
                    mTargetPaddingLeft = mTarget.getPaddingLeft();
                    mTargetPaddingRight = mTarget.getPaddingRight();
                    mTargetPaddingTop = mTarget.getPaddingTop();
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled() || canChildScrollUp() || mRefreshing) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTop(0, true);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = direction == PULL_DIRECTION_DOWN ? y - mInitialMotionY : mInitialMotionY - y;
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {

        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = direction == PULL_DIRECTION_DOWN ? y - mInitialMotionY : mInitialMotionY - y;
                final float scrollTop = yDiff * DRAG_RATE;
                mCurrentDragPercent = scrollTop / mTotalDragDistance;
                if (mCurrentDragPercent < 0) {
                    return false;
                }
                float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                float slingshotDist = mTotalDragDistance;
                float tensionSlingshotPercent = Math.max(0,
                        Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float extraMove = (slingshotDist) * tensionPercent / 2;
                int targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);

                mBaseRefreshView.setPercent(mCurrentDragPercent, true);
                if (direction == PULL_DIRECTION_DOWN) {
                    setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                } else {
                    setTargetOffsetTop(targetY + mCurrentOffsetTop, true);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:

                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (direction == PULL_DIRECTION_DOWN ? y - mInitialMotionY : mInitialMotionY - y) * DRAG_RATE;
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (overScrollTop >= mTotalDragDistance) {
                    mRefreshing = false;
                    mPullToRefreshListener.onMaxDistanceReached(mTarget.getTop());
                } else {
                    executePendingAnimation();
                }
                return false;
            }
        }

        return true;
    }

    public void animateOffsetToResetPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
//        setRefreshing(true, true);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateResetPosition);
    }

    public void executePendingAnimation() {
        mRefreshing = false;
        animateOffsetToStartPosition();
    }


    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        if (mPullToRefreshListener != null) {
            mPullToRefreshListener.onOffsetChange(offset);
        }

        mTarget.offsetTopAndBottom(direction == PULL_DIRECTION_DOWN ? offset : -offset);
        mBaseRefreshView.offsetTopAndBottom(direction == PULL_DIRECTION_DOWN ? offset : offset);
        mCurrentOffsetTop = mTarget.getTop();
    }

    private void animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;

        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);

        if (mRefreshing) {
            mBaseRefreshView.start();
            if (mNotify) {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
        } else {
            if (direction == PULL_DIRECTION_DOWN) {
                mBaseRefreshView.stop();
                animateOffsetToStartPosition();
            }
        }
        mCurrentOffsetTop = mTarget.getTop();

//        if(direction == PULL_DIRECTION_DOWN) {
//            mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTotalDragDistance);
//        }
//        else{
//            mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom);
//        }
    }

    private final Animation mAnimateResetPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {

            int targetTop = mFrom - (int) (mFrom * interpolatedTime) - (int) getTranslationY();
            float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
            int offset = direction == PULL_DIRECTION_DOWN ? targetTop - (mTarget.getTop()) : mTarget.getTop() - targetTop;
//            int offset = targetTop - mTarget.getTop();
            offset -= (int) getTranslationY();
            mCurrentDragPercent = targetPercent;
            mBaseRefreshView.setPercent(mCurrentDragPercent, true);
//            if(direction == PULL_DIRECTION_DOWN) {
//                mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom + targetTop);
//            }
//            else{
//                mTarget.setPadding(mTargetPaddingLeft, targetTop - mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom);
//            }
            setTargetOffsetTop(offset, false);
        }
    };

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {

            int targetTop = mFrom - (int) (mFrom * interpolatedTime);
            float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
            int offset = direction == PULL_DIRECTION_DOWN ? targetTop - mTarget.getTop() : mTarget.getTop() - targetTop;
//            int offset = targetTop - mTarget.getTop();
            mCurrentDragPercent = targetPercent;
            mBaseRefreshView.setPercent(mCurrentDragPercent, true);
//            if(direction == PULL_DIRECTION_DOWN) {
//                mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom + targetTop);
//            }
//            else{
//                mTarget.setPadding(mTargetPaddingLeft, targetTop - mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom);
//            }
            setTargetOffsetTop(offset, false);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = (direction == PULL_DIRECTION_DOWN) ? mTotalDragDistance : 0;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = direction == PULL_DIRECTION_DOWN ? targetTop - mTarget.getTop() : mTarget.getTop() - targetTop;
//            int offset = targetTop - mTarget.getTop();
            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;
            mBaseRefreshView.setPercent(mCurrentDragPercent, false);

            setTargetOffsetTop(offset, false /* requires update */);
        }
    };

    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mBaseRefreshView.setPercent(1f, true);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mBaseRefreshView.stop();
            mCurrentOffsetTop = mTarget.getTop();
        }
    };

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }


    private boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(mTarget, direction == PULL_DIRECTION_DOWN ? -1 : 1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();
        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
        mRefreshView.layout(left, top, left + width - right, top + height - bottom);

    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public static interface OnRefreshListener {
        public void onRefresh();
    }

    public int getDirection() {
        return direction;
    }

    public void setMaxDragDistance(int maxDragDistance) {
        this.mTotalDragDistance = this.DRAG_MAX_DISTANCE = maxDragDistance;
    }

    public void setPullToRefreshListener(PullToRefreshListener mPullToRefreshListener) {
        this.mPullToRefreshListener = mPullToRefreshListener;
    }

    public interface PullToRefreshListener {
        void onMaxDistanceReached(int distance);

        void onOffsetChange(int offset);
    }

    public boolean isInteractionDisabled() {
        return isInteractionDisabled;
    }

    public void setIsInteractionDisabled(boolean isInteractionDisabled) {
        this.isInteractionDisabled = isInteractionDisabled;
    }
}


