package com.example.investmenttracker.SlidePage.Fragments;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PercentFragment extends Fragment {

    public static PieChart percChart;
    public static ArrayList<PieEntry> mProcValues;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_percent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        percChart = view.findViewById(R.id.percentChart);
        mProcValues = new ArrayList<>();
        createPercChart(mProcValues, "0.0");
    }

    public void createPercChart(ArrayList<PieEntry> procValues, String portValue) {
        percChart.setExtraOffsets(12f, 12f,12f,12f);
        percChart.getDescription().setEnabled(false);
        percChart.getLegend().setEnabled(false);

        mProcValues = procValues;

        percChart.setCenterText(String.format("%.0f", (double)Math.round(Double.parseDouble(portValue))) + " " + Helper.currency);

        percChart.setUsePercentValues(true);
        PieDataSet dataSet = new PieDataSet(mProcValues, null);
        PieData data = new PieData(dataSet);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.6f);
        data.setValueFormatter(new PercentFormatter());
        percChart.setData(data);
        percChart.setDrawHoleEnabled(true);
        percChart.setTransparentCircleRadius(50f);
        percChart.setHoleRadius(46f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data.setValueTextSize(13f);

        if (Helper.uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES) {
            percChart.setEntryLabelColor(Color.WHITE);
            data.setValueTextColor(Color.WHITE);
        } else {
            percChart.setEntryLabelColor(Color.BLACK);
            data.setValueTextColor(Color.BLACK);
        }

        percChart.animateXY(0, 2000, Easing.EaseInOutCubic);
    }


}
