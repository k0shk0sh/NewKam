package com.fastaccess.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kosh on 27 May 2016, 9:04 PM
 */

public class AnimHelper {

    public static final Interpolator interpolator = new LinearInterpolator();

    @UiThread public static void animateVisibility(final boolean show, @Nullable final View view) {
        if (view == null) {
            return;
        }
        if (show && view.isShown()) return;
        else if (!show && !view.isShown()) return;
        if (!ViewCompat.isAttachedToWindow(view)) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    animateSafeVisibility(show, view);
                    return true;
                }
            });
        } else {
            animateSafeVisibility(show, view);
        }
    }

    @UiThread private static void animateSafeVisibility(final boolean show, @NonNull final View view) {
        ViewPropertyAnimator animator = view.animate().alpha(show ? 1F : 0F).setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        if (show) {
                            view.setScaleX(1);
                            view.setScaleY(1);
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override public void onAnimationEnd(@NonNull Animator animation) {
                        super.onAnimationEnd(animation);
                        if (!show) {
                            view.setVisibility(View.GONE);
                            view.setScaleX(0);
                            view.setScaleY(0);
                        }
                    }
                });
        animator.scaleX(show ? 1 : 0).scaleY(show ? 1 : 0);
    }

    @UiThread @NonNull public static List<ObjectAnimator> getBeats(@NonNull View view) {
        ObjectAnimator[] animator = new ObjectAnimator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
        };
        return Arrays.asList(animator);
    }

    @UiThread public static void startBeatsAnimation(@NonNull View view) {
        List<ObjectAnimator> animators = getBeats(view);
        for (ObjectAnimator anim : animators) {
            anim.setDuration(300).start();
            anim.setInterpolator(interpolator);
        }
    }
}
