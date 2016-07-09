package com.netease.zylchartcore.data;

import android.graphics.Point;

import com.netease.zylchartcore.c.Constant;

/**
 * Created by zyl06 on 6/3/16.
 */
public class Point3 {
    public static final Point3 ORIGIN3 = new Point3(0, 0, 0);

    public float x;
    public float y;
    public float z;

    public Point3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public float squareMagnitude() {
        return x * x + y * y + z * z;
    }

    public float magnitude() {
        return (float) Math.sqrt(squareMagnitude());
    }

    public Point3 normalize() {
        float len = magnitude();
        Point3 nor = new Point3(0, 0, 0);
        if (len > Constant.EPSILON) {
            nor.x = x / len;
            nor.y = y / len;
            nor.z = z / len;
        }
        return nor;
    }


    public Point3 middle(Point3 p) {
        float _x = (x + p.x) / 2.0f;
        float _y = (y + p.y) / 2.0f;
        float _z = (z + p.z) / 2.0f;

        return new Point3(_x, _y, _z);
    }

    public Point3 add(Point3 p) {
        float _x = (x + p.x);
        float _y = (y + p.y);
        float _z = (z + p.z);

        return new Point3(_x, _y, _z);
    }

    public Point3 sub(Point3 p) {
        float _x = (x - p.x);
        float _y = (y - p.y);
        float _z = (z - p.z);

        return new Point3(_x, _y, _z);
    }

    public Point3 multiple(float m) {
        return new Point3(x * m, y * m, z * m);
    }

    public Point3 devide(float m) {
        return new Point3(x / m, y / m, z / m);
    }

}
