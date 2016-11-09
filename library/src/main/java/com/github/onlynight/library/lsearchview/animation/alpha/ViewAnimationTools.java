package com.github.onlynight.library.lsearchview.animation.alpha;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

import java.lang.ref.WeakReference;

/**
 * Created by lion on 2015/11/26 0026.
 */
public class ViewAnimationTools {

    public static AlphaSupportAnimator createAlphaAnimator(View view, float startAlpha, float endAlpha) {
        if (!(view instanceof AlphaAnimator)) {
            throw new IllegalArgumentException("View must be implements AlphaAnimator.");
        }

        AlphaAnimator alphaView = (AlphaAnimator) view;
        alphaView.setAlphaInfo(new AlphaAnimator.AlphaInfo(new WeakReference<>(view), startAlpha, endAlpha));
        ObjectAnimator alphaAnim = ObjectAnimator.
                ofFloat(alphaView, SearchViewBackgroundAnimator.BACKGROUND_ALPHA, startAlpha, endAlpha);
        alphaAnim.addListener(new AlphaAnimator.SimpleAnimationListener());
        return new SearchViewBackgroundAnimator(alphaView, alphaAnim);
    }
}
