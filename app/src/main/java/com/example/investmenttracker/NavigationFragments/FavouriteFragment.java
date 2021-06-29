package com.example.investmenttracker.NavigationFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Coins.CoinsAdapter;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Database.model.CoinViewModel;
import com.example.investmenttracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavouriteFragment extends Fragment {

    private CoinViewModel coinViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CoinsAdapter mAdapter;
    private boolean isDetailsActive;
    private API_CoinGecko api;
    ArrayList<Coin> mCoinsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        coinViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication()).create(CoinViewModel.class);
        mCoinsList = new ArrayList<>();

        final View favView = inflater.inflate(R.layout.fragment_favourite, container, false);
        mRecyclerView = favView.findViewById(R.id.recycle_Favourite);

        api = new API_CoinGecko();
        api.startToPullDataFromAPI();

        getFavCoins();
        buildRecycleView();

        return favView;
    }

    private void changeStateOfFavouriteCoin(int position) {
        if (mCoinsList.get(position).getFavouriteImage() == R.drawable.heart_border_full) {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_empty);
            Toast.makeText(getContext(), "Coins was removed from favourites!", Toast.LENGTH_SHORT).show();
        } else {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_full);
        }
    }

    private void getFavCoins() {

        coinViewModel.getFavouriteCoins().observe(this, new Observer<List<Coin>>() {
            @Override
            public void onChanged(List<Coin> coins) {
                mAdapter.setCoins(coins);
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
        mAdapter = new CoinsAdapter(mCoinsList, "fav", api);

        mRecyclerView.setLayoutManager(mLayoutManager);
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
