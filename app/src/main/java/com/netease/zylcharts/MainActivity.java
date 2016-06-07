package com.netease.zylcharts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.netease.zylcharts.samples.DotChartActivity1;
import com.netease.zylcharts.samples.PolylineActivity1;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
        objects.add(new ContentItem("Dot Chart", "A simple demonstration of the dot chart."));
        objects.add(new ContentItem("Polyline Chart", "A simple demonstration of the polyline chart."));

        MainAdapter adapter = new MainAdapter(this, objects);

        ListView lv = (ListView) findViewById(R.id.listView1);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long arg3) {
        Intent i = null;

        switch (pos) {
            case 0:
                i = new Intent(this, DotChartActivity1.class);
                break;
            case 1:
                i = new Intent(this, PolylineActivity1.class);
                break;
            default:
                break;
        }
        if (i != null) {
            startActivity(i);
        }

        overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
    }
}
