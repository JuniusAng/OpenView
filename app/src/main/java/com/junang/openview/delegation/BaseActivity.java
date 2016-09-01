package com.junang.openview.delegation;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by junius.ang on 9/1/2016.
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected CoreDelegateDependency mCoreDelegateDependency;
    protected CoreAppBarDelegate mCoreAppBarDelegate;
    protected void initDelegate(){

    }
}
