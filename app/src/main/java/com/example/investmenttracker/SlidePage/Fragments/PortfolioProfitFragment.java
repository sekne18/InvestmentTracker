package com.example.investmenttracker.SlidePage.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.investmenttracker.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioProfitFragment extends Fragment {

    private static PortfolioProfitFragment instance = null;
    public static TextView profitText;
    public static ArrayList<PieEntry> mProcValues;

    public static PortfolioProfitFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup profitView = (ViewGroup) inflater.inflate(R.layout.fragment_portfolio_profit, container, false);
        profitText = profitView.findViewById(R.id.profitTextView);
        instance = this;
        createProfitChart(null);
        return profitView;
    }

    public void createProfitChart(Map<String, Map<Float,Float>> portfolioValue) {
        //TODO Naberi vse investicije za posamezen kovance in izračunaj avg entry price. Od te cene nato pomnoži s količino kovancov
        Map<String,Float> value = new HashMap<>();
//        for (Map<String, Map<Float,Float>> coin: portfolioValue) {
//            value.put(coin.keySet(), coin.values());
//
//            String coin: porfolioValue.keySet()
//        }
    }




}