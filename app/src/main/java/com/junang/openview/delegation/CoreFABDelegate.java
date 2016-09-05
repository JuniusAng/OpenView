package com.junang.openview.delegation;

import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.junang.openview.R;

/**
 * Created by junius.ang on 8/23/2016.
 * must be used from V3 environment
 */
public class CoreFABDelegate{
    /**
     * This builder will create default implementation of FAB in bottom end gravity
     * @param layoutInflater
     * @return CoreFABDelegate
     */
    public static CoreFABDelegate createDefaultImpl(LayoutInflater layoutInflater, CoordinatorLayout coordinatorLayout){
        CoreFABDelegate coreFABDelegate = new CoreFABDelegate(layoutInflater, coordinatorLayout);
        CoordinatorLayout.LayoutParams p = ((CoordinatorLayout.LayoutParams)coreFABDelegate.getFAB().getLayoutParams());
        p.gravity = Gravity.BOTTOM | Gravity.END;
        coreFABDelegate.getFAB().setLayoutParams(p);
        return coreFABDelegate;
    }

    public static CoreFABDelegate createFABOnlyImpl(LayoutInflater layoutInflater){
        CoreFABDelegate coreFABDelegate = new CoreFABDelegate(layoutInflater);
        return coreFABDelegate;
    }

    /**
     * will call createFullImpl but with predefined value as this builder intended so FAB can stick to the AppBar
     * @param anchorGravity set -1 to ignore, you can use something like Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL to put FAB in the botttom center of appbar
     * @return
     */
    public static CoreFABDelegate createAnchorAppBarFABImpl(LayoutInflater layoutInflater, CoordinatorLayout coordinatorLayout, int anchorGravity){
        if(anchorGravity == -1) {
            return createFullImpl(layoutInflater, coordinatorLayout, R.id.core_app_bar, Gravity.BOTTOM | Gravity.END, -1, null);
        }
        else{
            return createFullImpl(layoutInflater, coordinatorLayout, R.id.core_app_bar, anchorGravity, -1, null);
        }
    }

    /**
     * Use this builder if you need to customize anchor and behavior
     * @param anchorId this attribute will determine to which view this FAB will be anchored to, set -1 to ignore
     * @param anchorGravity this attribute will determine the anchor position from the anchored view, set -1 to ignore
     * @param gravity this attribute is recessive with anchor gravity, if you set anchorGravity as -1, this gravity will be used instead (no anchoring provided), vice versa. -1 to ignore, do not set concurrent with anchorgravity!
     * @param behavior this will determine the behavior for FAB, currently only support ScrollAwareFAB behavior, set null to ignore
     * @return CoreFABDelegate
     */
    public static CoreFABDelegate createFullImpl(LayoutInflater layoutInflater, CoordinatorLayout coordinatorLayout, @IdRes int anchorId, int anchorGravity, int gravity, FloatingActionButton.Behavior behavior){
        CoreFABDelegate coreFABDelegate = new CoreFABDelegate(layoutInflater, coordinatorLayout);
        CoordinatorLayout.LayoutParams p = ((CoordinatorLayout.LayoutParams) coreFABDelegate.getFAB().getLayoutParams());
        if(anchorId != -1) {
            p.setAnchorId(anchorId);
            if(anchorGravity != -1){
                p.anchorGravity = anchorGravity;
            }
        }
        if(anchorGravity == -1 && gravity != -1){
            p.gravity = gravity;
        }
        if(behavior != null) {
            p.setBehavior(behavior);
        }
        coreFABDelegate.getFAB().setLayoutParams(p);
        return coreFABDelegate;
    }

    protected FloatingActionButton mFloatingActionButton;

    private CoreFABDelegate(LayoutInflater layoutInflater){
        mFloatingActionButton = (FloatingActionButton) layoutInflater.inflate(R.layout.layer_core_fab, null, false);
    }

    private CoreFABDelegate(LayoutInflater layoutInflater, CoordinatorLayout coordinatorLayout) {
//        getAsyncLayoutInflater().inflate(R.layout.layer_core_fab, getCoordinatorLayout(), new AsyncLayoutInflater.OnInflateFinishedListener() {
//            @Override
//            public void onInflateFinished(View view, int resid, ViewGroup parent) {
//                parent.addView(view);
//                CoordinatorLayout.LayoutParams p = ((CoordinatorLayout.LayoutParams)view.getLayoutParams());
//                p.gravity = Gravity.BOTTOM | Gravity.END;
//                view.setLayoutParams(p);
//            }
//        });
        layoutInflater.inflate(R.layout.layer_core_fab, coordinatorLayout, true);
        mFloatingActionButton = (FloatingActionButton) (coordinatorLayout.findViewById(R.id.core_fab));
    }

    /**
     * to set custom image for FAB
     * @param resId
     */
    public void setImageDrawable(int resId){
        mFloatingActionButton.setImageResource(resId);
    }

    /**
     * get FAB
     * @return
     */
    public FloatingActionButton getFAB(){
        return mFloatingActionButton;
    }

    public void setFABListener(View.OnClickListener onClickListener){
        getFAB().setOnClickListener(onClickListener);
    }

    /**
     * Behavior for scrolling behavior, will be automatically set by coordinator layout
     * @param behavior
     */
    public void setFABBehavior(FloatingActionButton.Behavior behavior){
        CoordinatorLayout.LayoutParams p = ((CoordinatorLayout.LayoutParams)getFAB().getLayoutParams());
        p.setBehavior(behavior);
        getFAB().setLayoutParams(p);
    }
}

/**
 * sample usage
 *
 CoreFABDelegate coreFABDelegate = CoreFABDelegate.createFullImpl(this, getIntent(), -1, -1, Gravity.LEFT|Gravity.BOTTOM, null);
 coreFABDelegate.setFABListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
});
 coreFABDelegate.setFABBehavior(new ScrollAwareFABBehavior(this, null));
 */
