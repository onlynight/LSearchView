package com.github.onlynight.library.lsearchview.animation.alpha;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.util.FloatProperty;

import java.lang.ref.WeakReference;

/**
 * Created by lion on 2015/11/26 0026.
 */
public interface AlphaAnimator {

    void onAlphaAnimationStart();

    void onAlphaAnimationEnd();

    void onAlphaAnimationCancel();

    AlphaSupportAnimator startAlphaReverseAnimation();

    void setAlphaValue(float alpha);

    float getAlphaValue();

    void setAlphaInfo(AlphaInfo info);

    class AlphaInfo {
        private float startAlpha;
        private float endAlpha;
        private WeakReference<View> target;

        public AlphaInfo(WeakReference<View> target, float startAlpha, float endAlpha) {
            this.startAlpha = startAlpha;
            this.endAlpha = endAlpha;
            this.target = target;
        }

        public View getTarget() {
            return target.get();
        }

        public boolean hasTarget() {
            return target != null;
        }

        public float getStartAlpha() {
            return startAlpha;
        }

        public float getEndAlpha() {
            return endAlpha;
        }
    }

    class AlphaValue extends FloatProperty<AlphaAnimator> {
        public AlphaValue() {
            super("AlphaValue");
        }

        @Override
        public void setValue(AlphaAnimator object, float value) {
            object.setAlphaValue(value);
        }

        @Override
        public Float get(AlphaAnimator object) {
            return object.getAlphaValue();
        }
    }

    class AlphaAnimationListener extends SimpleAnimationListener {
        WeakReference<AlphaAnimator> mReference;

        AlphaAnimationListener(AlphaAnimator target) {
            mReference = new WeakReference<>(target);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            AlphaAnimator target = mReference.get();
            target.onAlphaAnimationStart();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            AlphaAnimator target = mReference.get();
            target.onAlphaAnimationCancel();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            AlphaAnimator target = mReference.get();
            target.onAlphaAnimationEnd();
        }
    }

    class SimpleAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
