package com.example.investmenttracker.NavigationFragments;

import static com.example.investmenttracker.Helper.mCoinsList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.API.API_CurrencyExchange;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.text.DecimalFormat;
import java.util.Map;

public class SettingsFragment extends Fragment implements API_CurrencyExchange.OnAsyncRequestComplete {

    private API_CurrencyExchange api_currencies;
    private ProgressBar progressBarCurr;
    private SettingsFragment thisFragment;
    private Switch nightMode;
    private Spinner currSpinner;
    private CompoundButton.OnCheckedChangeListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nightMode = view.findViewById(R.id.nightmodeSwitch);
        currSpinner = (Spinner) view.findViewById(R.id.spinner);
        progressBarCurr = view.findViewById(R.id.progBarCurrency);
        progressBarCurr.setVisibility(View.INVISIBLE);
        thisFragment = this;
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.currencies));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currSpinner.setAdapter(myAdapter);

        Animation fadein = new AlphaAnimation(1.f, 0.f);
        fadein.setDuration(1000);
        fadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                changeTheme();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getActivity().findViewById(R.id.fragment_main).startAnimation(fadein);
            }
        };

//        nightMode.setOnCheckedChangeListener(listener);

        currSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.mainText));
                ((TextView) view).setTextSize(17);
                String oldCurr = Helper.currency;
                Helper.currency = currSpinner.getItemAtPosition(position).toString();
                if (!oldCurr.equals(Helper.currency)) {
                    api_currencies = new API_CurrencyExchange(thisFragment);
                    if (Helper.currency.equals("€")) {
                        Helper.sharedPrefs.edit().putString("currency", "$").apply();
                        api_currencies.execute("https://api.exchangerate.host/convert?from=USD&to=EUR&amount=1");
                        Helper.getCoinsData((API_CoinGecko.OnAsyncRequestComplete) thisFragment);
                    }
                    else if (Helper.currency.equals("$")) {
                        Helper.sharedPrefs.edit().putString("currency", "€").apply();
                        api_currencies.execute("https://api.exchangerate.host/convert?from=EUR&to=USD&amount=1");
                        Helper.getCoinsData((API_CoinGecko.OnAsyncRequestComplete) thisFragment);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        nightMode.setOnCheckedChangeListener(null);
        nightMode.setChecked(Helper.sharedPrefs.getBoolean("nightMode", false));
        nightMode.setOnCheckedChangeListener(listener);
        switch (Helper.currency) {
            case "$":
                currSpinner.setSelection(0);
                break;
            case "€":
                currSpinner.setSelection(1);
                break;
        }
    }

    public void ConvertCoins() {
        DecimalFormat df = new DecimalFormat(".#");
        for (Coin coin: mCoinsList) {
            if (!coin.getCurrency().equals(Helper.currency)) {
                coin.setCurrency(Helper.currency);
                coin.setPrice_curr((float) Math.round((Double.parseDouble(String.valueOf(coin.getPrice_curr()*api_currencies.Currency.get(Helper.currency))))*100)/100);
            }
        }
    }

    private void changeTheme() {
        Helper.sharedPrefs.edit().putBoolean("nightMode", nightMode.isChecked()).apply();
        Helper.returnToSettings = true;
        if (Helper.sharedPrefs.getBoolean("nightMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    public void onPostExecute(Map<String, Double> currencies) {
        progressBarCurr.setVisibility(View.INVISIBLE);
        ConvertCoins();
    }

    @Override
    public void onPreExecute() {
        progressBarCurr.setVisibility(View.VISIBLE);
    }

}
