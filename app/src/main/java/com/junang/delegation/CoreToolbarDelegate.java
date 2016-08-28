package com.junang.delegation;

import android.support.v7.widget.Toolbar;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreToolbarDelegate<T extends CoreDelegateDependency> extends CoreDelegate<T> {
    public static CoreToolbarDelegate createDefaultImpl(CoreDelegateDependency coreDelegateDependency){
        CoreToolbarDelegate coreToolbarDelegate = new CoreToolbarDelegate(coreDelegateDependency);
        //default is not using collapsing toolbar
        coreDelegateDependency.getAppBarLayout().addView(coreToolbarDelegate.getToolbar());
        return coreToolbarDelegate;
    }

    protected Toolbar vToolbar;

    public CoreToolbarDelegate(T mCoreDelegateDependency) {
        super(mCoreDelegateDependency);
    }

    public void setTitle(CharSequence charSequence){

    }

    public void setSubtitle(CharSequence charSequence){

    }

    public Toolbar getToolbar() {
        return vToolbar;
    }
}
