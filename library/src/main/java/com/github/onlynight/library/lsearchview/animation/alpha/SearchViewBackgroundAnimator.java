package com.github.onlynight.library.lsearchview.animation.alpha;

import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;

/**
 * Created by lion on 2015/11/26 0026.
 */
public class SearchViewBackgroundAnimator extends AlphaSupportAnimator {

    private static final float DISAPPEAR_ALPHA = 0f;
    private static final float APPEAR_ALPHA = 0.8f;
    public static AlphaAnimator.AlphaValue BACKGROUND_ALPHA = new AlphaAnimator.AlphaValue();
    private WeakReference<Animator> animator;

    public SearchViewBackgroundAnimator(AlphaAnimator backgroundView, Animator animator) {
        super(backgroundView);
        this.animator = new WeakReference<>(animator);
    }

    @Override
    public void start() {
        Animator anim = animator.get();
        if (anim != null) {
            anim.start();
        }
    }

    @Override
    public void cancel() {
        Animator anim = animator.get();
        if (anim != null) {
            anim.cancel();
        }
    }

    @Override
    public Animator get() {
        return animator.get();
    }

    @Override
    public boolean isRunning() {
        Animator anim = animator.get();
        return anim != null && anim.isRunning();
    }

    @Override
    public void setDuration(long duration) {
        Animator anim = animator.get();
        if (anim != null) {
            anim.setDuration(duration);
        }
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        Animator anim = animator.get();
        if (anim != null) {
            anim.setInterpolator(interpolator);
        }
    }

    @Override
    public void addListener(final AlphaAnimatorListener listener) {
        Animator anim = animator.get();
        if (anim == null) {
            return;
        }

        if (listener == null) {
            anim.addListener(null);
            return;
        }

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                listener.onAnimationCancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                listener.onAnimationRepeat();
            }
        });
    }
}
