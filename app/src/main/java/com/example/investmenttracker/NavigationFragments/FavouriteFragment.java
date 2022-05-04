package com.example.investmenttracker.NavigationFragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Adapters.CoinsAdapter;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.investmenttracker.Helper.CheckConnection;
import static com.example.investmenttracker.Helper.coinViewModel;
import static com.example.investmenttracker.Helper.openDialogForNetworkConnection;
import static com.example.investmenttracker.Helper.api_coin;
import static com.example.investmenttracker.Helper.canRefresh;

public class FavouriteFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CoinsAdapter mAdapter;
    private SwipeRefreshLayout swipeLayout;
    private TextView mTextLastDate;
    private boolean isDetailsActive;
    private FavouriteFragment thisFrag;
    ArrayList<Coin> mCoinsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        thisFrag = this;
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Helper.connected = Helper.CheckConnection(getContext());
        if (Helper.connected) {
            new Helper.InternetCheck(internet -> { Helper.connected = internet; });
            if (!Helper.connected) {
                Helper.openDialogForNetworkConnection(getContext());
            }
        }
        mCoinsList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recycle_Favourite);
        mTextLastDate = view.findViewById(R.id.textViewLastDate);
        while (api_coin.Coins.isEmpty()) {
        }

        swipeLayout = view.findViewById(R.id.swipeLayoutPort);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Helper.connected & canRefresh) {
                    if (api_coin.currentStatus == AsyncTask.Status.FINISHED)
                        Helper.getCoinsData((API_CoinGecko.OnAsyncRequestComplete) thisFrag);
                    buildRecycleView();
                    refreshTimeOfUpdate();
                }
                swipeLayout.setRefreshing(false);
            }
        });

        getFavCoins();
        buildRecycleView();
    }

    private void changeStateOfFavouriteCoin(int position) {
        if (mCoinsList.get(position).isFavourite() == 1) {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), (byte) 0);
            Toast.makeText(getContext(), "Coin "+mCoinsList.get(position).getName().toUpperCase()+" was removed from favourites!", Toast.LENGTH_SHORT).show();
        } else {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), (byte) 1);
        }

    }

    @Override
    public void onStart() {
        Helper.connected = CheckConnection(getContext());
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        super.onStart();
    }

    private void refreshTimeOfUpdate() {
        SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
        Date myTime = null;
        String time = "";
        Log.i("DATUM",Calendar.getInstance().getTime().toString().substring(0,19));
        time = Calendar.getInstance().getTime().toString().substring(0,19);
        mTextLastDate.setText("Last updated: "+ time);
    }

    @Override
    public void onResume() {
        Helper.connected = Helper.CheckConnection(getContext());
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        if (!Helper.connected)
            openDialogForNetworkConnection(getContext());
        refreshTimeOfUpdate();
        super.onResume();
    }

    private void getFavCoins() {

        coinViewModel.getFavouriteCoins().observe(this, new Observer<List<Coin>>() {
            @Override
            public void onChanged(List<Coin> coins) {
                mAdapter.setCoins(coins);
                if (coins.size() == 0) {
                    mTextLastDate.setText("");
                }
            }
        });

        coinViewModel.getFavouriteCoins().observe(this, coins -> {
            mCoinsList.clear();
            mCoinsList.addAll(coins);
        });
    }

    private void removeItem(int position) {
        CoinViewModel.deleteCoin(mCoinsList.get(position).getId());

    }

    private void buildRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CoinsAdapter(mCoinsList, "fav");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setListeners(mAdapter);
    }

    private void setListeners(CoinsAdapter mAdapter) {
        mAdapter.setOnItemClickListener(new CoinsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (isDetailsActive) {
                    CoinsAdapter.CoinsViewHolder.setDetailsEnabled(false);
                    isDetailsActive = false;
                    mRecyclerView.scheduleLayoutAnimation();
                } else {
                    CoinsAdapter.CoinsViewHolder.setDetailsEnabled(true);
                    isDetailsActive = true;
                    mRecyclerView.scheduleLayoutAnimation();
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
            public void onFavouriteClick(int position) {
                changeStateOfFavouriteCoin(position);
            }

            @Override
            public void onEditClick(int position) {
                //
            }
        });
    }
}
