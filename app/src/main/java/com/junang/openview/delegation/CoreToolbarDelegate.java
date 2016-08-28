package com.junang.openview.delegation;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;

import com.junang.openview.R;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreToolbarDelegate<T extends CoreDelegateDependency> extends CoreDelegate<T> {
    public static CoreToolbarDelegate createDefaultImpl(CoreDelegateDependency coreDelegateDependency){
        CoreToolbarDelegate coreToolbarDelegate = new CoreToolbarDelegate(coreDelegateDependency);
        return coreToolbarDelegate;
    }

    protected Toolbar vToolbar;

    public CoreToolbarDelegate(T mCoreDelegateDependency) {
        super(mCoreDelegateDependency);
        vToolbar = (Toolbar) getLayoutInflater().inflate(R.layout.layer_core_toolbar, null, false);
    }

    /**
     * set toolbar interaction against AppBarLayout scrolling mechanism
     * @param appBarScrollMode set -1 to leave it as non scrollable
     */
    public void setAppBarScrollingBehavior(int appBarScrollMode){
        AppBarLayout.LayoutParams p = getAppBarLayoutParam(vToolbar);
        if(appBarScrollMode == -1){
            p.setScrollFlags(0);
        }
        else {
            p.setScrollFlags(appBarScrollMode);
        }
        vToolbar.setLayoutParams(p);
    }

    public void setTitle(CharSequence charSequence){

    }

    public void setSubtitle(CharSequence charSequence){

    }

    public Toolbar getToolbar() {
        return vToolbar;
    }
}
