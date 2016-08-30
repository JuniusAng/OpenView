package com.junang.openview.delegation;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AsyncLayoutInflater;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by junius.ang on 8/23/2016.
 */
public abstract class CoreDelegate<T extends CoreDelegateDependency>{
    T mCoreDelegateDependency;

    //temporary solution for appbar dependency
    public CoreDelegate(T mCoreDelegateDependency){
        this.mCoreDelegateDependency = mCoreDelegateDependency;
    }

//    public abstract void onCreate(Bundle savedInstanceState);
//    public abstract void onStart();
//    public abstract void onResume();
//    public abstract void onStop();


    public void setCoreDelegateDependency(T mCoreDelegateDependency) {
        this.mCoreDelegateDependency = mCoreDelegateDependency;
    }

    public T getCoreDelegateDependency(){
        return mCoreDelegateDependency;
    }

    protected CoordinatorLayout getCoordinatorLayout(){
        return mCoreDelegateDependency.getCoordinatorLayout();
    }

    protected LayoutInflater getLayoutInflater(){
        return mCoreDelegateDependency.getLayoutInflater();
    }

    protected AsyncLayoutInflater getAsyncLayoutInflater(){
        return mCoreDelegateDependency.getAsyncLayoutInflater();
    }
    /**
     * helper method for view inside AppBar layout only
     * @return
     */
    protected AppBarLayout.LayoutParams getAppBarLayoutParam(View v){
        return (AppBarLayout.LayoutParams)v.getLayoutParams();
    }
}
