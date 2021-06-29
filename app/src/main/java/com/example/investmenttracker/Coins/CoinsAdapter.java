package com.example.investmenttracker.Coins;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.investmenttracker.API.API_CoinGecko;
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.R;

import java.util.ArrayList;
import java.util.List;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder> {
    private List<Coin> mCoinsList;
    private String nameOfFragment;
    private API_CoinGecko data;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onFavouriteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class CoinsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView, mDeleteImage, mFavouriteImage, mEditImage;
        public TextView mTextName, mTextValue, mTextOwned, mTextLastPrice, mTextVolume, mTextMarketCap;
        public CardView mCardView;
        private LinearLayout detailsLayout, favDetailsLinLayout;
        private LottieAnimationView rocketAnim;
        private static boolean detailsEnabled, rocketAnimEnabled;

        public static void setDetailsEnabled(boolean isActionEnabled){
            detailsEnabled = isActionEnabled;
        }

        public static void setRocketAnimEnabled(boolean isActionEnabled){
            rocketAnimEnabled = isActionEnabled;
        }

        public CoinsViewHolder(View itemView, OnItemClickListener listener, String nameOfFragment) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.cardView);
            mImageView = itemView.findViewById(R.id.imageCoin);
            mDeleteImage = itemView.findViewById(R.id.imageDelete);
            mEditImage = itemView.findViewById(R.id.imageEdit);
            mTextName = itemView.findViewById(R.id.textName);
            mTextValue = itemView.findViewById(R.id.textValue);
            mTextOwned = itemView.findViewById(R.id.textOwned);
            mTextLastPrice = itemView.findViewById(R.id.textViewPrice);
            mTextVolume = itemView.findViewById(R.id.textViewVolume);
            mTextMarketCap = itemView.findViewById(R.id.textViewMrktCap);
            mFavouriteImage = itemView.findViewById(R.id.imageFavourite);
            detailsLayout = itemView.findViewById(R.id.detailsLinLayout);
            favDetailsLinLayout = itemView.findViewById(R.id.favDetailsLinLayout);
            rocketAnim = itemView.findViewById(R.id.rocket_anim);
            rocketAnim.setVisibility(rocketAnimEnabled ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            if (nameOfFragment == "fav") {
                                favDetailsLinLayout.setVisibility(detailsEnabled ? View.VISIBLE : View.GONE);
                            } else {
                                detailsLayout.setVisibility(detailsEnabled ? View.VISIBLE : View.GONE);
                            }
                        }
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            mFavouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavouriteClick(position);
                        }
                    }
                }
            });

            mEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
        }
    }

    public CoinsAdapter(ArrayList<Coin> coinsList, String nameOfFrag, API_CoinGecko api) {
        mCoinsList = coinsList;
        nameOfFragment = nameOfFrag;
        data = api;
    }


    @NonNull
    @Override
    public CoinsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_coin_cardview_item, parent, false);
        CoinsViewHolder ovh = new CoinsViewHolder(v, mListener, nameOfFragment);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull CoinsViewHolder holder, int position) {
        Coin currentItem = mCoinsList.get(position);
        if (nameOfFragment == "fav") {
            holder.mTextLastPrice.setText(Float.toString(data.Coins.get(currentItem.getName().toLowerCase()).get("current_price")));
            holder.mTextVolume.setText(Float.toString(data.Coins.get(currentItem.getName().toLowerCase()).get("total_volume")));
            holder.mTextMarketCap.setText(Float.toString(data.Coins.get(currentItem.getName().toLowerCase()).get("market_cap")));
            holder.mFavouriteImage.setImageResource(currentItem.getFavouriteImage());
            holder.mImageView.setImageResource(currentItem.getCoinImage());
            holder.mTextName.setText(currentItem.getName());
        } else {
            holder.mTextValue.setText(Float.toString(currentItem.getPrice_curr()));
            holder.mTextOwned.setText(Float.toString(currentItem.getOwned()));
            holder.mFavouriteImage.setImageResource(currentItem.getFavouriteImage());
            holder.mImageView.setImageResource(currentItem.getCoinImage());
            holder.mTextName.setText(currentItem.getName());
        }
    }

    @Override
    public int getItemCount() {
        return mCoinsList.size();
    }

    public void setCoins(List<Coin> coins) {
        mCoinsList = coins;
        notifyDataSetChanged();
    }

}
