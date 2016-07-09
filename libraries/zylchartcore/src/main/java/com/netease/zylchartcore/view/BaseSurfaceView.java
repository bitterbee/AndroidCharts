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
public class BaseSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 120.0f / 320;//角度缩放比例
    private SceneRenderer mRenderer;//场景渲染器

    private List<Shape> mShapes = new ArrayList<>();
    private CoordinateSystem mCoordinateSystem = new CoordinateSystem();

    private float xAngle = 0;// 绕x轴旋转的角度
    private float yAngle = 0;// 绕y轴旋转的角度
    private float zAngle = 0;// 绕z轴旋转的角度

    private float mTouchScale = 1;// 缩放比例

    float mLightOffset = 0; //灯光的位置或方向的偏移量
    private boolean mIsShowCoordiateSystem = true;

    private GestureDetector mGestureDetector = null;
    private ScaleGestureDetector mScaleGestureDetector = null;

    public BaseSurfaceView(Context context) {
        this(context, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, null);
    }

    public BaseSurfaceView(Context context, AttributeSet attrs, List<Shape> shapes) {
        super(context, attrs);
        init(shapes);
    }

    private void init(List<Shape> shapes) {
        if (shapes != null) {
            mShapes = shapes;
        }

        this.setEGLContextClientVersion(2); //设置使用OPENGL ES2.0
        mRenderer = new SceneRenderer();    //创建场景渲染器
        setRenderer(mRenderer);             //设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//设置渲染模式为被动渲染

        mGestureDetector = new GestureDetector(this.getContext(), new GestureListener());
        mScaleGestureDetector = new ScaleGestureDetector(this.getContext(), new ScaleGestureListener());
    }

    public void addShape(Shape s) {
        mShapes.add(s);
        requestRender();
    }

    public void setShape(Shape s) {
        mShapes.clear();
        if (s != null) {
            mShapes.add(s);
        }
        requestRender();
    }

    public boolean isShowCoordiateSystem() {
        return mIsShowCoordiateSystem;
    }

    public void setShowCoordiateSystem(boolean showCoordiateSystem) {
        mIsShowCoordiateSystem = showCoordiateSystem;
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean gestureResult = mGestureDetector.onTouchEvent(e);
        boolean scaleResult = mScaleGestureDetector.onTouchEvent(e);

        requestRender();

        return gestureResult || scaleResult;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        public void onDrawFrame(GL10 gl) {
            //清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            //初始化光源位置
            MatrixState.setLightLocation(mLightOffset, 0, 1.5f);
            //保护现场
            MatrixState.pushMatrix();

            MatrixState.scale(mTouchScale);
            MatrixState.rotate(xAngle, 1, 0, 0);
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(zAngle, 0, 0, 1);

            if (mIsShowCoordiateSystem) {
                mCoordinateSystem.drawSelf();
            }

            for (Shape shape : mShapes) {
                MatrixState.pushMatrix();
                shape.drawSelf();
                MatrixState.popMatrix();
            }

            //恢复现场
            MatrixState.popMatrix();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            //计算GLSurfaceView的宽高比
            Constant.ratio = (float) width / height;
            // 调用此方法计算产生透视投影矩阵
            MatrixState.setProjectFrustum(-Constant.ratio, Constant.ratio, -1, 1, 20, 100);
            // 调用此方法产生摄像机9参数位置矩阵
            MatrixState.setCamera(0, 0f, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            //初始化变换矩阵
            MatrixState.setInitStack();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            for (Shape shape : mShapes) {
                shape.initInSurfaceViewCreated();
            }
            mCoordinateSystem.initInSurfaceViewCreated();

            //设置屏幕背景色RGBA
            GLES20.glClearColor(0f, 0f, 0f, 1.0f);
            //打开深度检测
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            //打开背面剪裁
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
    }

    public void setLightOffset(float lightOffset) {
        this.mLightOffset = lightOffset;
    }

    public float incrYAngle(float delta) {
        yAngle += delta;
        return yAngle;
    }

    public float incrXAngle(float delta) {
        xAngle += delta;
        return xAngle;
    }

    public float incrZAngle(float delta) {
        zAngle += delta;
        return zAngle;
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