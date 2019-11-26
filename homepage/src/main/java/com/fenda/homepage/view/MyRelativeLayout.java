package com.fenda.homepage.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.view.MyNestedScrollView;
import com.fenda.homepage.BuildConfig;
import com.fenda.homepage.activity.HomePageActivity;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/11/5 14:30
 * @Description
 */
public class MyRelativeLayout extends RelativeLayout {

    private static final String TAG = "MyRelativeLayout";
    private  int MIX_SCROLL;

    private LauncherRecycleView launcherView;
    private LinearLayout skillView;
    private HomePageActivity pageActivity;
    private MyNestedScrollView scrollView;
    private PullView pullView;

    private float moveY;
    private int mShiftStart;
    private int mShiftRange = BaseApplication.getBaseInstance().getScreenHeight();
    private long mCurrentMillis;
    private float mVelocity;
    private final PointF mDownPos = new PointF();
    private final PointF mLastPos = new PointF();
    /**
     * The time constant used to calculate dampening in the low-pass filter of scroll velocity.
     * Cutoff frequency is set at 10 Hz.
     */
    public static final float SCROLL_VELOCITY_DAMPENING_RC = 1000f / (2f * (float) Math.PI * 10);
    private static final float ANIMATION_DURATION = 1200;
    private static final float FAST_FLING_PX_MS = 10;
    private static final float SCROLL_Y = (float) (BaseApplication.getBaseInstance().getScreenHeight() / 1.2);
    private int mActivePointerId;
    private long mAnimationDuration;


    private final ScrollInterpolator mScrollInterpolator = new ScrollInterpolator();
    private float shift;

    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MIX_SCROLL = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mDownPos.set(ev.getX(), ev.getY());
                mLastPos.set(mDownPos);
                if (skillView == null){
                    return false;
                }
                if (INVISIBLE == skillView.getVisibility()){
                    skillView.setVisibility(VISIBLE);
                    skillView.setTranslationY(BaseApplication.getBaseInstance().getScreenHeight());
                }
                break;
                case MotionEvent.ACTION_MOVE:
                    int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    float interMoveY  = VERTICAL.getDisplacement(ev, pointerIndex, mDownPos);
                    if(pageActivity == null){
                        return false;
                    }
                    if (Constant.Common.HOME_PAGE == pageActivity.getLauncherState()){
                        if (interMoveY < (-MIX_SCROLL)){
                            mShiftStart = BaseApplication.getBaseInstance().getScreenHeight();
                            //上滑
                            return true;
                        }
                    }else if (Constant.Common.ALL_SKILL == pageActivity.getLauncherState()){
                        boolean isTop = scrollView.isScrolledToTop();
                        if (isTop && interMoveY > MIX_SCROLL){
                            mShiftStart = 0;
                            return true;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    break;
        }

        return false;
    }



    public void setSkillAndLauncher(HomePageActivity pageActivity,LauncherRecycleView launcherView,LinearLayout skillView,MyNestedScrollView scrollView,PullView pullView){
        this.launcherView = launcherView;
        this.skillView = skillView;
        this.pageActivity = pageActivity;
        this.scrollView = scrollView;
        this.pullView = pullView;
//        this.linNest = linNest;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:


                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = event.findPointerIndex(0);
                moveY = VERTICAL.getDisplacement(event, pointerIndex, mDownPos);
                computeVelocity(VERTICAL.getDisplacement(event, pointerIndex, mLastPos),
                        event.getEventTime());
                pageActivity.stopCycleRollRunnable();
                shift = Math.min(Math.max(0, mShiftStart + moveY), mShiftRange);
                float progress = shift/mShiftRange;
                pullView.setProgress(progress);
                skillView.setTranslationY(shift);

                mLastPos.set(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_UP:

                if (Constant.Common.HOME_PAGE == pageActivity.getLauncherState()){
                    if (skillView.getTranslationY() > SCROLL_Y){
                        animateToWorkspace();
                    }else {
                        animateToAllApps();
                    }

                }else {
                    if (skillView.getTranslationY() > SCROLL_Y){
                        animateToAllApps();
                    }else {
                        animateToWorkspace();
                    }
                }

                    break;
            default:
                break;

        }

        return false;

    }


    /**
     * Computes the damped velocity.
     */
    public float computeVelocity(float delta, long currentMillis) {
        long previousMillis = mCurrentMillis;
        mCurrentMillis = currentMillis;

        float deltaTimeMillis = mCurrentMillis - previousMillis;
        float velocity = (deltaTimeMillis > 0) ? (delta / deltaTimeMillis) : 0;

        if (Math.abs(mVelocity) < 0.001f) {
            mVelocity = velocity;
        } else {
            float alpha = computeDampeningFactor(deltaTimeMillis);
            mVelocity = interpolate(mVelocity, velocity, alpha);
        }
        Log.i("TAG","velocity = "+velocity);
        return mVelocity;
    }


    /**
     * Returns a time-dependent dampening factor using delta time.
     */
    private static float computeDampeningFactor(float deltaTime) {
        return deltaTime / (SCROLL_VELOCITY_DAMPENING_RC + deltaTime);
    }


    /**
     * Returns the linear interpolation between two values
     */
    private static float interpolate(float from, float to, float alpha) {
        return (1.0f - alpha) * from + alpha * to;
    }


    public static final SwipeDetector.Direction VERTICAL = new SwipeDetector.Direction() {

        @Override
        float getDisplacement(MotionEvent ev, int pointerIndex, PointF refPoint) {
            return ev.getY() - refPoint.y;
        }

        @Override
        float getActiveTouchSlop(MotionEvent ev, int pointerIndex, PointF downPos) {
            return Math.abs(ev.getX(pointerIndex) - downPos.x);
        }
    };




    public  long calculateDuration(float velocity, float progressNeeded) {
        // TODO: make these values constants after tuning.
        float velocityDivisor = Math.max(2f, Math.abs(0.5f * velocity));
        float travelDistance = Math.max(0.2f, progressNeeded);
        long duration = (long) Math.max(100, ANIMATION_DURATION / velocityDivisor * travelDistance);
        if (BuildConfig.DEBUG){
            Log.d("TAG", String.format("calculateDuration=%d, v=%f, d=%f", duration, velocity, progressNeeded));
        }
        return duration;
    }


    /**
     * 显示技能页面
     */
    public void animateToAllApps() {
        mScrollInterpolator.setVelocityAtZero(Math.abs(mVelocity));
        mAnimationDuration = calculateDuration(mVelocity,Math.abs(skillView.getTranslationY()/mShiftRange));
        ValueAnimator driftAndAlpha = ValueAnimator.ofFloat( shift, 0f);
        driftAndAlpha.setDuration(mAnimationDuration);
        driftAndAlpha.setInterpolator(mScrollInterpolator);
        driftAndAlpha.start();
        driftAndAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                float progress = value/mShiftRange;
                pullView.setProgress(progress);
                skillView.setTranslationY(value);

            }
        });
        driftAndAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {

            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                pageActivity.setLauncherState(Constant.Common.ALL_SKILL);

            }
        });

    }


    /**
     * 显示主界面
     */
    public void animateToWorkspace() {
            mScrollInterpolator.setVelocityAtZero(Math.abs(mVelocity));
            mAnimationDuration = calculateDuration(mVelocity,Math.abs((mShiftRange - skillView.getTranslationY())/mShiftRange));
        ValueAnimator driftAndAlpha = ValueAnimator.ofFloat(shift, mShiftRange);
        driftAndAlpha.setDuration(mAnimationDuration);
        driftAndAlpha.setInterpolator(mScrollInterpolator);
        driftAndAlpha.start();
        driftAndAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                float progress = value/mShiftRange;
                pullView.setProgress(progress);
                skillView.setTranslationY(value);
            }
        });

        driftAndAlpha.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                pageActivity.setLauncherState(Constant.Common.HOME_PAGE);
                pageActivity.startCycleRollRunnable();
            }
        });
    }


    public static class ScrollInterpolator implements Interpolator {

        boolean mSteeper;

        public void setVelocityAtZero(float velocity) {
            mSteeper = velocity > FAST_FLING_PX_MS;
        }

        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            float output = t * t * t;
            if (mSteeper) {
                output *= t * t; // Make interpolation initial slope steeper
            }
            return output + 1;
        }
    }

}
