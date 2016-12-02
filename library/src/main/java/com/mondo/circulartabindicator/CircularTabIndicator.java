package com.mondo.circulartabindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * CircularTabIndicator
 * <p>
 * Create tab indicators which are update with currently selected tab in a view pager.
 * </p>
 */
public class CircularTabIndicator extends View {
    private final String TAG = getClass().getSimpleName();

    private ViewPager mViewPager;

    private int mCount;

    private Drawable[] mIndicators;
    private ShapeDrawable mSelectedDrawable;
    private ValueAnimator mAnimator;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private int mIndicatorSize = 35;
    private int mIndicatorMargin = 5;

    private int mSelectedColor = ContextCompat.getColor(getContext(),
            com.mondo.circulartabindicator.R.color
                    .indicator_default_selected_color);
    private int mNotSelectedColor = ContextCompat.getColor(getContext(),
            com.mondo.circulartabindicator.R.color
                    .indicator_default_not_selected_color);

    private int mLastPosition;

    public CircularTabIndicator(Context context) {
        super(context);
        init(null);
    }

    public CircularTabIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularTabIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setUpWithViewPager(ViewPager viewPager) {
        if (viewPager != null) {
            mViewPager = viewPager;
            mCount = viewPager.getAdapter().getCount();
            mLastPosition = mViewPager.getCurrentItem();
            mViewPager.addOnPageChangeListener(new OnPageChangeListener());
            setUp();
        }
    }

    public void setIndicatorSize(int size) {
        mIndicatorSize = size;
    }

    public void setIndicatorSizeResId(int sizeResId) {
        mIndicatorSize = getContext().getResources().getDimensionPixelSize(sizeResId);
    }

    public void setIndicatorMargin(int margin) {
        mIndicatorMargin = margin;
    }

    public void setIndicatorMarginResId(int marginResId) {
        mIndicatorMargin = getContext().getResources().getDimensionPixelSize(marginResId);
    }

    public void setColors(int selectedColor, int notSelectedColor) {
        mSelectedColor = selectedColor;
        mNotSelectedColor = notSelectedColor;
    }

    public void setColorsResId(int selectedColorResId, int notSelectedColor) {
        mSelectedColor = ContextCompat.getColor(getContext(), selectedColorResId);
        mNotSelectedColor = ContextCompat.getColor(getContext(), notSelectedColor);
    }

    public void setSelected(int position) {
        setSelected(position, true);
    }

    public void setSelected(int position, boolean smooth) {
        if (position > -1 && position < mIndicators.length) {
            Drawable oldDrawable = mIndicators[mLastPosition];
            Drawable newDrawable = mIndicators[position];

            mLastPosition = position;

            if (smooth) {
                Rect oldBounds = oldDrawable.getBounds();
                Rect newBounds = newDrawable.getBounds();

                mSelectedDrawable.setBounds(oldBounds);
                int oldLeft;
                int newLeft = newBounds.left;
                if (mAnimator != null && mAnimator.isRunning()) {
                    oldLeft = (int) mAnimator.getAnimatedValue();
                    mAnimator.cancel();
                } else {
                    oldLeft = oldBounds.left;
                }
                mAnimator = ValueAnimator.ofInt(oldLeft, newLeft);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(mInterpolator);
                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Rect bounds = mSelectedDrawable.getBounds();
                        bounds.left = (int) valueAnimator.getAnimatedValue();
                        mSelectedDrawable.setBounds(bounds);
                        postInvalidate();
                    }
                });
                mAnimator.start();
            } else {
                mSelectedDrawable.setBounds(newDrawable.getBounds());
                postInvalidate();
            }
        }
    }

    private void init(AttributeSet attrs) {
        Log.d(TAG, "attrs == null ? " + (attrs == null));
        if (attrs != null) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, com.mondo
                    .circulartabindicator.R.styleable
                    .CircularTabIndicator, 0, 0);
            try {
                mIndicatorSize = array.getDimensionPixelSize(
                        com.mondo.circulartabindicator.R.styleable
                                .CircularTabIndicator_indicatorSize,
                        getContext().getResources().getDimensionPixelSize(
                                com.mondo.circulartabindicator.R.dimen
                                        .indicator_default_size));

                mIndicatorMargin = array.getDimensionPixelSize(
                        com.mondo.circulartabindicator.R.styleable
                                .CircularTabIndicator_indicatorMargin,
                        getContext().getResources().getDimensionPixelSize(
                                com.mondo.circulartabindicator.R.dimen
                                        .indicator_default_margin));

                mSelectedColor = array.getColor(com.mondo.circulartabindicator.R.styleable
                                .CircularTabIndicator_indicatorSelectedColor,
                        ContextCompat.getColor(getContext(), com.mondo.circulartabindicator.R.color
                                .indicator_default_selected_color));
                mNotSelectedColor = array.getColor(com.mondo.circulartabindicator.R.styleable
                                .CircularTabIndicator_indicatorNotSelectedColor,
                        ContextCompat.getColor(getContext(), com.mondo.circulartabindicator.R.color
                                .indicator_default_not_selected_color));
            } finally {
                array.recycle();
            }
        } else {
            mIndicatorSize = getContext().getResources().getDimensionPixelSize(
                    com.mondo.circulartabindicator.R.dimen
                            .indicator_default_size);
            mIndicatorMargin = getContext().getResources().getDimensionPixelSize(
                    com.mondo.circulartabindicator.R.dimen
                            .indicator_default_margin);
            mSelectedColor = ContextCompat.getColor(getContext(),
                    com.mondo.circulartabindicator.R.color
                            .indicator_default_selected_color);
            mNotSelectedColor = ContextCompat.getColor(getContext(),
                    com.mondo.circulartabindicator.R.color
                            .indicator_default_not_selected_color);
        }
    }

    private void setUp() {
        mIndicators = new Drawable[mCount];

        int leftMargin = mIndicatorMargin / 2;

        mSelectedDrawable = new ShapeDrawable(new OvalShape());
        mSelectedDrawable.setIntrinsicWidth(mIndicatorSize);
        mSelectedDrawable.setIntrinsicHeight(mIndicatorSize);
        mSelectedDrawable.setBounds(leftMargin, mIndicatorMargin / 2, mIndicatorSize + leftMargin,
                mIndicatorMargin / 2 + mIndicatorSize);
        mSelectedDrawable.getPaint().setColor(mSelectedColor);

        for (int i = 0; i < mIndicators.length; i++) {
            int left = leftMargin;
            int right = mIndicatorSize + left;
            int top = mIndicatorMargin / 2;
            int bottom = mIndicatorMargin / 2 + mIndicatorSize;

            ShapeDrawable indicator = new ShapeDrawable(new OvalShape());
            indicator.setIntrinsicWidth(mIndicatorSize);
            indicator.setIntrinsicHeight(mIndicatorSize);
            indicator.getPaint().setColor(mNotSelectedColor);
            indicator.setBounds(left, top, right, bottom);

            if (mLastPosition == i) {
                mSelectedDrawable.setBounds(indicator.getBounds());
            }
            mIndicators[i] = indicator;

            leftMargin += mIndicatorSize + mIndicatorMargin;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIndicators != null) {
            int measuredHeight = mIndicatorSize + mIndicatorMargin;

            int measuredWidth = (mIndicatorSize + mIndicatorMargin) * mIndicators.length;

            setMeasuredDimension(measuredWidth, measuredHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicators != null) {
            for (Drawable drawable : mIndicators) {
                drawable.draw(canvas);
            }
            mSelectedDrawable.draw(canvas);
        }
    }

    private class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            setSelected(position);
        }
    }
}
