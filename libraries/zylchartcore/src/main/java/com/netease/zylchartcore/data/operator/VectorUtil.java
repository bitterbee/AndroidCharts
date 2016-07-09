package com.netease.zylchartcore.data.operator;

import com.netease.zylchartcore.c.Constant;
import com.netease.zylchartcore.data.Point3;

/**
 * Created by zyl06 on 6/6/16.
 */
public class VectorUtil {

    public static float[] calConeNormal(
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            float x2, float y2, float z2) {

        float[] a = {x1 - x0, y1 - y0, z1 - z0};
        float[] b = {x2 - x0, y2 - y0, z2 - z0};
        float[] c = {x2 - x1, y2 - y1, z2 - z1};

        float[] k = crossTwoVectors(a, b);
        float[] d = crossTwoVectors(c, k);
        return normalizeVector(d);
    }

    public static float[] normalizeVector(float[] vec) {
        float mod = module(vec);
        return new float[]{vec[0] / mod, vec[1] / mod, vec[2] / mod};
    }

    public static float module(float[] vec) {
        return (float) Math.sqrt(vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]);
    }

    public static float[] crossTwoVectors(float[] a, float[] b) {
        float x = a[1] * b[2] - a[2] * b[1];
        float y = a[2] * b[0] - a[0] * b[2];
        float z = a[0] * b[1] - a[1] * b[0];
        return new float[]{x, y, z};
    }

    public static float getAngleOf2Vector(final Point3 v1, final Point3 v2) {
        float r, val, cos, angle;
        float len1 = (float)Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        float len2 = (float)Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        r = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        val = len1 * len2;
        if (Math.abs(val) < Constant.EPSILON) {
            angle = (float)(Math.PI / 2.0);
        } else {
            cos = r / val;
            if (cos > 1.0) {
                return 0.0f;
            } else if (cos < -1.0) {
                return (float) Math.PI;
            }
            angle = (float) Math.acos(cos);
        }
        return angle;
    }
}
