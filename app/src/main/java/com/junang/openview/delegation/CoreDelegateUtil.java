package com.junang.openview.delegation;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by junius.ang on 8/23/2016.
 */
public abstract class CoreDelegateUtil{
        /**
     * helper method for view inside AppBar layout only
     * @return
     */
    public static AppBarLayout.LayoutParams getAppBarLayoutParam(View v){
        return (AppBarLayout.LayoutParams)v.getLayoutParams();
    }
}
