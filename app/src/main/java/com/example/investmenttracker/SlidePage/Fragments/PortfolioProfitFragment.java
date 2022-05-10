package com.example.investmenttracker.SlidePage.Fragments;

import static com.example.investmenttracker.Helper.api_coin;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;


public class PortfolioProfitFragment extends Fragment implements API_CoinGecko.OnAsyncRequestComplete {

    private TextView profitText, balanceText, textPercentage;
    private ImageView imageUpOrDown;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_portfolio_profit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profitText = view.findViewById(R.id.profitTextView);
        balanceText = view.findViewById(R.id.textBalance);
        textPercentage = view.findViewById(R.id.textPercentage);
        imageUpOrDown = view.findViewById(R.id.imageUpOrDown);
        createProfitChart();
    }

    public void createProfitChart() {
        if (api_coin.currentStatus == AsyncTask.Status.FINISHED) {
            float sumCurrentPrice = 0f, sumCoinPrices = 0f;
            DecimalFormat df = new DecimalFormat("#.#");
            if (Helper.mCoinsList.isEmpty()) {
                imageUpOrDown.setVisibility(View.INVISIBLE);
                profitText.setTextColor(getResources().getColor(R.color.mainText));
                textPercentage.setTextColor(getResources().getColor(R.color.mainText));
                textPercentage.setText("0 %");
                profitText.setVisibility(View.INVISIBLE);
                balanceText.setText("0 " + Helper.currency);
                return;
            } else {
                imageUpOrDown.setVisibility(View.VISIBLE);
                profitText.setVisibility(View.VISIBLE);
            }

            if (Helper.mCoinsList != null) {
                for (Coin coin : Helper.mCoinsList) {
                    sumCoinPrices += coin.getPrice_curr() * coin.getOwned();
                    sumCurrentPrice += Float.parseFloat(api_coin.Coins.get(coin.getName().toLowerCase()).get("current_price").toString()) * coin.getOwned();
                }
            }

            balanceText.setText(df.format(sumCurrentPrice) + " " + Helper.currency);
            if (sumCurrentPrice > sumCoinPrices) {
                profitText.setTextColor(getResources().getColor(R.color.mainText));
                textPercentage.setTextColor(getResources().getColor(R.color.mainText));
                imageUpOrDown.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
                textPercentage.setText(df.format(((sumCurrentPrice - sumCoinPrices) * 100) / sumCoinPrices) + " %");
                profitText.setText("+" + df.format(sumCurrentPrice - sumCoinPrices) + " " + Helper.currency);
            } else {
                profitText.setTextColor(getResources().getColor(R.color.mainText));
                textPercentage.setTextColor(getResources().getColor(R.color.mainText));
                imageUpOrDown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                textPercentage.setText(df.format(((sumCurrentPrice - sumCoinPrices) * 100) / sumCoinPrices) + " %");
                profitText.setText("-" + df.format(sumCurrentPrice - sumCoinPrices) + " " + Helper.currency);
            }
        }
    }

    @Override
    public void onPostExecute(Map<String, Map<String, BigDecimal>> coins) {

    }
}