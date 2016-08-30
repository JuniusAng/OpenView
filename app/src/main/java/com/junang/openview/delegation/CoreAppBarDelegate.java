package com.junang.openview.delegation;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.junang.openview.R;
import com.junang.openview.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreAppBarDelegate<T extends CoreDelegateDependency> extends CoreDelegate<T> implements AppBarLayout.OnOffsetChangedListener{

    public static CoreAppBarDelegate createDefaultImpl(CoreDelegateDependency coreDelegateDependency, boolean isCollapsingToolbarNeeded){
        CoreAppBarDelegate coreAppBarDelegate = new CoreAppBarDelegate(coreDelegateDependency, isCollapsingToolbarNeeded);
        return coreAppBarDelegate;
    }

    private ArrayList<View> addedViewList;
    private CollapsingToolbarLayout vCollapsingToolbarLayout;
    private boolean isCollapsingToolbarLayoutNeeded;
    protected Toolbar vToolbar;
    protected boolean isAnimatingVisibility;

    public CoreAppBarDelegate(T mCoreDelegateDependency, boolean isCollapsingToolbarLayoutNeeded) {
        super(mCoreDelegateDependency);
        addedViewList = new ArrayList<>();
        this.isCollapsingToolbarLayoutNeeded = isCollapsingToolbarLayoutNeeded;

        if(isCollapsingToolbarLayoutNeeded){
            getLayoutInflater().inflate(R.layout.layer_core_collapsing_layout, getCoreDelegateDependency().getAppBarLayout(), true);
            vCollapsingToolbarLayout = ViewUtil.findById(getCoreDelegateDependency().getAppBarLayout(), R.id.core_collapsing_toolbar);
            registerInternalView(vCollapsingToolbarLayout);
            vToolbar = ViewUtil.findById(vCollapsingToolbarLayout, R.id.core_toolbar);
        }
        else{
            getLayoutInflater().inflate(R.layout.layer_core_toolbar, getCoreDelegateDependency().getAppBarLayout(), true);
            vToolbar = ViewUtil.findById(getCoreDelegateDependency().getAppBarLayout(), R.id.core_toolbar);
        }

        //getCoreDelegateDependency().getAppBarLayout().addOnOffsetChangedListener(this);
    }

    /**
     * set toolbar interaction against AppBarLayout scrolling mechanism
     * @param appBarScrollMode set -1 to leave it as non scrollable
     */
    public void setAppBarScrollingBehavior(int appBarScrollMode){
        if(!isCollapsingToolbarLayoutNeeded) {
            AppBarLayout.LayoutParams p = getAppBarLayoutParam(vToolbar);
            if (appBarScrollMode == -1) {
                p.setScrollFlags(0);
            } else {
                p.setScrollFlags(appBarScrollMode);
            }
            vToolbar.setLayoutParams(p);
        }
        //must be CollapsingToolbarLayout.LayoutParams
        else{
            AppBarLayout.LayoutParams p = getAppBarLayoutParam(vCollapsingToolbarLayout);
            if (appBarScrollMode == -1) {
                p.setScrollFlags(0);
            } else {
                p.setScrollFlags(appBarScrollMode);
            }
            vCollapsingToolbarLayout.setLayoutParams(p);
        }
    }

    public void setCollapsingToolbarScrollingBehavior(int collapsingBehavior, View v){
        if(isCollapsingToolbarLayoutNeeded) {
            CollapsingToolbarLayout.LayoutParams p = (CollapsingToolbarLayout.LayoutParams) v.getLayoutParams();
            if (collapsingBehavior == -1) {
                p.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);
            } else {
                p.setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            }
        }
    }

    /**
     * set custom Status bar color rather than predefined primaryDark
     * @param appCompatActivity
     * @param color
     */
    public void setStatusBarColor(AppCompatActivity appCompatActivity, int color){
        if(Build.VERSION.SDK_INT >= 21){
            Window window = appCompatActivity.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(appCompatActivity, color));
        }
    }

    public boolean isAnimatingVisibility() {
        return isAnimatingVisibility;
    }

    /**
     * must be set before changing view hierarchy
     * @param animatingVisibility
     */
    public void setAnimatingVisibility(boolean animatingVisibility) {
        isAnimatingVisibility = animatingVisibility;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return vCollapsingToolbarLayout;
    }

    public Toolbar getToolbar(){
        return vToolbar;
    }

    /**
     * AppBarlayout is vertical linear layout
     * @param v
     * @param isAddToCollapsingDisabled this will make sure no matter the collapsing behavior exists or not, it will always add view to AppBarLayout, if set to false will add to CollapsingLayout if exists
     * @param index set -1 to add it to last index.
     */
    public void addView(View v, int index, boolean isAddToCollapsingDisabled){
        if(index == -1){
            addedViewList.add(v);
        }
        else{
            addedViewList.add(index, v);
        }
        if(isAddToCollapsingDisabled || !isCollapsingToolbarLayoutNeeded){
            if (index == -1) {
                mCoreDelegateDependency.getAppBarLayout().addView(v);
            }
            else{
                mCoreDelegateDependency.getAppBarLayout().addView(v, index);
            }
        }
        else{
            if(index == -1) {
                vCollapsingToolbarLayout.addView(v);
            }
            else{
                vCollapsingToolbarLayout.addView(v, index);
            }
        }
    }

    private void registerInternalView(View v){
        addedViewList.add(v);
    }

    //this will return every view added to AppBar, including view that is a parent to another view (like CollapsingToolbar)
    public ArrayList<View> getViewList(){
        return addedViewList;
    }

    //to detect whether the collapsingToolbarLayout is collapsed or not
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        //collapsed
        if(offset == 0){

        }
        //not collapsed
        else{

        }
//        if (percentage == 1f && isHideToolbarView) {
//            toolbarHeaderView.setVisibility(View.VISIBLE);
//            isHideToolbarView = !isHideToolbarView;
//
//        } else if (percentage < 1f && !isHideToolbarView) {
//            toolbarHeaderView.setVisibility(View.GONE);
//            isHideToolbarView = !isHideToolbarView;
//        }
    }
}
