package com.netease.zylchartcore.data;

/**
 * Created by zyl06 on 7/13/16.
 */
public class ControlPoint3 extends Point3 {
    public boolean isSelected = false;

    public ControlPoint3(float x, float y, float z) {
        super(x, y, z);
    }

    public ControlPoint3(float x, float y, float z, int color) {
        super(x, y, z, color);
    }

    public ControlPoint3(float x, float y, float z, boolean isSelected) {
        super(x, y, z);
        this.isSelected = isSelected;
    }

    public ControlPoint3(float x, float y, float z, int color, boolean isSelected) {
        super(x, y, z, color);
        isSelected = isSelected;
    }
}
