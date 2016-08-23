package com.junang.openview.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.CheckResult;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by junius.ang on 8/23/16.
 */
public class ViewUtil {
    /////taken directly from
    /////https://github.com/JakeWharton/butterknife/blob/master/butterknife/src/main/java/butterknife/ButterKnife.java
    /** Simpler version of {@link View#findViewById(int)} which infers the target type. */
    @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    /** Simpler version of {@link Activity#findViewById(int)} which infers the target type. */
    @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    /** Simpler version of {@link Dialog#findViewById(int)} which infers the target type. */
    @SuppressWarnings({ "unchecked", "UnusedDeclaration" }) // Checked by runtime cast. Public API.
    @CheckResult
    public static <T extends View> T findById(@NonNull Dialog dialog, @IdRes int id) {
        return (T) dialog.findViewById(id);
    }
    ////

    ////Converter
    public static float dp_px(final float dp){
        return dp * ViewValue.get().getScreenDensity();
    }
    public static float px_dp(final float px){
        return px / ViewValue.get().getScreenDensity();
    }
}
