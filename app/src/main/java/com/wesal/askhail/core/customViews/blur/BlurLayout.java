package com.wesal.askhail.core.customViews.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.annotation.UiThread;

import com.wesal.askhail.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dimon.
 */

public class BlurLayout extends FrameLayout {

    private static final String TAG = BlurLayout.class.getSimpleName();

    public static int DEFAULT_BLUR_RADIUS = 5;
    public static float DEFAULT_SCALE_FACTOR = 5f;

    private static final int DEFAULT_RENDER_PERIOD = 33;

    private int mBlurRadius = DEFAULT_BLUR_RADIUS;
    private float mScaleFactor = DEFAULT_SCALE_FACTOR;
    private boolean mCropSubArea;
    private int mUpdateFrequency;

    private int mTargetViewId;
    private View mTargetView;
    private ImageView mBlurredImageView;

    private Timer mTimer;

    public BlurLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public BlurLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BlurLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public BlurLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BlurLayout, 0, 0);
            try {
                mBlurRadius = ta.getInteger(R.styleable.BlurLayout_blurRadius,
                        DEFAULT_BLUR_RADIUS);
                mScaleFactor = ta.getFloat(R.styleable.BlurLayout_scaleFactor,
                        DEFAULT_SCALE_FACTOR);
                mTargetViewId = ta.getResourceId(R.styleable.BlurLayout_targetView, 0);
                mCropSubArea = ta.getBoolean(R.styleable.BlurLayout_cropSubArea, false);
                mUpdateFrequency = ta.getInteger(R.styleable.BlurLayout_updateFrequency, 0);
            } finally {
                ta.recycle();
            }
        }
        mBlurredImageView = new ImageView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mBlurredImageView.setLayoutParams(params);
        mBlurredImageView.setClickable(false);
        mBlurredImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mBlurredImageView);
    }

    public void setBlurRadius(int blurRadius) {
        mBlurRadius = blurRadius;
    }

    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTargetViewId != 0) {
            mTargetView = getRootView().findViewById(mTargetViewId);
        }
        if (mTargetView == null) {
            Log.e(TAG, "target view is null");
            return;
        }
        if (mUpdateFrequency > 0) {
            start();
        } else {
            takeFirstFrame();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
        getHandler().removeCallbacksAndMessages(null);
    }

    private synchronized void start() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                render();
            }
        }, 0, mUpdateFrequency);
    }

    private synchronized void stop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void takeFirstFrame() {
        mTargetView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mTargetView.getHeight() > 0) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mTargetView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mTargetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    render();
                }
            }
        });
    }

    @UiThread
    private void invalidateBlurFrame(final Bitmap bitmap) {
        if (getHandler() != null) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mBlurredImageView.setImageBitmap(bitmap);
                }
            });
        }
    }

    private void render() {
        Bitmap bitmap = obtainBitmap(mTargetView);
        if (bitmap != null) {
            bitmap = scaleBitmap(bitmap);
            bitmap = BlurUtils.getBlurBitmap(bitmap, mBlurRadius);
            invalidateBlurFrame(bitmap);
        }
    }

    private Bitmap obtainBitmap(View mView) {
        if (mView.getHeight() == 0) {
            return null;
        }
        Bitmap bitmap;
        if (mView instanceof TextureView) {
            TextureView textureView = (TextureView) mView;
            bitmap = textureView.getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(
                    mView.getWidth(),
                    mView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            mView.draw(c);
        }

        if (mCropSubArea) {
            bitmap = cropBitmap(bitmap);
        }
        return bitmap;
    }

    private Bitmap scaleBitmap(Bitmap myBitmap) {
        int width = (int) (myBitmap.getWidth() / mScaleFactor);
        int height = (int) (myBitmap.getHeight() / mScaleFactor);
        return Bitmap.createScaledBitmap(myBitmap, width, height, false);
    }

    private Bitmap cropBitmap(Bitmap bitmap) {
        int[] coords = new int[2];
        getLocationInWindow(coords);
        if (coords[0] + getWidth() > bitmap.getWidth() || coords[1] + getHeight() > bitmap.getHeight()) {
            Log.e(TAG, "crop error " + (coords[0] + getWidth()) + " <= " + bitmap.getWidth() + " " + coords[1] + getHeight() + " <= " + getHeight());
            return bitmap;
        }
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, coords[0], coords[1], getWidth(), getHeight());
        bitmap.recycle();
        return croppedBitmap;
    }
}