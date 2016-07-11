package com.netease.zylcharts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netease.zylcharts.samples.BezierActivity;
import com.netease.zylcharts.samples.DotChartActivity1;
import com.netease.zylcharts.samples.PolylineActivity1;
import com.netease.zylcharts.samples.BSplineActivity;
import com.netease.zylcharts.samples.SplineActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    ArrayList<ContentItem> mObjects = new ArrayList<ContentItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mObjects.add(new ContentItem(DotChartActivity1.class, "Dot Chart", "A simple demonstration of the dot chart."));
//        mObjects.add(new ContentItem(PolylineActivity1.class, "Polyline Chart", "A simple demonstration of the polyline chart."));
//        mObjects.add(new ContentItem(BezierActivity.class, "LH Bezier Chart", "A simple demonstration of the bezier chart."));
        mObjects.add(new ContentItem(BSplineActivity.class, "LH BSpline Chart", "A simple demonstration of the b-spline chart."));
        mObjects.add(new ContentItem(SplineActivity.class, "LH Spline Chart", "A simple demonstration of the spline chart."));

        MainAdapter adapter = new MainAdapter(this, mObjects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        if (lv != null) {
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
        ContentItem ci = mObjects.get(pos);
        Intent i = new Intent(this, ci.getActivityClass());
        if (i != null) {
            startActivity(i);
        }

        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}
