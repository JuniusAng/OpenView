package com.junang.openview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.junang.openview.delegation.CoreAppBarDelegate;
import com.junang.openview.delegation.CoreDelegateDependency;
import com.junang.openview.delegation.CoreFABDelegate;
import com.junang.openview.delegation.CoreTabDelegate;
import com.junang.openview.delegation.behavior.ScrollAwareFABBehavior;

public class ScrollingActivity extends AppCompatActivity {

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

        CoreDelegateDependency coreDelegateDependency = new CoreDelegateDependency(getLayoutInflater(),
                (CoordinatorLayout) findViewById(R.id.core_coordinator_layout),
                (AppBarLayout) findViewById(R.id.core_app_bar),
                (NestedScrollView) findViewById(R.id.core_content_scroll));
        CoreFABDelegate coreFABDelegate = CoreFABDelegate.createDefaultFABImpl(coreDelegateDependency);
        coreFABDelegate.setFABBehavior(new ScrollAwareFABBehavior());
        coreFABDelegate.setFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CoreAppBarDelegate coreAppBarDelegate = CoreAppBarDelegate.createDefaultImpl(coreDelegateDependency, false);
        coreAppBarDelegate.setAppBarScrollingBehavior(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
//        coreAppBarDelegate.setCollapsingToolbarScrollingBehavior(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN, coreAppBarDelegate.getToolbar());
        CoreTabDelegate<CoreDelegateDependency> coreTabDelegate = CoreTabDelegate.createDefaultImpl(coreDelegateDependency);
        coreTabDelegate.setScrollMode(TabLayout.MODE_FIXED);
//        coreTabDelegate.setAppBarScrollingBehavior(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        coreTabDelegate.createDummyTab(2);
        coreTabDelegate.setCustomViewToTab(R.layout.sample_layer_tab_title, 0, -1);
        setSupportActionBar(coreAppBarDelegate.getToolbar());

//        toolbarHeaderView.bindTo("Some Title", "Subtitle");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        coreAppBarDelegate.getCollapsingToolbarLayout().setTitle("");
    }
}
