package com.example.investmenttracker.NavigationFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Coins.CoinsAdapter;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.R;
import com.example.investmenttracker.SlidePage.Fragments.MoneyAllocFragment;
import com.example.investmenttracker.SlidePage.Fragments.PercentFragment;
import com.example.investmenttracker.SlidePage.ViewPagerAdapter;
import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.investmenttracker.MainActivity.api;


public class PortfolioFragment extends Fragment {

    private CoinViewModel coinViewModel;
    private RecyclerView mRecyclerView;
    private CoinsAdapter mAdapter;
    private EditText textVnosName, textVnosValue, textVnosQuantity;
    private Animation fadeAnimation;
    private Switch switchLiveData;
    private boolean connected;
    private ViewPager2 pager;
    private Float portfolio_value = 0f;
    private ConstraintLayout popUpLayout;
    private Button confButton, cancelButton;
    private boolean isDetailsActive;
    private boolean canReset = true;
    private int posOfChart;
    ArrayList<PieEntry> percValues, moneyAllocValues;
    ArrayList<Coin> mCoinsList, mGroupedCoinsList;
    ViewPagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        coinViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication()).create(CoinViewModel.class);
        mCoinsList = new ArrayList<>();
        mGroupedCoinsList = new ArrayList<>();
        CoinsAdapter.CoinsViewHolder.setRocketAnimEnabled(false);
        final View portfView = inflater.inflate(R.layout.fragment_portfolio, container, false);
        popUpLayout = (ConstraintLayout) portfView.findViewById(R.id.Popup);
        confButton = (Button) popUpLayout.findViewById(R.id.confirm_button);
        cancelButton = (Button) popUpLayout.findViewById(R.id.cancelButton);
        textVnosName = (EditText) portfView.findViewById(R.id.textVnosName);
        textVnosValue = (EditText) portfView.findViewById(R.id.textVnosValue);
        textVnosQuantity = (EditText) portfView.findViewById(R.id.textVnosQuantity);
        switchLiveData = (Switch) portfView.findViewById(R.id.switchRealData);
        mRecyclerView = portfView.findViewById(R.id.recycle_portfolio);
        ImageButton add_button = (ImageButton) portfView.findViewById(R.id.add_button);
        pager = portfView.findViewById(R.id.viewPager);

        initViewPager();
        getOwnedCoins();
        buildRecycleView();

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    MoneyAllocFragment.getInstance().createMoneyAllocChart(moneyAllocValues, portfolio_value.toString());
                } else {
                    PercentFragment.getInstance().createPercChart(percValues, portfolio_value.toString());
                }
                posOfChart = position;
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fadeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
                popUpLayout.startAnimation(fadeAnimation);
                popUpLayout.setVisibility(View.VISIBLE);

                switchLiveData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        textVnosValue.setEnabled(!isChecked);
                    }
                });

                confButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        api.RefreshDataFromAPI();
                        if (switchLiveData.isChecked()) {
                            addCoin(textVnosName.getText().toString(), Float.parseFloat(api.Coins.get(textVnosName.getText().toString().toLowerCase()).get("current_price").toString()), Float.parseFloat(textVnosQuantity.getText().toString()));
                        } else {
                            addCoin(textVnosName.getText().toString(), Float.parseFloat(textVnosValue.getText().toString()), Float.parseFloat(textVnosQuantity.getText().toString()));
                        }
                        fadeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
                        popUpLayout.startAnimation(fadeAnimation);
                        popUpLayout.setVisibility(View.INVISIBLE);

                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        fadeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
                        popUpLayout.startAnimation(fadeAnimation);
                        popUpLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
        return portfView;
    }

    @Override
    public void onStart() {
        connected = CheckConnection();

        if (connected) {
            api = new API_CoinGecko();
            api.RefreshDataFromAPI();
        }
        super.onStart();
    }

    private void initViewPager() {
        mPagerAdapter = new ViewPagerAdapter(getActivity());
        pager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onResume() {
        if (!connected)
            openDialog();
        super.onResume();
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("No internet. Please check your connection status!");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connected = CheckConnection();
                if (!connected) {
                    openDialog();
                }
                else {
                    api = new API_CoinGecko();
                    api.RefreshDataFromAPI();
                }
            }
        });
        builder.show();
    }

    private boolean CheckConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    private void getOwnedCoins() {

        coinViewModel.getAllCoins().observe(this, new Observer<List<Coin>>() {
            @Override
            public void onChanged(List<Coin> coins) {
                mAdapter.setCoins(coins);
            }
        });

        coinViewModel.getAllCoins().observe(this, coins -> {
            mCoinsList.clear();
            portfolio_value = 0f;
            percValues = new ArrayList<PieEntry>();
            moneyAllocValues = new ArrayList<PieEntry>();
            Map<String, Float> hm = new HashMap<>();
            for (Coin coin: coins) {
                portfolio_value = portfolio_value + (coin.getOwned()*coin.getPrice_curr());
                mCoinsList.add(coin);
            }
            for (Coin coin : coins) {
                String name = coin.getName();
                Float price = hm.containsKey(name) ? hm.get(name) : 0f;
                price += (coin.getPrice_curr()*coin.getOwned());
                hm.put(name, price);
            }
            for (String key : hm.keySet()) {
                percValues.add(new PieEntry(hm.get(key), key));
                moneyAllocValues.add(new PieEntry(hm.get(key), key));
            }
            if (canReset) {
                drawCharts();
            }
        });
    }

    private void drawCharts() {
        if (mPagerAdapter.getCurrFragment() != null) {
            if (posOfChart == 0) {
                PercentFragment.getInstance().createPercChart(percValues, portfolio_value.toString());
            } else {
                MoneyAllocFragment.getInstance().createMoneyAllocChart(moneyAllocValues, portfolio_value.toString());
            }
            mPagerAdapter.notifyItemChanged(pager.getCurrentItem());
        }
    }

    private void addCoin(String name, Float value, float owned) {
        canReset = true;
        CoinViewModel.insert(new Coin(getResources().getIdentifier(name.toLowerCase(), "drawable", getContext().getPackageName()), name.toUpperCase(), Float.parseFloat(value.toString()), owned, R.drawable.heart_border_empty));
        mCoinsList.add(new Coin(getResources().getIdentifier(name.toLowerCase(), "drawable", getContext().getPackageName()), name.toUpperCase(), Float.parseFloat(value.toString()), owned, R.drawable.heart_border_empty));
    }

    private void removeItem(int position) {
        canReset = true;
        CoinViewModel.deleteCoin(mCoinsList.get(position).getId());
    }

    private void changeStateOfFavouriteCoin(int position) {
        canReset = false;
        if (mCoinsList.get(position).getFavouriteImage() == R.drawable.heart_border_full) {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_empty);
            mCoinsList.get(position).setFavouriteImage(R.drawable.heart_border_empty);
        } else {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_full);
            mCoinsList.get(position).setFavouriteImage(R.drawable.heart_border_full);
        }

    }

    private void buildRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CoinsAdapter(mCoinsList, "port");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new CoinsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (isDetailsActive) {
                    CoinsAdapter.CoinsViewHolder.setDetailsEnabled(false);
                    isDetailsActive = false;
                } else {
                    CoinsAdapter.CoinsViewHolder.setDetailsEnabled(true);
                    isDetailsActive = true;
                }
            }

            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Are you sure?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }

            @Override
            public void onFavouriteClick(int position) {
                changeStateOfFavouriteCoin(position);
            }
        });



    }



}
