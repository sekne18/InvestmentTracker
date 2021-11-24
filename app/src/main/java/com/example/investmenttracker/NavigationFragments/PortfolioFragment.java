package com.example.investmenttracker.NavigationFragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.investmenttracker.Adapters.CoinsAdapter;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;
import com.example.investmenttracker.SlidePage.Fragments.MoneyAllocFragment;
import com.example.investmenttracker.SlidePage.Fragments.PercentFragment;
import com.example.investmenttracker.SlidePage.Fragments.PortfolioProfitFragment;
import com.example.investmenttracker.SlidePage.ViewPagerAdapter;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


import static com.example.investmenttracker.Helper.coinViewModel;
import static com.example.investmenttracker.Helper.mCoinsList;
import static com.example.investmenttracker.MainActivity.api_coin;
import static com.example.investmenttracker.MainActivity.canRefresh;


public class PortfolioFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CoinsAdapter mAdapter;
    private EditText textVnosName, textVnosValue, textVnosQuantity;
    private Animation fadeAnimation;
    private Switch switchLiveData;
    private ViewPager2 pager;
    private Float portfolio_value = 0f;
    private ConstraintLayout popUpLayout;
    private Button confButton, cancelButton;
    private boolean canReset = true;
    private int posOfChart;
    private Map<String, Map<Float,Float>> grouppedcoins = new HashMap<>();
    private ArrayList<PieEntry> percValues, moneyAllocValues;
    private ViewPagerAdapter mPagerAdapter;
    private SwipeRefreshLayout swipeLayoutPort;
    private ImageButton add_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CoinsAdapter.CoinsViewHolder.setRocketAnimEnabled(false);
        return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        popUpLayout = (ConstraintLayout) view.findViewById(R.id.Popup);
        confButton = (Button) view.findViewById(R.id.confirm_button);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        textVnosName = (EditText) view.findViewById(R.id.textVnosName);
        textVnosValue = (EditText) view.findViewById(R.id.textVnosValue);
        textVnosQuantity = (EditText) view.findViewById(R.id.textVnosQuantity);
        switchLiveData = (Switch) view.findViewById(R.id.switchRealData);
        mRecyclerView = view.findViewById(R.id.recycle_portfolio);
        swipeLayoutPort = view.findViewById(R.id.swipeLayoutPort);
        add_button = (ImageButton) view.findViewById(R.id.add_button);
        pager = view.findViewById(R.id.viewPager);
        mPagerAdapter = new ViewPagerAdapter(getActivity());
        pager.setAdapter(mPagerAdapter);
        getOwnedCoins();
        buildRecycleView();
        setUpListeners();
    }

    private void setUpListeners() {
        swipeLayoutPort.setRefreshing(false);
        swipeLayoutPort.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (api_coin.isCompleted)
                    api_coin.RefreshDataFromAPI();
                PortfolioProfitFragment.getInstance().createProfitChart();
                swipeLayoutPort.setRefreshing(false);
            }
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    PortfolioProfitFragment.getInstance().createProfitChart();
                } else if (position == 1) {
                    MoneyAllocFragment.getInstance().createMoneyAllocChart(moneyAllocValues, portfolio_value.toString());
                } else if (position == 2) {
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
                        //Check if coin is in the API
                        if (api_coin.Coins.get(textVnosName.getText().toString().toLowerCase()) == null) {
                            new AlertDialog.Builder(getContext()).setTitle("Coin not found").setMessage("This coin is not supported").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            }).show();
                        } else {
                            if (switchLiveData.isChecked()) {
                                if (api_coin.isCompleted)
                                    api_coin.RefreshDataFromAPI();
                                addCoin(textVnosName.getText().toString(), Float.parseFloat(api_coin.Coins.get(textVnosName.getText().toString().toLowerCase()).get("current_price").toString()), Float.parseFloat(textVnosQuantity.getText().toString()));
                            } else {
                                addCoin(textVnosName.getText().toString(), Float.parseFloat(textVnosValue.getText().toString()), Float.parseFloat(textVnosQuantity.getText().toString()));
                            }
                            fadeAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
                            popUpLayout.startAnimation(fadeAnimation);
                            popUpLayout.setVisibility(View.INVISIBLE);
                        }
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
    }

    @Override
    public void onStart() {
        Helper.connected = Helper.CheckConnection(getContext());
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        super.onStart();
    }

    private void getOwnedCoins() {
        coinViewModel.getAllCoins().observe(this, coins -> {
            mAdapter.setCoins(coins);
            mCoinsList.clear();
            portfolio_value = 0f;
            percValues = new ArrayList<PieEntry>();
            moneyAllocValues = new ArrayList<PieEntry>();
            Map<String, Float> hm = new HashMap<>();

            for (Coin coin : coins) {
                portfolio_value = portfolio_value + (coin.getOwned()*coin.getPrice_curr());
                mCoinsList.add(coin);

                String name = coin.getName();
                float price = hm.containsKey(name) ? hm.get(name) : 0f;
                price += (coin.getPrice_curr()*coin.getOwned());
                hm.put(name, price);

                Map<Float, Float> value = new HashMap<>();
                value.put(coin.getOwned(), coin.getPrice_curr());
                grouppedcoins.put(coin.getName(), value);
            }
            for (String key : hm.keySet()) {
                percValues.add(new PieEntry(hm.get(key), key));
                moneyAllocValues.add(new PieEntry(hm.get(key), key));
            }
            if (canReset) {
                drawCharts();
                canReset = false;
            }
        });
    }

    private void drawCharts() {
        if (mPagerAdapter.getCurrFragment() != null) {
            if (posOfChart == 0) {
                PortfolioProfitFragment.getInstance().createProfitChart();
            } else if (posOfChart == 1) {
                PercentFragment.getInstance().createPercChart(percValues, portfolio_value.toString());
            } else if (posOfChart == 2) {
                MoneyAllocFragment.getInstance().createMoneyAllocChart(moneyAllocValues, portfolio_value.toString());
            }
            mPagerAdapter.notifyItemChanged(pager.getCurrentItem());
        }
    }

    private void addCoin(String name, Float value, float owned) {
        canReset = true;
        CoinViewModel.insert(new Coin(api_coin.coin_Images.get(name.toLowerCase()),name.toUpperCase(), Float.parseFloat(value.toString()), owned, Helper.currency, (byte)0));
        mCoinsList.add(new Coin(api_coin.coin_Images.get(name.toLowerCase()),name.toUpperCase(), Float.parseFloat(value.toString()), owned, Helper.currency, (byte)1));
    }

    private void removeItem(int position) {
        canReset = true;
        CoinViewModel.deleteCoin(mCoinsList.get(position).getId());
    }

    private void changeStateOfFavouriteCoin(int position) {
        canReset = false;
        if (mCoinsList.get(position).isFavourite() == 1) {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), (byte)0);
            mCoinsList.get(position).isFavourite((byte)0);
        } else {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), (byte)1);
            mCoinsList.get(position).isFavourite((byte)1);
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
