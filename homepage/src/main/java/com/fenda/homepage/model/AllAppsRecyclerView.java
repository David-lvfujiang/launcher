/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fenda.homepage.model;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.util.SparseIntArray;
import android.view.MotionEvent;


import com.fenda.common.BaseApplication;

/**
 * A RecyclerView with custom fast scroll support for the all apps view.
 */
public class AllAppsRecyclerView extends BaseRecyclerView {


    // The specific view heights that we use to calculate scroll
    private SparseIntArray mCachedScrollPositions = new SparseIntArray();

    // The empty-search result background
    private OverScrollHelper mOverScrollHelper;
    private SwipeDetector mPullDetector;

    private float mContentTranslationY = 0;
    public static final Property<AllAppsRecyclerView, Float> CONTENT_TRANS_Y =
            new Property<AllAppsRecyclerView, Float>(Float.class, "appsRecyclerViewContentTransY") {
                @Override
                public Float get(AllAppsRecyclerView allAppsRecyclerView) {
                    return allAppsRecyclerView.getContentTranslationY();
                }

                @Override
                public void set(AllAppsRecyclerView allAppsRecyclerView, Float y) {
                    allAppsRecyclerView.setContentTranslationY(y);
                }
            };

    public AllAppsRecyclerView(Context context) {
        this(context, null);
    }

    public AllAppsRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AllAppsRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AllAppsRecyclerView(Context context, AttributeSet attrs, int defStyleAttr,
                               int defStyleRes) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        addOnItemTouchListener(this);

        mOverScrollHelper = new OverScrollHelper();
        mPullDetector = new SwipeDetector(getContext(), mOverScrollHelper, SwipeDetector.VERTICAL);
        mPullDetector.setDetectableScrollConditions(SwipeDetector.DIRECTION_BOTH, true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.e("TAG","拉取滑动 == 》onTouchEvent = "+mPullDetector);
        mPullDetector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }




    /**
     * Scrolls this recycler view to the top.
     */
    public void scrollToTop() {
        // Ensure we reattach the scrollbar if it was previously detached while fast-scrolling
        scrollToPosition(0);
    }

    @Override
    public void onDraw(Canvas c) {

        super.onDraw(c);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.translate(0, mContentTranslationY);
        super.dispatchDraw(canvas);
        canvas.translate(0, -mContentTranslationY);
    }

    public float getContentTranslationY() {
        return mContentTranslationY;
    }

    /**
     * Use this method instead of calling {@link #setTranslationY(float)}} directly to avoid drawing
     * on top of other Views.
     */
    public void setContentTranslationY(float y) {
        mContentTranslationY = y;
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateEmptySearchBackgroundBounds();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        mPullDetector.onTouchEvent(e);
        boolean result = super.onInterceptTouchEvent(e) || mOverScrollHelper.isInOverScroll();
        return result;
    }



    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mCachedScrollPositions.clear();
            }
        });
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        // No bottom fading edge.
        return 0;
    }

    @Override
    protected boolean isPaddingOffsetRequired() {
        return true;
    }

    @Override
    protected int getTopPaddingOffset() {
        return -getPaddingTop();
    }

    /**
     * Updates the bounds for the scrollbar.
     */
    @Override
    public void onUpdateScrollbar(int dy) {
    }

    @Override
    public boolean supportsFastScrolling() {
        // Only allow fast scrolling when the user is not searching, since the results are not
        // grouped in a meaningful order
        return false;
    }

    @Override
    public int getCurrentScrollY() {
        return 0;
    }

    @Override
    public String scrollToPositionAtProgress(float touchFraction) {
        return null;
    }

    /**
     * Returns the available scroll height:
     *   AvailableScrollHeight = Total height of the all items - last page height
     */
    @Override
    protected int getAvailableScrollHeight() {
        return BaseApplication.getBaseInstance().getScreenHeight();
    }

    /**
     * Updates the bounds of the empty search background.
     */
    private void updateEmptySearchBackgroundBounds() {

    }

    private class SpringMotionOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOverScrollHelper.isInOverScroll()) {
                // OverScroll will handle animating the springs.
                return;
            }

            // We only start the spring animation when we hit the top/bottom, to ensure
            // that all of the animations start at the same time.
            if (dy < 0 && !canScrollVertically(-1)) {
            } else if (dy > 0 && !canScrollVertically(1)) {
            }
        }
    }

    private class OverScrollHelper implements SwipeDetector.Listener {

        private static final float MAX_RELEASE_VELOCITY = 5000; // px / s
        private static final float MAX_OVERSCROLL_PERCENTAGE = 0.07f;

        private boolean mIsInOverScroll;

        // We use this value to calculate the actual amount the user has overscrolled.
        private float mFirstDisplacement = 0;

        private boolean mAlreadyScrollingUp;
        private int mFirstScrollYOnScrollUp;

        @Override
        public void onDragStart(boolean start) {
        }

        @Override
        public boolean onDrag(float displacement, float velocity) {
            boolean isScrollingUp = displacement > 0;
            if (isScrollingUp) {
                if (!mAlreadyScrollingUp) {
                    mFirstScrollYOnScrollUp = getCurrentScrollY();
                    mAlreadyScrollingUp = true;
                }
            } else {
                mAlreadyScrollingUp = false;
            }

            // Only enter overscroll if the user is interacting with the RecyclerView directly
            // and if one of the following criteria are met:
            // - User scrolls down when they're already at the bottom.
            // - User starts scrolling up, hits the top, and continues scrolling up.
            boolean wasInOverScroll = mIsInOverScroll;
            mIsInOverScroll =  ((!canScrollVertically(1) && displacement < 0) ||
                    (!canScrollVertically(-1) && isScrollingUp && mFirstScrollYOnScrollUp != 0));

            if (wasInOverScroll && !mIsInOverScroll) {
                // Exit overscroll. This can happen when the user is in overscroll and then
                // scrolls the opposite way.
                reset(false /* shouldSpring */);
            } else if (mIsInOverScroll) {
                if (Float.compare(mFirstDisplacement, 0) == 0) {
                    // Because users can scroll before entering overscroll, we need to
                    // subtract the amount where the user was not in overscroll.
                    mFirstDisplacement = displacement;
                }
                float overscrollY = displacement - mFirstDisplacement;
                setContentTranslationY(getDampedOverScroll(overscrollY));
            }

            return mIsInOverScroll;
        }

        @Override
        public void onDragEnd(float velocity, boolean fling) {
           reset(mIsInOverScroll  /* shouldSpring */);
        }

        private void reset(boolean shouldSpring) {
            float y = getContentTranslationY();
            if (Float.compare(y, 0) != 0) {

                ObjectAnimator.ofFloat(AllAppsRecyclerView.this,
                        AllAppsRecyclerView.CONTENT_TRANS_Y, 0)
                        .setDuration(100)
                        .start();
            }
            mIsInOverScroll = false;
            mFirstDisplacement = 0;
            mFirstScrollYOnScrollUp = 0;
            mAlreadyScrollingUp = false;
        }

        public boolean isInOverScroll() {
            return mIsInOverScroll;
        }

        private float getDampedOverScroll(float y) {
            return OverScroll.dampedScroll(y, getHeight());
        }
    }
}
