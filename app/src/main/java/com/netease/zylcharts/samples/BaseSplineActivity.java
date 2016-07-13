package com.netease.zylcharts.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.netease.zylchartcore.activity.BaseChartActivity;
import com.netease.zylchartcore.data.ControlPoint3;
import com.netease.zylchartcore.shape.Spline;
import com.netease.zylchartcore.shape.SplineMode;
import com.netease.zylchartcore.view.BaseTranslateSurfaceView;
import com.netease.zylcharts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyl06 on 7/11/16.
 */
public class BaseSplineActivity extends BaseChartActivity implements SeekBar.OnSeekBarChangeListener {
    protected SeekBar mSeekBar;
    protected Spline mSpline;

    List<ControlPoint3> mPoints = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.activity_translate_point_clouds, mContainer);

        mSurfaceView = (BaseTranslateSurfaceView) vg.findViewById(R.id.surface_view);

        setPointCount(30);
        //普通拖拉条被拉动的处理代码
        mSeekBar = (SeekBar) this.findViewById(R.id.SeekBar01);
        mSeekBar.setMax(100);
        mSeekBar.setProgress(10);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = mSeekBar.getProgress();
        setPointCount(progress);
    }

    protected int getSplineMode() {
        return SplineMode.SPLMODE_SPLINE;
    }

    protected void setPointCount(int count) {
        mPoints = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            mPoints.add(new ControlPoint3(i / 30.0f, (float) Math.random() / 3.0f, 0, false));
        }

        if (mSpline == null) {
            mSpline = new Spline(mPoints, true, getSplineMode());
            mSurfaceView.setShape(mSpline);
        }
        mSpline.setPoints(mPoints);
        mSurfaceView.requestRender();
    }

    public void setPoint(int idx, ControlPoint3 point) {
        if (mSpline != null && mPoints != null && idx >= 0 && idx < mPoints.size()) {
            mPoints.set(idx, point);
            mSpline.setPoints(mPoints);
            mSurfaceView.requestRender();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lh, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lh_change_value:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_lh_change, null);
        builder.setView(rootView);
        final AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        final EditText etDay = (EditText) rootView.findViewById(R.id.day_value);
        final EditText etLHValue = (EditText) rootView.findViewById(R.id.lh_value);

        Button btnNegative = (Button) rootView.findViewById(R.id.btn_alert_negative);
        Button btnPositive = (Button) rootView.findViewById(R.id.btn_alert_positive);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDay = etDay.getText().toString();
                int day = -1;
                try {
                    day = Integer.parseInt(strDay);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                String strValue = etLHValue.getText().toString();
                float lhValue = -1;
                try {
                    lhValue = Float.parseFloat(strValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (day >= 0 && day < mPoints.size() && lhValue > 0) {
                    setPoint(day, new ControlPoint3(1.0f * day / mPoints.size(), lhValue, 0));
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(BaseSplineActivity.this, "输出有错误哦，day 0 ~ point.size()，lhValue > 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}

