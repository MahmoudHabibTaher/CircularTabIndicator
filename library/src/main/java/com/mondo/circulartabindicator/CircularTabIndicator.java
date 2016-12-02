package com.mondo.circulartabindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * CircularTabIndicator
 * <p>
 * Create tab indicators which are update with currently selected tab in a view pager.
 */
public class CircularTabIndicator extends View {
    private static final int[] SELECTED_STATES = {android.R.attr.state_selected};
    private static final int[] NOT_SELECTED_STATES = {-android.R.attr.stateNotNeeded};

    private final String TAG = getClass().getSimpleName();

    private ViewPager mViewPager;

    private int mCount;

    private Drawable[] mIndicators;

    private int mIndicatorSize = 35;
    private int mIndicatorMargin = 5;

    private int mSelectedColor = ContextCompat.getColor(getContext(),
            com.mondo.circulartabindicator.R.color
                    .indicator_default_selected_color);
    private int mNotSelectedColor = ContextCompat.getColor(getContext(),
            com.mondo.circulartabindicator.R.color
                    .indicator_default_not_selected_color);

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

        for (int i = 0; i < mIndicators.length; i++) {
            ShapeDrawable selectedDrawable = new ShapeDrawable(new OvalShape());
            selectedDrawable.setIntrinsicWidth(mIndicatorSize);
            selectedDrawable.setIntrinsicHeight(mIndicatorSize);
            selectedDrawable.getPaint().setColor(mSelectedColor);

            ShapeDrawable notSelectedDrawable = new ShapeDrawable(new OvalShape());
            selectedDrawable.setIntrinsicWidth(mIndicatorSize);
            selectedDrawable.setIntrinsicHeight(mIndicatorSize);
            notSelectedDrawable.getPaint().setColor(mNotSelectedColor);

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(SELECTED_STATES, selectedDrawable);
            stateListDrawable.addState(NOT_SELECTED_STATES, notSelectedDrawable);

            int left = leftMargin;
            int right = mIndicatorSize + left;
            int top = mIndicatorMargin / 2;
            int bottom = mIndicatorMargin / 2 + mIndicatorSize;

            stateListDrawable.setBounds(left, top, right, bottom);
            if (mViewPager.getCurrentItem() == i) {
                stateListDrawable.setState(SELECTED_STATES);
            }
            mIndicators[i] = stateListDrawable;

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
        }
    }

    private class OnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        private int mLastPosition = mViewPager.getCurrentItem();

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mIndicators[mLastPosition].setState(NOT_SELECTED_STATES);
            mIndicators[position].setState(SELECTED_STATES);
            mLastPosition = position;
            invalidate();
        }
    }
}
