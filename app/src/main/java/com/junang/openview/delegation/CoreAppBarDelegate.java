package com.junang.openview.delegation;

import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.junang.openview.R;
import com.junang.openview.util.ViewUtil;

import java.util.ArrayList;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreAppBarDelegate<T extends CoreDelegateDependency> extends CoreDelegate<T> {

    public static CoreAppBarDelegate createDefaultImpl(CoreDelegateDependency coreDelegateDependency, boolean isCollapsingToolbarNeeded){
        CoreAppBarDelegate coreAppBarDelegate = new CoreAppBarDelegate(coreDelegateDependency, isCollapsingToolbarNeeded);
        return coreAppBarDelegate;
    }

    private ArrayList<View> addedViewList;
    private CollapsingToolbarLayout vCollapsingToolbarLayout;
    private boolean isCollapsingToolbarLayoutNeeded;

    public CoreAppBarDelegate(T mCoreDelegateDependency, boolean isCollapsingToolbarLayoutNeeded) {
        super(mCoreDelegateDependency);
        addedViewList = new ArrayList<>();
        this.isCollapsingToolbarLayoutNeeded = isCollapsingToolbarLayoutNeeded;

        if(isCollapsingToolbarLayoutNeeded){
            getLayoutInflater().inflate(R.layout.layer_core_collapsing_layout, getCoreDelegateDependency().getAppBarLayout(), true);
            vCollapsingToolbarLayout = ViewUtil.findById(getCoreDelegateDependency().getAppBarLayout(), R.id.core_collapsing_toolbar);
        }
    }

    /**
     *
     * @param toolbarDelegate
     * @param appCompatActivity
     * @param appBarScroll set -1 to let it non scrollable
     */
    public void addToolbarView(CoreToolbarDelegate toolbarDelegate, AppCompatActivity appCompatActivity, int appBarScroll){
        if(isCollapsingToolbarLayoutNeeded){
            vCollapsingToolbarLayout.addView(toolbarDelegate.getToolbar());
        }
        else{
            getCoreDelegateDependency().getAppBarLayout().addView(toolbarDelegate.getToolbar());
        }
        toolbarDelegate.setAppBarScrollingBehavior(appBarScroll);
        appCompatActivity.setSupportActionBar(toolbarDelegate.getToolbar());
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

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return vCollapsingToolbarLayout;
    }

    public void addView(View v){
        addedViewList.add(v);
    }

    public ArrayList<View> getView(View v){
        return addedViewList;
    }
}
