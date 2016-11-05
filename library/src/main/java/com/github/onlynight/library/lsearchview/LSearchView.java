package com.github.onlynight.library.lsearchview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.onlynight.library.lsearchview.animation.alpha.AlphaAnimator;
import com.github.onlynight.library.lsearchview.animation.alpha.AlphaSupportAnimator;
import com.github.onlynight.library.lsearchview.animation.alpha.ViewAnimationTools;
import com.github.onlynight.library.lsearchview.animation.reveal.RevealAnimator;
import com.github.onlynight.library.lsearchview.animation.reveal.SupportAnimator;
import com.github.onlynight.library.lsearchview.animation.reveal.ViewAnimationUtils;

/**
 * Created by lion on 2016/11/4.
 */

public class LSearchView extends LinearLayout implements AlphaAnimator, RevealAnimator {

    private static final float DEFAULT_SEARCH_BAR_HEIGHT = 48;
    private static final float DEFAULT_SEARCH_BAR_BACK_BUTTON_PADDING = 12;
    private static final float DEFAULT_SEARCH_BAR_SEARCH_BUTTON_PADDING = 12;
    private static final int DEFAULT_SEARCH_BAR_BG_COLOR = Color.argb(0xaa, 0, 0, 0);
    private static final int DEFAULT_ANIM_TIME = 300;
    private static final int DEFAULT_ANIM_ORIGIN_X = 23; //dp
    private static final int DEFAULT_ANIM_ORIGIN_Y = 23; //dp
    private static final int DEFAULT_IC_BACK = R.drawable.ic_back;
    private static final int DEFAULT_IC_SEARCH = R.drawable.ic_search;

    private float searchBarHeight = 0;
    private float backBtnPadding = 0;
    private float searchBtnPadding = 0;
    private String searchHint = "";
    private int searchBgColor = DEFAULT_SEARCH_BAR_BG_COLOR;
    private int animTime = 0;
    private float originX = 0;
    private float originY = 0;
    private int icBack = DEFAULT_IC_BACK;
    private int icSearch = DEFAULT_IC_SEARCH;

    private LinearLayout btnBackLayout;
    private EditText editSearch;
    private LinearLayout btnSearchLayout;
    //    private LinearLayout btnClearLayout;
    private ImageView btnSearch;
    private ProgressBar progressBarSearch;

    private float alpha;
    private float defaultAlpha;
    private boolean running;
    private AlphaInfo alphaInfo;

    private Path revealPath;
    private Rect targetBounds;
    private boolean mRunning;
    private RevealInfo revealInfo;
    private float radius;

    private AlphaSupportAnimator searchViewBaseAnimator;
    private SupportAnimator searchViewAnimator;

    public LSearchView(Context context) {
        super(context);
        init(null);
    }

    public LSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.LSearchView);
        init(typedArray);
    }

    public LSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.LSearchView, defStyleAttr, 0);
        init(typedArray);
    }

    @TargetApi(21)
    public LSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.LSearchView, defStyleAttr, defStyleRes);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(INVISIBLE);

        if (typedArray != null) {
            searchBarHeight = typedArray.getDimension(
                    R.styleable.LSearchView_search_bar_height, dp2px(getContext(),
                            DEFAULT_SEARCH_BAR_HEIGHT));
            backBtnPadding = typedArray.getDimension(
                    R.styleable.LSearchView_back_button_padding, dp2px(getContext(),
                            DEFAULT_SEARCH_BAR_BACK_BUTTON_PADDING));
            searchBtnPadding = typedArray.getDimension(
                    R.styleable.LSearchView_search_button_padding, dp2px(getContext(),
                            DEFAULT_SEARCH_BAR_SEARCH_BUTTON_PADDING));
            searchHint = typedArray.getString(R.styleable.LSearchView_search_hint);
            searchBgColor = typedArray.getColor(R.styleable.LSearchView_search_bg_color,
                    DEFAULT_SEARCH_BAR_BG_COLOR);
            animTime = typedArray.getInteger(R.styleable.LSearchView_anim_time,
                    DEFAULT_ANIM_TIME);
            originX = typedArray.getDimension(R.styleable.LSearchView_anim_origin_x,
                    DEFAULT_ANIM_ORIGIN_X);
            originY = typedArray.getDimension(R.styleable.LSearchView_anim_origin_y,
                    DEFAULT_ANIM_ORIGIN_Y);
            icBack = typedArray.getResourceId(R.styleable.LSearchView_back_btn_icon,
                    DEFAULT_IC_BACK);
            icSearch = typedArray.getResourceId(R.styleable.LSearchView_search_btn_icon,
                    DEFAULT_IC_SEARCH);
        } else {
            searchBarHeight = dp2px(getContext(),
                    DEFAULT_SEARCH_BAR_HEIGHT);
            backBtnPadding = dp2px(getContext(),
                    DEFAULT_SEARCH_BAR_BACK_BUTTON_PADDING);
            searchBtnPadding = dp2px(getContext(),
                    DEFAULT_SEARCH_BAR_SEARCH_BUTTON_PADDING);
            searchBgColor = DEFAULT_SEARCH_BAR_BG_COLOR;
            animTime = DEFAULT_ANIM_TIME;
            originX = dp2px(getContext(), DEFAULT_ANIM_ORIGIN_X);
            originY = dp2px(getContext(), DEFAULT_ANIM_ORIGIN_Y);
        }

        targetBounds = new Rect();
        revealPath = new Path();

        addDefaultView();
    }

    private void addDefaultView() {
        // base layout
        LinearLayout searchBarBaseLayout = new LinearLayout(getContext());
        searchBarBaseLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                (int) searchBarHeight));
        searchBarBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
        searchBarBaseLayout.setBackgroundResource(R.drawable.l_search_view_bg);

        // button back
        ImageView btnBack = new ImageView(getContext());
        LayoutParams btnBackLayoutParams = new LayoutParams(
                (int) (searchBarHeight - backBtnPadding * 2),
                (int) (searchBarHeight - backBtnPadding * 2));
        btnBack.setLayoutParams(btnBackLayoutParams);
        btnBack.setImageResource(icBack);

        btnBackLayout = new LinearLayout(getContext());
        LayoutParams temp = new LayoutParams((int) searchBarHeight,
                (int) searchBarHeight);
        btnBackLayout.setLayoutParams(temp);
        btnBackLayout.setBackgroundResource(R.drawable.l_search_button_bg);
        btnBackLayout.setGravity(Gravity.CENTER);
        btnBackLayout.addView(btnBack);
        btnBackLayout.setClickable(true);

        // edittext search content
        editSearch = new EditText(getContext());
        LayoutParams editSearchLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        editSearchLayoutParams.weight = 1;
        editSearch.setLayoutParams(editSearchLayoutParams);
        editSearch.setBackgroundDrawable(null);
        editSearch.setHint(searchHint);

        // button search
        btnSearch = new ImageView(getContext());
        LayoutParams btnSearchLayoutParams = new LayoutParams(
                (int) (searchBarHeight - searchBtnPadding * 2),
                (int) (searchBarHeight - searchBtnPadding * 2));
        btnSearch.setLayoutParams(btnSearchLayoutParams);
        btnSearch.setImageResource(icSearch);
        btnSearch.setVisibility(VISIBLE);

        // progress bar
        progressBarSearch = new ProgressBar(getContext());
        LayoutParams progressBarSearchParams = new LayoutParams(
                (int) (searchBarHeight - backBtnPadding * 2),
                (int) (searchBarHeight - backBtnPadding * 2));
        progressBarSearch.setLayoutParams(progressBarSearchParams);
        progressBarSearch.setVisibility(GONE);

        btnSearchLayout = new LinearLayout(getContext());
        temp = new LayoutParams((int) searchBarHeight,
                (int) searchBarHeight);
        btnSearchLayout.setLayoutParams(temp);
        btnSearchLayout.setBackgroundResource(R.drawable.l_search_button_bg);
        btnSearchLayout.setGravity(Gravity.CENTER);
        btnSearchLayout.addView(btnSearch);
        btnSearchLayout.addView(progressBarSearch);
        btnSearchLayout.setClickable(true);

        // clear button
//        ImageView btnClear = new ImageView(getContext());
//        LayoutParams btnClearLayoutParams = new LayoutParams(
//                (int) (searchBarHeight - backBtnPadding * 2),
//                (int) (searchBarHeight - backBtnPadding * 2));
//        btnClear.setLayoutParams(btnClearLayoutParams);
//        btnClear.setImageResource(R.drawable.ic_search_delete);
//
//        btnClearLayout = new LinearLayout(getContext());
//        temp = new LayoutParams((int) searchBarHeight,
//                (int) searchBarHeight);
//        btnClearLayout.setLayoutParams(temp);
//        btnClearLayout.setBackgroundResource(R.drawable.l_search_button_bg);
//        btnClearLayout.setGravity(Gravity.CENTER);
//        btnClearLayout.addView(btnClear);
//        btnClearLayout.setClickable(true);

        searchBarBaseLayout.addView(btnBackLayout);
        searchBarBaseLayout.addView(editSearch);
        searchBarBaseLayout.addView(btnSearchLayout);

        addView(searchBarBaseLayout);

//        LinearLayout temp1 = new LinearLayout(getContext());
//        temp1.setLayoutParams(new LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 50)));
//        temp1.setBackgroundColor(Color.RED);
//        addView(temp1);

        btnBackLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideWithAnim();
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hideWithAnim();
            }
        });
    }

    public void setSearching(boolean searching) {
        if (searching) {
            btnSearchLayout.setClickable(false);
            btnSearch.setVisibility(GONE);
            progressBarSearch.setVisibility(VISIBLE);
        } else {
            btnSearchLayout.setClickable(true);
            btnSearch.setVisibility(VISIBLE);
            progressBarSearch.setVisibility(GONE);
        }
    }

    private void startAnimation() {
        startAlphaAnimation();
        startRevealAnim();
    }

    public LinearLayout getBackButton() {
        return btnBackLayout;
    }

    public LinearLayout getSearchButton() {
        return btnSearchLayout;
    }

    public EditText getSearchEdit() {
        return editSearch;
    }

    /**
     * forbidden HORIZONTAL
     *
     * @param orientation
     */
    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public void onAlphaAnimationStart() {
        running = true;
    }

    @Override
    public void onAlphaAnimationEnd() {
        running = true;
        setAlpha(defaultAlpha);
    }

    @Override
    public void onAlphaAnimationCancel() {
        onAlphaAnimationEnd();
    }

    @Override
    public AlphaSupportAnimator startAlphaReverseAnimation() {
        if (alphaInfo != null && alphaInfo.hasTarget() && !running) {
            return ViewAnimationTools.createAlphaAnimator(alphaInfo.getTarget(),
                    alphaInfo.getEndAlpha(), alphaInfo.getStartAlpha());
        }
        return null;
    }

    @Override
    public void setAlphaValue(float alpha) {
        this.alpha = alpha;
        defaultAlpha = alphaInfo.getTarget().getAlpha();
//        setAlpha(defaultAlpha);
        int color = Color.argb((int) (alpha * Color.alpha(searchBgColor)),
                Color.red(searchBgColor),
                Color.green(searchBgColor),
                Color.blue(searchBgColor));
        setBackgroundColor(color);
        invalidate();
    }

    @Override
    public float getAlphaValue() {
        return alpha;
    }

    @Override
    public void setAlphaInfo(AlphaInfo info) {
        this.alphaInfo = info;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        setAlpha(alpha);
        int color = Color.argb((int) (alpha * Color.alpha(searchBgColor)),
                Color.red(searchBgColor),
                Color.green(searchBgColor),
                Color.blue(searchBgColor));
        setBackgroundColor(color);
    }

    private void startAlphaAnimation() {
        if (searchViewBaseAnimator != null && !searchViewBaseAnimator.isRunning()) {
            searchViewBaseAnimator = searchViewBaseAnimator.reverse();
            searchViewBaseAnimator.addListener(new AlphaSupportAnimator.SimpleAlphaAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    super.onAnimationEnd();
                    searchViewBaseAnimator = null;
                }
            });
        } else if (searchViewBaseAnimator != null) {
            searchViewBaseAnimator.cancel();
            return;
        } else {
            searchViewBaseAnimator = ViewAnimationTools.createAlphaAnimator(this, 0f, 1f);
            searchViewBaseAnimator.addListener(new AlphaSupportAnimator.SimpleAlphaAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    super.onAnimationEnd();
                }
            });
        }
        searchViewBaseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        searchViewBaseAnimator.setDuration(animTime);
        searchViewBaseAnimator.start();
    }

    @Override
    public void onRevealAnimationStart() {
        this.mRunning = true;
    }

    @Override
    public void onRevealAnimationEnd() {
        this.mRunning = false;
        this.invalidate(this.targetBounds);
    }

    @Override
    public void onRevealAnimationCancel() {
        this.onRevealAnimationEnd();
    }

    @Override
    public void setRevealRadius(float radius) {
        this.radius = radius;
        this.invalidate(this.targetBounds);
    }

    @Override
    public float getRevealRadius() {
        return this.radius;
    }

    @Override
    public void attachRevealInfo(RevealInfo info) {
        info.getTarget().getHitRect(this.targetBounds);
        this.revealInfo = info;
    }

    @Override
    public SupportAnimator startReverseAnimation() {
        return this.revealInfo != null && this.revealInfo.hasTarget() && !this.mRunning ? ViewAnimationUtils.createCircularReveal(this.revealInfo.getTarget(), this.revealInfo.centerX, this.revealInfo.centerY, this.revealInfo.endRadius, this.revealInfo.startRadius) : null;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (this.mRunning && child == this.revealInfo.getTarget()) {
            int state = canvas.save();
            this.revealPath.reset();
            this.revealPath.addCircle((float) this.revealInfo.centerX, (float) this.revealInfo.centerY, this.radius, Path.Direction.CW);
            canvas.clipPath(this.revealPath);
            boolean isInvalided = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(state);
            return isInvalided;
        } else {
            return super.drawChild(canvas, child, drawingTime);
        }
    }

    private void startRevealAnim() {
        if (searchViewAnimator != null && !searchViewAnimator.isRunning()) {
            searchViewAnimator = searchViewAnimator.reverse();
            searchViewAnimator.addListener(new SupportAnimator.SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    super.onAnimationEnd();
                    searchViewAnimator = null;
                    setVisibility(GONE);
                }
            });
        } else if (searchViewAnimator != null) {
            searchViewAnimator.cancel();
            return;
        } else {
            final View myView = this;

            int cx = (int) (myView.getRight() - originX); //32dp
            int top = (int) (myView.getTop() + originY); //36dp
            float finalRadius = hypo(myView.getWidth(), myView.getHeight());

            searchViewAnimator = ViewAnimationUtils.createCircularReveal(myView, cx, top, 0, finalRadius);
            searchViewAnimator.addListener(new SupportAnimator.SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd() {
                    super.onAnimationEnd();
                }
            });
        }

        searchViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        searchViewAnimator.setDuration(animTime);
        searchViewAnimator.start();
    }

    public void showWithAnim() {
        setVisibility(VISIBLE);
        startAnimation();
    }

    public void hideWithAnim() {
        startAnimation();
    }

    private static float hypo(int a, int b) {
        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    private static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
