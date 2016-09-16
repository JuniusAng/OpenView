package com.junang.openview;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.junang.openview.delegation.CoreAppBarDelegate;
import com.junang.openview.delegation.CoreInputLayoutWidget;
import com.junang.openview.delegation.CoreFABDelegate;
import com.junang.openview.delegation.CoreTabDelegate;
import com.junang.openview.delegation.behavior.ScrollAwareFABBehavior;
import com.junang.openview.util.ViewUtil;
import com.junang.openview.widget.ClickToSelectEditText;
import com.junang.openview.widget.Listable;
import com.transitionseverywhere.TransitionManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static com.junang.openview.util.ViewUtil.findById;

public class ScrollingActivity extends AppCompatActivity {
    CoreAppBarDelegate coreAppBarDelegate;
    TextView tv;
    boolean isShow;
    String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example"};
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

//        final CoreDelegateDependency coreDelegateDependency = new CoreDelegateDependency(new AsyncLayoutInflater(this), getLayoutInflater(),
//                (CoordinatorLayout) findViewById(R.id.core_coordinator_layout),
//                (AppBarLayout) findViewById(R.id.core_app_bar),
//                (NestedScrollView) findViewById(R.id.core_content_scroll));
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.core_coordinator_layout);
        ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text3)).setIsHintFloating(false).setHint("hint inline*");
        ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text4)).setIsHintFloating(true).setHint("hint floating*");
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.core_app_bar);
        CoreFABDelegate coreFABDelegate = CoreFABDelegate.createDefaultImpl(getLayoutInflater(),coordinatorLayout );
        coreFABDelegate.setFABBehavior(new ScrollAwareFABBehavior());
        coreFABDelegate.setFABListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(coordinatorLayout);
                if(!isShow) {
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text3)).setIsErrorBottom(false).setError("error inline");
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text4)).setIsErrorBottom(true).setError("error below");
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text)).setCounterEnabled(false);
                }
                else {
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text3)).setIsErrorBottom(false).setError(null);
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text4)).setIsErrorBottom(true).setError(null);
                    ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text)).setCounterEnabled(true);
                }
                isShow = !isShow;
//                if(tv == null) {
//                    tv = new TextView(ScrollingActivity.this);
//                    tv.setText("Form here : initialization example");
//                    appBarLayout.addView(tv, 1);
//                }
//                else{
//                    tv.getLayoutParams().height = 300;
//                    if(tv.getVisibility() == View.VISIBLE) {
//                        tv.setVisibility(View.GONE);
//                    }
//                    else {
//                        tv.setVisibility(View.VISIBLE);
//                        tv.setText("Form here : Expanded");
//                    }
//                }

            }
        });
        coreAppBarDelegate = CoreAppBarDelegate.createDefaultImpl(getLayoutInflater(), appBarLayout, true);
        coreAppBarDelegate.setStatusBarColor(this, R.color.colorPrimaryDark);
        coordinatorLayout.setFitsSystemWindows(true);
        coreAppBarDelegate.setAppBarScrollingBehavior(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        coreAppBarDelegate.setCollapsingToolbarScrollingBehavior(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN, coreAppBarDelegate.getToolbar());
//        CoreTabDelegate coreTabDelegate = CoreTabDelegate.createDefaultImpl(getLayoutInflater(), appBarLayout);
//        coreTabDelegate.setScrollMode(TabLayout.MODE_FIXED);
//        coreTabDelegate.createDummyTab(2);
//        coreTabDelegate.setCustomViewToTab(R.layout.sample_layer_tab_title, 0, -1);
        setSupportActionBar(coreAppBarDelegate.getToolbar());

        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setShowHideAnimationEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text)).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText ed = (EditText) v;
                if(ed.getText() != null && ed.getText().toString().contains("adapt focus")){
                    TransitionManager.beginDelayedTransition(coordinatorLayout);
                    if(!hasFocus) {
                        ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text)).setCounterEnabled(false);
                    }
                    else{
                        ((CoreInputLayoutWidget) findById(ScrollingActivity.this, R.id.error_text)).setCounterEnabled(true);
                    }
                }

            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.widget_spinner_text, SPINNERLIST);
        AppCompatSpinner materialDesignSpinner = (AppCompatSpinner)
                findViewById(R.id.spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);
        getSupportActionBar().setTitle("asdasd");

        TextInputLayout asd = ViewUtil.findById(this, R.id.asd);
        AppCompatSpinner materialDesignSpinner1 = (AppCompatSpinner)
                findViewById(R.id.spinner1);
        materialDesignSpinner1.setAdapter(arrayAdapter);

        ClickToSelectEditText editTextJobCategory = ViewUtil.findById(this, R.id.signup_text_input_job_category);
        ArrayList<Data> mData = new ArrayList<>();
        for(String str : SPINNERLIST){
            mData.add(new Data(str));
        }
        editTextJobCategory.setItems(mData);
        editTextJobCategory.setOnItemSelectedListener(new ClickToSelectEditText.OnItemSelectedListener<Data>() {
            @Override
            public void onItemSelectedListener(Data item, int selectedIndex) {
                Toast.makeText(ScrollingActivity.this, item.getLabel(), Toast.LENGTH_SHORT).show();
            }
        });

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

    private static class Data implements Listable{
        public Data(String label) {
            this.label = label;
        }

        public String label;
        @Override
        public String getLabel() {
            return label;
        }
    }
}
