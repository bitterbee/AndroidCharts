package com.netease.zylchartcore.data.operator;

import com.netease.zylchartcore.data.Point3;

/**
 * Created by zyl06 on 7/9/16.
 */
public class PointUtil {
    public static float squareDistance(Point3 p0, Point3 p1) {
        float dx = p0.x - p1.x;
        float dy = p0.y - p1.y;
        float dz = p0.z - p1.z;

        return dx * dx + dy * dy + dz * dz;
    }

    public static float distance(Point3 p0, Point3 p1) {
        return (float) Math.sqrt(squareDistance(p0, p1));
    }

    public static Point3 middle(Point3 p0, Point3 p1) {
        float x = (p0.x + p1.x) / 2.0f;
        float y = (p0.y + p1.y) / 2.0f;
        float z = (p0.z + p1.z) / 2.0f;

        return new Point3(x, y, z);
    }

    public static Point3 add(Point3 p0, Point3 p1) {
        float x = (p0.x + p1.x);
        float y = (p0.y + p1.y);
        float z = (p0.z + p1.z);

        return new Point3(x, y, z);
    }

    public static Point3 sub(Point3 p0, Point3 p1) {
        float x = (p0.x - p1.x);
        float y = (p0.y - p1.y);
        float z = (p0.z - p1.z);

        return new Point3(x, y, z);
    }

    public static Point3 multiple(Point3 p, float m) {
        return new Point3(p.x * m, p.y * m, p.z * m);
    }

    public static Point3 divide(Point3 p, float m) {
        return new Point3(p.x / m, p.y / m, p.z / m);
    }
}
