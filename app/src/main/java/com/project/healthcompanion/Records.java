package com.project.healthcompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.healthcompanion.R;

import java.util.ArrayList;

public class Records extends AppCompatActivity {

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_records);

        lineChart = findViewById(R.id.lineChart);
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(18f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = lineChart.getAxisLeft();

        yAxis.setValueFormatter(new MyAxisValueFormatter());
    }

    private void getEntries() {
        lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(1, 58.8f));
        lineEntries.add(new Entry(2, 58.5f));
        lineEntries.add(new Entry(3, 58.2f));
        lineEntries.add(new Entry(4, 58f));
        lineEntries.add(new Entry(5, 57.8f));
        lineEntries.add(new Entry(6, 57.5f));
        lineEntries.add(new Entry(7, 57.2f));
        lineEntries.add(new Entry(8, 57f));
        lineEntries.add(new Entry(9, 56.8f));
        lineEntries.add(new Entry(10, 56.5f));
        lineEntries.add(new Entry(11, 56.2f));
        lineEntries.add(new Entry(12, 56f));
        lineEntries.add(new Entry(13, 55.8f));
        lineEntries.add(new Entry(14, 55.6f));
        lineEntries.add(new Entry(15, 55.2f));
        lineEntries.add(new Entry(16, 55f));
    }

    private class MyAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return value + "kg";
        }
    }
}