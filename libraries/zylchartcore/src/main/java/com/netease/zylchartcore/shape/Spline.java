package com.netease.zylchartcore.shape;

import android.graphics.Color;
import android.opengl.GLES20;

import com.netease.zylchartcore.ChartsCore;
import com.netease.zylchartcore.c.Constant;
import com.netease.zylchartcore.data.ControlPoint3;
import com.netease.zylchartcore.data.Point3;
import com.netease.zylchartcore.data.operator.PointUtil;
import com.netease.zylchartcore.data.operator.VectorUtil;
import com.netease.zylchartcore.matrix.MatrixState;
import com.netease.zylchartcore.shader.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 7/9/16.
 */
public class Spline extends Shape {
    private Ball mWhiteBall = null;  // 初始值
    private Ball mGreenBall = null;  // 被直接设置的点
    private Ball mYellowBall = null; // 被影响到的点
    private boolean mIsShowControlPoints = false;

    private ControlPoint3[] mCP;

    private int mSplineMode = SplineMode.SPLMODE_SPLINE;
    private boolean mIsNormalSpline = false;

    private List<Point3> mAllPoints = new ArrayList<>();
    private Point3[] mTangents; // 切向向量
    private Float[] mTangentMagnitudes0; //一个控制点 i 有 2 个切向值，(i-1, i)，(i, i+1)
    private Float[] mTangentMagnitudes1; //

    public Spline(List<ControlPoint3> ctrlPoints, boolean isShowControlPoints, int splineMode) {
        this.mIsShowControlPoints = isShowControlPoints;
        this.mSplineMode = splineMode;
        setPoints(ctrlPoints);
    }

    public void setPoints(List<ControlPoint3> points) {
        if (mWhiteBall == null) {
            mWhiteBall = new Ball(Point3.ORIGIN3, 0.01f, 30, Color.WHITE);
        }
        if (mGreenBall == null) {
            mGreenBall = new Ball(Point3.ORIGIN3, 0.01f, 30, Color.GREEN);
        }
        if (mYellowBall == null) {
            mYellowBall = new Ball(Point3.ORIGIN3, 0.01f, 30, Color.YELLOW);
        }
        if (points != null) {
            mCP = new ControlPoint3[points.size()];
            points.toArray(mCP);

            updateInterpoints();

            initVertexData();
        }
    }

    private void updateInterpoints() {
        mAllPoints.clear();

        int cptCount = mCP.length;
        if (cptCount == 0)
            return;

        //DiscreteSpline(spline, thresholdAngle, linePoint);
        if (cptCount == 2) {
            mAllPoints.add(mCP[0]);
            mAllPoints.add(mCP[1]);
        } else {
            int clineCount = cptCount - 1;
            if (mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE) {
                clineCount = cptCount;
            }
            float length = 0.0f;
            for (int i = 0; i < clineCount; i++) {
                length = length + mCP[i].sub(mCP[(i + 1) % cptCount]).magnitude();
            }
            mAllPoints.add(mCP[0]);
            for (int i = 0; i < clineCount; i++) {
                float clineLength = mCP[i].sub(mCP[(i + 1) % cptCount]).magnitude();
                int divCount = (int) (clineLength / length * clineCount * 10 + 3);
                float curLength = mCP[i].sub(mCP[(i + 1) % cptCount]).magnitude();
                float ratio = 1.0f;
                if (i < clineCount - 1) {
                    float nextLength = mCP[(i + 1) % cptCount].sub(mCP[(i + 2) % cptCount]).magnitude();
                    float ratioTemp = curLength / nextLength;
                    if (ratioTemp < 0.9) {
                        ratio = ratioTemp;
                    }
                } else {
                    float nextLength = mCP[0].sub(mCP[1]).magnitude();
                    float ratioTemp = curLength / nextLength;
                    if (ratioTemp < 0.9) {
                        ratio = ratioTemp;
                    }
                }
                if (i > 0) {
                    float preLength = mCP[i - 1].sub(mCP[i]).magnitude();
                    float ratioTemp = curLength / preLength;
                    if (ratioTemp < 0.9 && ratioTemp < ratio) {
                        ratio = ratioTemp;
                    }
                } else {
                    float preLength = mCP[cptCount - 1].sub(mCP[0]).magnitude();
                    float ratioTemp = curLength / preLength;
                    if (ratioTemp < 0.9 && ratioTemp < ratio) {
                        ratio = ratioTemp;
                    }
                }
                float tension1 = 1 - (float) Math.sqrt(Math.sin(0.5 * Math.PI * ratio));
                float ang = (float) (Math.PI * 176.0 / 180.0);
                if (i < clineCount - 1) {
                    float angTemp = VectorUtil.getAngleOf2Vector(mCP[i].sub(mCP[i + 1]), mCP[(i + 2) % cptCount].sub(mCP[i + 1]));
                    if (angTemp < ang) {
                        ang = angTemp;
                    }
                } else {
                    if (mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE) {
                        float angTemp = VectorUtil.getAngleOf2Vector(mCP[cptCount - 1].sub(mCP[0]), mCP[1].sub(mCP[0]));
                        if (angTemp < ang) {
                            ang = angTemp;
                        }
                    }
                }
                if (i > 0) {
                    float angTemp = VectorUtil.getAngleOf2Vector(mCP[i - 1].sub(mCP[i]), mCP[(i + 1) % cptCount].sub(mCP[i]));
                    if (angTemp < ang) {
                        ang = angTemp;
                    }
                } else {
                    if (mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE) {
                        float angTemp = VectorUtil.getAngleOf2Vector(mCP[cptCount - 1].sub(mCP[0]), mCP[1].sub(mCP[0]));
                        if (angTemp < ang) {
                            ang = angTemp;
                        }
                    }
                }
                float tension2 = 1 - (float) Math.sqrt(Math.sin(0.5 * ang));
                float tension = tension1 > tension2 ? tension1 : tension2;
                for (int k = 1; k <= divCount; k++) {
                    mAllPoints.add(getInterpoint(i, 1.0f * k / divCount, tension));
                }
            }
        }
    }


    private Point3 getInterpoint(int i, float t) {
        return getInterpoint(i, t, 0.0f);
    }

    /**
     * @param i
     * @param t
     * @param tension
     * @return
     */
    private Point3 getInterpoint(int i, float t, float tension) {
        Point3 v = new Point3(0, 0, 0);
        final int size = mCP.length;

        int tangentSize = (mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE) ? size + 1 : size;
        mTangents = new Point3[tangentSize];
        mTangentMagnitudes0 = new Float[tangentSize];
        mTangentMagnitudes1 = new Float[tangentSize];

        if (!mIsNormalSpline) {
            if (mSplineMode == SplineMode.SPLMODE_SPLINE) {
                if (PointUtil.distance(mCP[i + 1], mCP[i]) < Constant.EPSILON) {
                    v = mCP[i];
                } else {
                    Point3 pd0, pd1;
                    if (size > 2) {
                        if (i == 0) {
//                            pd0 = (1 - tension) * ((mCP[1] + (mCP[1] - (mCP[0] + mCP[2]) * 0.5) * 0.5) - mCP[0]);
                            pd0 = (mCP[1].add(mCP[1].sub(mCP[0].middle(mCP[2])).multiple(0.5f)).sub(mCP[0])).multiple((1 - tension));
                        } else {
//                            pd0 = (1 - tension) * ((mCP[i] - mCP[i-1]) + (mCP[i+1] - mCP[i])) * 0.5f;
                            pd0 = (mCP[i].sub(mCP[i - 1].add(mCP[i + 1].sub(mCP[i])))).multiple((1 - tension)).multiple(0.5f);
                        }

                        if ((i + 2) >= size) {
//                            pd1 = (1 - tension) * (mCP[i+1] - (mCP[i] + (mCP[i] - (mCP[i-1] + mCP[i+1]) * 0.5) * 0.5f));
                            pd1 = mCP[i + 1].sub(mCP[i].add(mCP[i].sub(mCP[i - 1].middle(mCP[i + 1])).multiple(0.5f))).multiple(1 - tension);
                        } else {
//                            pd1 = (1 - tension) * ((mCP[i+1] - mCP[i]) + (mCP[i+2] - mCP[i+1])) * 0.5f;
                            pd1 = mCP[i + 1].sub(mCP[i]).add(mCP[i + 2].sub(mCP[i + 1])).multiple(0.5f).multiple(1 - tension);
                        }

                        mTangents[i] = pd0.normalize();
                        mTangents[i + 1] = pd1.normalize();
                        mTangentMagnitudes1[i] = mTangentMagnitudes0[i] = pd0.magnitude();
                        mTangentMagnitudes1[i + 1] = mTangentMagnitudes0[i + 1] = pd1.magnitude();

                        v = mCP[i].multiple((t - 1) * (t - 1) * (2 * t + 1)).add(
                                mCP[i + 1].multiple(t * t * (3 - 2 * t)).add(
                                        pd0.multiple((1 - t) * (1 - t) * t).add(
                                                pd1.multiple((t - 1) * t * t))));
                    } else {
                        mTangents[i + 1] = mTangents[i] = mCP[i + 1].sub(mCP[i]).normalize();
                        mTangentMagnitudes0[i + 1] = mTangentMagnitudes1[i + 1] =
                                mTangentMagnitudes0[i] = mTangentMagnitudes1[i] = mCP[i + 1].sub(mCP[i]).magnitude();
                    }
                }
            } else if (mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE) {
                int pim1 = (i - 1 < 0) ? (size - 1) : (i - 1);
                int pi = (i >= size) ? (i - size) : i;
                int pip1 = i + 1 >= size ? (i + 1 - size) : (i + 1);
                int pip2 = (i + 2 >= size) ? (i + 2 - size) : (i + 2);

                if (mCP[pip1].sub(mCP[pi]).magnitude() < Constant.EPSILON) {
                    v = mCP[i];
                } else {
//                    Point3 pd0 = (1 - tension) * ((mCP[pi] - mCP[pim1]) + (mCP[pip1] - mCP[pi])) * 0.5f;
                    Point3 pd0 = mCP[pi].sub(mCP[pim1]).add(mCP[pip1].sub(mCP[pi])).multiple(0.5f).multiple(1 - tension);
//                    Point3 pd1 = (1 - tension) * ((mCP[pip1] - mCP[pi]) + (mCP[pip2] - mCP[pip1])) * 0.5f;;
                    Point3 pd1 = mCP[pip1].sub(mCP[pi]).add(mCP[pip2].sub(mCP[pip1])).multiple(0.5f).multiple(1 - tension);

                    if (i < size) {
                        mTangents[i + 1] = pd1.normalize();
                        mTangentMagnitudes0[i + 1] = mTangentMagnitudes1[i + 1] = pd1.magnitude();
                    }

                    v = mCP[pi].multiple((t - 1) * (t - 1) * (2 * t + 1)).add(
                            mCP[pip1].multiple(t * t * (3 - 2 * t)).add(
                                    pd0.multiple((1 - t) * (1 - t) * t).add(
                                            pd1.multiple((t - 1) * t * t))));
                }
            } else if (mSplineMode == SplineMode.SPLMODE_BSPLINE) {
                float f = 0.5f * (1.0f - t) * (1.0f - t);
                float g = (1.0f - t) * t + 0.5f;
                float h = 0.5f * t * t;
                int index0 = (i - 1 < 0) ? 0 : i - 1;
                int index1 = i;
                int index2 = (i + 1) > (size - 1) ? (size - 1) : (i + 1);

                v.x = mCP[index0].x * f + mCP[index1].x * g + mCP[index2].x * h;
                v.y = mCP[index0].y * f + mCP[index1].y * g + mCP[index2].y * h;
                v.z = mCP[index0].z * f + mCP[index1].z * g + mCP[index2].z * h;
            } else if (mSplineMode == SplineMode.SPLMODE_CLOSED_BSPLINE) {
                float f = 0.5f * (1.0f - t) * (1.0f - t);
                float g = (1.0f - t) * t + 0.5f;
                float h = 0.5f * t * t;
                int index0 = i - 1;
                int index1 = i;
                int index2 = i + 1;
                if (index0 < 0) index0 = size - 1;
                if (index2 >= size) index2 = 0;
                v.x = mCP[index0].x * f + mCP[index1].x * g + mCP[index2].x * h;
                v.y = mCP[index0].y * f + mCP[index1].y * g + mCP[index2].y * h;
                v.z = mCP[index0].z * f + mCP[index1].z * g + mCP[index2].z * h;
            } else if (mSplineMode == SplineMode.SPLMODE_BEZIER) {
                v.x = 0.0f;
                v.y = 0.0f;
                v.z = 0.0f;
                int n = size - 1;
                for (int j = 0; j < size; j++) {
                    v.x += comb(n, j) * Math.pow(t, j) * Math.pow((1 - t), (n - j)) * mCP[j].x;
                    v.y += comb(n, j) * Math.pow(t, j) * Math.pow((1 - t), (n - j)) * mCP[j].y;
                    v.z += comb(n, j) * Math.pow(t, j) * Math.pow((1 - t), (n - j)) * mCP[j].z;
                }
            } else if (mSplineMode == SplineMode.SPLMODE_LINEAR) {
                v = mCP[i].multiple(1.0f - t).add(mCP[i + 1].multiple(t));
            }
        } else { // tangent mode
            if (size > 2) {
                int pip1 = (i + 1 > size) ? (i + 1 - size) : (i + 1);
                v = mCP[i].multiple((t - 1) * (t - 1) * (2 * t + 1)).add(
                        mCP[pip1].multiple(t * t * (3 - 2 * t)).add(
                                mTangents[i].multiple((1 - t) * (1 - t) * t * mTangentMagnitudes1[i]).add(
                                        mTangents[pip1].multiple((t - 1) * t * t * mTangentMagnitudes0[pip1]))));
            } else {
                if (i < 1) {
//                    v = (1.0f - t) * mCP[i] + t * mCP[i + 1];
                    v = mCP[i].multiple(1.0f - t).add(mCP[i + 1].multiple(t));
                }
            }
        }

        return v;
    }

    private float comb(int x, int y) {
        float a = 1;
        float b = 1;
        for (int i = 2; i <= x - y; i++) {
            a *= i;
        }
        for (int i = y + 1; i <= x; i++) {
            b *= i;
        }
        return b / a;
    }

    private void initVertexData() {
        List<Point3> cpList = new ArrayList<>(mCP.length);
        for (Point3 cp : mCP) {
            cpList.add(cp);
        }
        doInitVertexData();
    }

    private void doInitVertexData() {
        int vertexCount = mAllPoints.size();
        float[] vertices = new float[3 * vertexCount];
        int count = 0;
        for (int i = 0; i < vertexCount; i++) {
            Point3 location = mAllPoints.get(i);
            vertices[count++] = location.x;
            vertices[count++] = location.y;
            vertices[count++] = location.z;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    private void initShader() {
        mVertexShader = ShaderUtil.loadFromAssetsFile("line_vertex_1.glsl", ChartsCore.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("line_fragment_1.glsl", ChartsCore.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
    }

    @Override
    protected void onInitInSurfaceViewCreated() {
        mWhiteBall.initInSurfaceViewCreated();
        mGreenBall.initInSurfaceViewCreated();
        mYellowBall.initInSurfaceViewCreated();

        initVertexData();
        initShader();
    }

    @Override
    protected void onDrawSelf() {
        if (mIsShowControlPoints) {
            int size = mCP.length;
            for (int i = 0; i < size; i++) {
                ControlPoint3 controlPoint = mCP[i];

                MatrixState.pushMatrix();
                MatrixState.translate(controlPoint.x, controlPoint.y, controlPoint.z);
                if (controlPoint.color == Color.GREEN) {
                    mGreenBall.drawSelf();
                } else if (controlPoint.color == Color.YELLOW) {
                    mYellowBall.drawSelf();
                } else {
                    mWhiteBall.drawSelf();
                }
                MatrixState.popMatrix();
            }
        }

        GLES20.glUseProgram(mProgram);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        GLES20.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
        GLES20.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);

        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandle);

        GLES20.glLineWidth(1.0f);

        int lineMode = isClosed() ? GLES20.GL_LINE_LOOP : GLES20.GL_LINE_STRIP;
        GLES20.glDrawArrays(lineMode, 0, mAllPoints.size());
    }

    private boolean isClosed() {
        return mSplineMode == SplineMode.SPLMODE_CLOSED_BSPLINE ||
                mSplineMode == SplineMode.SPLMODE_CLOSED_SPLINE ||
                mSplineMode == SplineMode.SPLMODE_CLOSED_BEZIER;
    }
}
