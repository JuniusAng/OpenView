package com.junang.delegation;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by junius.ang on 8/24/2016.
 */
public class CoreAppBarDelegate<T extends CoreDelegateDependency> extends CoreDelegate<T> {
    public ArrayList<View> addedViewList;
    public CoreAppBarDelegate(T mCoreDelegateDependency) {
        super(mCoreDelegateDependency);
        addedViewList = new ArrayList<>();
    }

    public void addView(View v){
        addedViewList.add(v);
    }

    public ArrayList<View> getView(View v){
        return addedViewList;
    }
}
