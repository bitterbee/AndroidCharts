package com.netease.zylchartcore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.netease.zylchartcore.shape.Shape;

import java.util.List;

/**
 * Created by zyl06 on 7/10/16.
 */
public class BaseTranslateSurfaceView extends BaseSurfaceView {
    private final float TOUCH_SCALE_FACTOR = -1.0f / 400;//角度缩放比例

    public BaseTranslateSurfaceView(Context context) {
        this(context, null);
    }

    public BaseTranslateSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, null);
    }

    public BaseTranslateSurfaceView(Context context, AttributeSet attrs, List<Shape> shapes) {
        super(context, attrs, shapes);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(this.getContext(), new GestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleGestureListener());
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float mBeginScale = 1;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factor = detector.getScaleFactor();
            mTouchScale = mBeginScale * factor;
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mBeginScale = mTouchScale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            translate(distanceX * TOUCH_SCALE_FACTOR, -distanceY * TOUCH_SCALE_FACTOR, 0);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }
    }
}
