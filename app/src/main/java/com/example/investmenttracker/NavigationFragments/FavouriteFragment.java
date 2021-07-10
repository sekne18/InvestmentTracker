package com.example.investmenttracker.NavigationFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.example.investmenttracker.MainActivity.api;

public class FavouriteFragment extends Fragment {

    private CoinViewModel coinViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CoinsAdapter mAdapter;
    private boolean connected;
    private TextView mTextLastDate;
    private boolean isDetailsActive;
    ArrayList<Coin> mCoinsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        coinViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication()).create(CoinViewModel.class);
        mCoinsList = new ArrayList<>();
        final View favView = inflater.inflate(R.layout.fragment_favourite, container, false);
        mRecyclerView = favView.findViewById(R.id.recycle_Favourite);
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        mTextLastDate = favView.findViewById(R.id.textViewLastDate);
        try {
            date = format.parse(api.last_updated);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTextLastDate.setText("Last updated: "+ format.format(date));

        getFavCoins();
        buildRecycleView();

        return favView;
    }

    private void changeStateOfFavouriteCoin(int position) {
        if (mCoinsList.get(position).getFavouriteImage() == R.drawable.heart_border_full) {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_empty);
            Toast.makeText(getContext(), "Coin "+mCoinsList.get(position).getName().toUpperCase()+" was removed from favourites!", Toast.LENGTH_SHORT).show();

        } else {
            CoinViewModel.favouriteImage(mCoinsList.get(position).getName(), R.drawable.heart_border_full);
        }

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
