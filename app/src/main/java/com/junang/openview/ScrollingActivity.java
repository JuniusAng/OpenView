package com.junang.openview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.junang.openview.delegation.CoreAppBarDelegate;
import com.junang.openview.delegation.CoreDelegateDependency;
import com.junang.openview.delegation.CoreFABDelegate;
import com.junang.openview.delegation.CoreTabDelegate;
import com.junang.openview.delegation.behavior.ScrollAwareFABBehavior;
import com.transitionseverywhere.TransitionManager;

public class ScrollingActivity extends AppCompatActivity {
    CoreAppBarDelegate coreAppBarDelegate;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final CoreDelegateDependency coreDelegateDependency = new CoreDelegateDependency(new AsyncLayoutInflater(this), getLayoutInflater(),
                (CoordinatorLayout) findViewById(R.id.core_coordinator_layout),
                (AppBarLayout) findViewById(R.id.core_app_bar),
                (NestedScrollView) findViewById(R.id.core_content_scroll));
        CoreFABDelegate coreFABDelegate = CoreFABDelegate.createDefaultImpl(coreDelegateDependency);
        coreFABDelegate.setFABBehavior(new ScrollAwareFABBehavior());
        coreFABDelegate.setFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(coreDelegateDependency.getCoordinatorLayout());
                if(tv == null) {
                    tv = new TextView(ScrollingActivity.this);
                    tv.setText("Form here : initialization example");
                    coreDelegateDependency.getAppBarLayout().addView(tv, 1);
                }
                else{
                    tv.getLayoutParams().height = 300;
                    if(tv.getVisibility() == View.VISIBLE)
                        tv.setVisibility(View.GONE);
                    else {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText("Form here : Expanded");
                    }
                }
            }
        });
//        CoreFABDelegate coreFABDelegate = new CoreFABDelegate(coreDelegateDependency);
        coreAppBarDelegate = CoreAppBarDelegate.createDefaultImpl(coreDelegateDependency, false);
        coreAppBarDelegate.setStatusBarColor(this, R.color.colorPrimaryDark);
        coreAppBarDelegate.setAppBarScrollingBehavior(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
//        coreAppBarDelegate.setCollapsingToolbarScrollingBehavior(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN, coreAppBarDelegate.getToolbar());
        CoreTabDelegate<CoreDelegateDependency> coreTabDelegate = CoreTabDelegate.createDefaultImpl(coreDelegateDependency);
//        coreTabDelegate.setScrollMode(TabLayout.MODE_FIXED);
        coreTabDelegate.createDummyTab(2);
        coreTabDelegate.setCustomViewToTab(R.layout.sample_layer_tab_title, 0, -1);
        setSupportActionBar(coreAppBarDelegate.getToolbar());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
