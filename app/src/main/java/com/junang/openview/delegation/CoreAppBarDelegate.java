package com.junang.openview.delegation;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junang.openview.R;
import com.junang.openview.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreAppBarDelegate implements AppBarLayout.OnOffsetChangedListener{

    public static CoreAppBarDelegate createDefaultImpl(LayoutInflater layoutInflater, AppBarLayout appBarLayout, boolean isCollapsingToolbarLayoutNeeded){
        CoreAppBarDelegate coreAppBarDelegate = new CoreAppBarDelegate(layoutInflater, appBarLayout, isCollapsingToolbarLayoutNeeded);
        return coreAppBarDelegate;
    }

    public static CoreAppBarDelegate createNoToolbarImpl(){
        CoreAppBarDelegate coreAppBarDelegate = new CoreAppBarDelegate();
        return coreAppBarDelegate;
    }


    private ArrayList<View> addedViewList;
    private CollapsingToolbarLayout vCollapsingToolbarLayout;
    private boolean isCollapsingToolbarLayoutNeeded;
    protected Toolbar vToolbar;
    private AppBarLayout vAppBarLayout;
    protected boolean isAnimatingVisibility;

    private CoreAppBarDelegate(){
        addedViewList = new ArrayList<>();
    }

    public CoreAppBarDelegate(LayoutInflater layoutInflater, AppBarLayout appBarLayout, boolean isCollapsingToolbarLayoutNeeded) {
        vAppBarLayout = appBarLayout;
        vAppBarLayout.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                Log.d("height","measureHeight : "+ vAppBarLayout.getMeasuredHeight()+" "+vAppBarLayout.getHeight());
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });

        ViewTreeObserver vto = vAppBarLayout.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    try {
                        Log.d("height_vto","measureHeight : "+ vAppBarLayout.getMeasuredHeight()+" "+vAppBarLayout.getHeight());
                        if (Build.VERSION.SDK_INT >= 16) {
                            vAppBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            vAppBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        vAppBarLayout.getLayoutParams().height = vAppBarLayout.getMeasuredHeight();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            });
        }
        addedViewList = new ArrayList<>();
        this.isCollapsingToolbarLayoutNeeded = isCollapsingToolbarLayoutNeeded;

        if(isCollapsingToolbarLayoutNeeded){
            layoutInflater.inflate(R.layout.layer_core_collapsing_layout, appBarLayout, true);
            vCollapsingToolbarLayout = ViewUtil.findById(appBarLayout, R.id.core_collapsing_toolbar);
            registerInternalView(vCollapsingToolbarLayout);
            vToolbar = ViewUtil.findById(vCollapsingToolbarLayout, R.id.core_toolbar);
        }
        else{
            layoutInflater.inflate(R.layout.layer_core_toolbar, appBarLayout, true);
            vToolbar = ViewUtil.findById(appBarLayout, R.id.core_toolbar);
        }
//        vAppBarLayout.getLayoutParams().height = vAppBarLayout.getMeasuredHeight();
        registerInternalView(vToolbar);

        //appBarLayout.addOnOffsetChangedListener(this);
    }

    /**
     * set toolbar interaction against AppBarLayout scrolling mechanism
     * @param appBarScrollMode set -1 to leave it as non scrollable
     */
    public void setAppBarScrollingBehavior(int appBarScrollMode){
        if(!isCollapsingToolbarLayoutNeeded) {
            AppBarLayout.LayoutParams p = CoreDelegateUtil.getAppBarLayoutParam(vToolbar);
            if (appBarScrollMode == -1) {
                p.setScrollFlags(0);
            } else {
                p.setScrollFlags(appBarScrollMode);
            }
            vToolbar.setLayoutParams(p);
        }
        //must be CollapsingToolbarLayout.LayoutParams
        else{
            AppBarLayout.LayoutParams p = CoreDelegateUtil.getAppBarLayoutParam(vCollapsingToolbarLayout);
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
                vAppBarLayout.addView(v);
            }
            else{
                vAppBarLayout.addView(v, index);
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
