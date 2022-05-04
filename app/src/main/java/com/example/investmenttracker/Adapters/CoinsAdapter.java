package com.example.investmenttracker.Adapters;

import android.annotation.SuppressLint;
import android.transition.TransitionManager;
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
import com.example.investmenttracker.Database.model.Coin;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import static com.example.investmenttracker.Helper.api_coin;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder> {
    private List<Coin> mCoinsList;
    private String nameOfFragment;
    private OnItemClickListener mListener;
    private int mExpandedPosition = -1;
    private int previousExpandedPosition = 0;

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
        public TextView mTextName, mTextValue, mTextOwned, mTextLastPrice, mTextVolume, mTextMarketCap, mTextAth, mTextPriceChange;
        public CardView mCardView;
        private LinearLayout favDetailsLinLayout, linLayoutCoin, detailsLayout;
        private LottieAnimationView rocketAnim;
        private RecyclerView recPortView, recFavView;
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
            mTextAth = itemView.findViewById(R.id.textViewAth);
            mTextPriceChange = itemView.findViewById(R.id.textViewChange);
            mTextValue = itemView.findViewById(R.id.textValue);
            mTextOwned = itemView.findViewById(R.id.textOwned);
            mTextLastPrice = itemView.findViewById(R.id.textViewPrice);
            mTextVolume = itemView.findViewById(R.id.textViewVolume);
            mTextMarketCap = itemView.findViewById(R.id.textViewMrktCap);
            mFavouriteImage = itemView.findViewById(R.id.imageFavourite);
            detailsLayout = itemView.findViewById(R.id.detailsLinLayout);
            favDetailsLinLayout = itemView.findViewById(R.id.favDetailsLinLayout);
            linLayoutCoin = itemView.findViewById(R.id.linLayoutCoin);
            recPortView = itemView.findViewById(R.id.recycle_portfolio);
            recFavView = itemView.findViewById(R.id.recycle_Favourite);
            rocketAnim = itemView.findViewById(R.id.rocket_anim);
            rocketAnim.setVisibility(rocketAnimEnabled ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
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

    public CoinsAdapter(ArrayList<Coin> coinsList, String nameOfFrag) {
        mCoinsList = coinsList;
        nameOfFragment = nameOfFrag;
    }


    @NonNull
    @Override
    public CoinsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_coin_cardview_item, parent, false);
        ImageView mImageDelete = v.findViewById(R.id.imageDelete);
        if (nameOfFragment == "fav")
            mImageDelete.setVisibility(View.GONE);
        else
            mImageDelete.setVisibility(View.VISIBLE);


        CoinsViewHolder ovh = new CoinsViewHolder(v, mListener, nameOfFragment);
        return ovh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CoinsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Coin currentItem = mCoinsList.get(position);

        if (nameOfFragment == "fav") {
            holder.mTextLastPrice.setText(Helper.currency+api_coin.Coins.get(currentItem.getName().toLowerCase()).get("current_price").toString());
            holder.mTextVolume.setText(Helper.currency+api_coin.Coins.get(currentItem.getName().toLowerCase()).get("total_volume").toString());
            holder.mTextMarketCap.setText(Helper.currency+api_coin.Coins.get(currentItem.getName().toLowerCase()).get("market_cap").toString());
            holder.mTextAth.setText(Helper.currency+api_coin.Coins.get(currentItem.getName().toLowerCase()).get("ath").toString());
            holder.mTextPriceChange.setText(api_coin.Coins.get(currentItem.getName().toLowerCase()).get("price_change_percentage_24h").toString()+" %");
            holder.mFavouriteImage.setImageResource(currentItem.isFavourite() == 1 ? R.drawable.heart_border_full : R.drawable.heart_border_empty);
            Picasso.get().load(currentItem.getImageUrl()).transform(new CropCircleTransformation()).fit().into(holder.mImageView);
            holder.mTextName.setText(currentItem.getName());
        } else {
            holder.mTextValue.setText(Helper.currency+currentItem.getPrice_curr().toString());
            holder.mTextOwned.setText(currentItem.getOwned().toString());
            holder.mFavouriteImage.setImageResource(currentItem.isFavourite() == 1 ? R.drawable.heart_border_full : R.drawable.heart_border_empty);
            Picasso.get().load(currentItem.getImageUrl()).transform(new CropCircleTransformation()).fit().into(holder.mImageView);
            holder.mTextName.setText(currentItem.getName());

        }

        boolean isExpanded = position==mExpandedPosition;
        if (nameOfFragment == "fav")
            holder.favDetailsLinLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        else
            holder.detailsLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        holder.itemView.setActivated(isExpanded);

        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);

            }
        });
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
