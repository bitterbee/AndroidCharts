package com.netease.zylchartcore.data;

/**
 * Created by zyl06 on 6/3/16.
 */
public class Vector3 {

    public static final Vector3 ZERO_VECTOR3 = new Vector3(0, 0, 0);

    private float x;
    private float y;
    private float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


}
