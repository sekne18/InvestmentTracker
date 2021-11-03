package com.example.investmenttracker.SlidePage.Fragments;

import static com.example.investmenttracker.MainActivity.api_coin;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.investmenttracker.Database.model.Coin;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioProfitFragment extends Fragment {

    private static PortfolioProfitFragment instance = null;
    private TextView profitText, balanceText, textPercentage;
    private ImageView imageUpOrDown;

    public static PortfolioProfitFragment getInstance() {
        return instance != null ? instance : new PortfolioProfitFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup profitView = (ViewGroup) inflater.inflate(R.layout.fragment_portfolio_profit, container, false);
        profitText = profitView.findViewById(R.id.profitTextView);
        balanceText = profitView.findViewById(R.id.textBalance);
        textPercentage = profitView.findViewById(R.id.textPercentage);
        imageUpOrDown = profitView.findViewById(R.id.imageUpOrDown);
        instance = this;
        return profitView;
    }

    public void createProfitChart() {
        float sumCurrentPrice  = 0f, sumCoinPrices = 0f;
        DecimalFormat df = new DecimalFormat("#.#");
        if (Helper.mCoinsList.isEmpty()) {
            imageUpOrDown.setVisibility(View.INVISIBLE);
            profitText.setTextColor(getResources().getColor(R.color.fonts));
            textPercentage.setTextColor(getResources().getColor(R.color.fonts));
            textPercentage.setText("0 %");
            profitText.setVisibility(View.INVISIBLE);
            balanceText.setText("0 " +  Helper.currency);
            return;
        } else {
            imageUpOrDown.setVisibility(View.VISIBLE);
            profitText.setVisibility(View.VISIBLE);
        }

        while (api_coin.Coins.isEmpty()) {
        }

        for (Coin coin : Helper.mCoinsList) {
            sumCoinPrices += coin.getPrice_curr()*coin.getOwned();
            sumCurrentPrice += Float.parseFloat(api_coin.Coins.get(coin.getName().toLowerCase()).get("current_price").toString())*coin.getOwned();
        }
        balanceText.setText(df.format(sumCurrentPrice) + " " +  Helper.currency);
        if (sumCurrentPrice > sumCoinPrices) {
            profitText.setTextColor(getResources().getColor(R.color.plus_port));
            textPercentage.setTextColor(getResources().getColor(R.color.plus_port));
            imageUpOrDown.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            textPercentage.setText(df.format(((sumCurrentPrice-sumCoinPrices)*100)/sumCoinPrices) + " %");
            profitText.setText("+" + df.format(sumCurrentPrice-sumCoinPrices) + " " +  Helper.currency);
        } else {
            profitText.setTextColor(getResources().getColor(R.color.minus_port));
            textPercentage.setTextColor(getResources().getColor(R.color.minus_port));
            imageUpOrDown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            textPercentage.setText(df.format(((sumCurrentPrice-sumCoinPrices)*100)/sumCoinPrices) + " %");
            profitText.setText("-" + df.format(sumCurrentPrice-sumCoinPrices) + " " +  Helper.currency);
        }

    }

}