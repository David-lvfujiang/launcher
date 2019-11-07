/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * A ViewGroup that coordinates dragging across its descendants
 */
public class DragLayer extends RelativeLayout {

    public static final int ANIMATION_END_DISAPPEAR = 0;
    public static final int ANIMATION_END_REMAIN_VISIBLE = 2;

    private final int[] mTmpXY = new int[2];

//    DragController mDragController;


    // Variables relating to animation of views after drop
    private ValueAnimator mDropAnim = null;
    private final TimeInterpolator mCubicEaseOutInterpolator = new DecelerateInterpolator(1.5f);
    int mAnchorViewInitialScrollX = 0;
    View mAnchorView = null;

    private boolean mHoverPointClosesFolder = false;
    private final Rect mHitRect = new Rect();
    private final Rect mHighlightRect = new Rect();

    private TouchCompleteListener mTouchCompleteListener;

    private int mTopViewIndex;
    private int mChildCountOnLastUpdate = -1;

    // Darkening scrim
    private float mBackgroundAlpha = 0;

    // Related to adjacent page hints

    // Handles all apps pull up interaction
    private AllAppsTransitionController mAllAppsController;

    private TouchController mActiveController;
    /**
     * Used to create a new DragLayer from XML.
     *
     * @param context The application's context.
     * @param attrs The attributes set containing the Workspace's customization values.
     */
    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Disable multitouch across the workspace/all apps/customize tray
        setMotionEventSplittingEnabled(false);
        setChildrenDrawingOrderEnabled(true);
    }

    public void setup(
            AllAppsTransitionController allAppsTransitionController) {
        mAllAppsController = allAppsTransitionController;
    }




    public boolean isEventOverView(View view, MotionEvent ev) {
        return mHitRect.contains((int) ev.getX(), (int) ev.getY());
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

         if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (mTouchCompleteListener != null) {
                mTouchCompleteListener.onTouchComplete();
            }
            mTouchCompleteListener = null;
        }
        mActiveController = null;

        //判断拦截


        if (mAllAppsController.onControllerInterceptTouchEvent(ev)) {
            mActiveController = mAllAppsController;
            return true;
        }


        return false;
    }





    @Override
    public boolean onHoverEvent(MotionEvent ev) {
        // If we've received this, we've already done the necessary handling
        // in onInterceptHoverEvent. Return true to consume the event.
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

         if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (mTouchCompleteListener != null) {
                mTouchCompleteListener.onTouchComplete();
            }
            mTouchCompleteListener = null;
        }

        if (mActiveController != null) {
            //上滑下拉 逻辑开始
            return mActiveController.onControllerTouchEvent(ev);
        }
        return false;
    }




    public void getViewRectRelativeToSelf(View v, Rect r) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        int x = loc[0];
        int y = loc[1];

        v.getLocationInWindow(loc);
        int vX = loc[0];
        int vY = loc[1];

        int left = vX - x;
        int top = vY - y;
        r.set(left, top, left + v.getMeasuredWidth(), top + v.getMeasuredHeight());
    }






















    public void setBackgroundAlpha(float alpha) {
        if (alpha != mBackgroundAlpha) {
            mBackgroundAlpha = alpha;
            invalidate();
        }
    }

    public float getBackgroundAlpha() {
        return mBackgroundAlpha;
    }



    public void setTouchCompleteListener(TouchCompleteListener listener) {
        mTouchCompleteListener = listener;
    }

    public interface TouchCompleteListener {
        public void onTouchComplete();
    }
}
