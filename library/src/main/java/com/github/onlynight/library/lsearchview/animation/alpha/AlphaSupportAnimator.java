package com.github.onlynight.library.lsearchview.animation.alpha;

import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;

import java.lang.ref.WeakReference;

/**
 * Created by lion on 2015/11/26 0026.
 */
public abstract class AlphaSupportAnimator {

    private WeakReference<AlphaAnimator> target;

    public AlphaSupportAnimator(AlphaAnimator target) {
        this.target = new WeakReference<>(target);
    }

    public abstract void start();

    public abstract void cancel();

    public AlphaSupportAnimator reverse() {
        if (isRunning()) {
            return null;
        }

        AlphaAnimator animator = target.get();
        if (animator != null) {
            return animator.startAlphaReverseAnimation();
        }

        return null;
    }

    public abstract Animator get();

    public abstract boolean isRunning();

    public abstract void setDuration(long duration);

    public abstract void setInterpolator(Interpolator interpolator);

    public abstract void addListener(AlphaAnimatorListener listener);

    /**
     * <p>An animation listener receives notifications from an animation.
     * Notifications indicate animation related events, such as the end or the
     * repetition of the animation.</p>
     */
    public interface AlphaAnimatorListener {
        /**
         * <p>Notifies the start of the animation.</p>
         */
        void onAnimationStart();

        /**
         * <p>Notifies the end of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         */
        void onAnimationEnd();

        /**
         * <p>Notifies the cancellation of the animation. This callback is not invoked
         * for animations with repeat count set to INFINITE.</p>
         */
        void onAnimationCancel();

        /**
         * <p>Notifies the repetition of the animation.</p>
         */
        void onAnimationRepeat();
    }

    /**
     * <p>Provides default implementation for AnimatorListener.</p>
     */
    public static abstract class SimpleAlphaAnimatorListener implements AlphaAnimatorListener {

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {

        }

        @Override
        public void onAnimationCancel() {

        }

        @Override
        public void onAnimationRepeat() {

        }
    }
}
