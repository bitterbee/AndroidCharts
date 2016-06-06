package com.netease.zylchartcore.data;

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
}
