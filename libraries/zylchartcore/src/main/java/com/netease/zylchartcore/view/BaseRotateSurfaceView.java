package com.netease.zylchartcore.view;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.netease.zylchartcore.c.Constant;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shape.CoordinateSystem;
import com.netease.zylchartcore.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 6/3/16.
 */
public class BaseRotateSurfaceView extends BaseSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 120.0f / 320;//角度缩放比例

    public BaseRotateSurfaceView(Context context) {
        this(context, null);
    }

    public BaseRotateSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, null);
    }

    public BaseRotateSurfaceView(Context context, AttributeSet attrs, List<Shape> shapes) {
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
            incrYAngle(-distanceX * TOUCH_SCALE_FACTOR); //设置填充椭圆绕y轴旋转的角度
            incrXAngle(-distanceY * TOUCH_SCALE_FACTOR);//设置填充椭圆绕x轴旋转的角度

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