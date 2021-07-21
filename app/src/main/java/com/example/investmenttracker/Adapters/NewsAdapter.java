package com.example.investmenttracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.investmenttracker.API.API_News;
import com.example.investmenttracker.Activities.News.NewsActivity;
import com.example.investmenttracker.Database.model.News;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> mNewsList;
    private API_News data;
    private NewsAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView mNewsImage;
        public TextView mTextContent, mTextTitle;
        public CardView mNewsCardView;
        private LinearLayout newsContentLinLayout;
        private static boolean detailsEnabled;

        public static void setDetailsEnabled(boolean isActionEnabled){
            detailsEnabled = isActionEnabled;
        }

        public NewsViewHolder(View itemView, NewsAdapter.OnItemClickListener listener) {
            super(itemView);
            mNewsCardView = itemView.findViewById(R.id.newsCardView);
            mTextTitle = itemView.findViewById(R.id.textTitle);
            mTextContent = itemView.findViewById(R.id.textContent);
            mNewsImage = itemView.findViewById(R.id.imageNews);
            newsContentLinLayout = itemView.findViewById(R.id.newsContentLinLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                            newsContentLinLayout.setVisibility(detailsEnabled ? View.VISIBLE : View.GONE);
                        }
                    }
                }
            });

        }
    }

    public NewsAdapter(ArrayList<News> newsList) {
        mNewsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_cardview_item, parent, false);
        NewsAdapter.NewsViewHolder ovh = new NewsAdapter.NewsViewHolder(v, mListener);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentItem = mNewsList.get(position);
        holder.mTextTitle.setText(currentItem.getTitle());
        holder.mTextContent.setText(currentItem.getBody());
        Picasso.get().load(currentItem.getImageUrl()).fit().into(holder.mNewsImage);
//        new Helper.DownloadImageTask((holder.mNewsImage)).execute(currentItem.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setCoins(List<News> news) {
        mNewsList = news;
        notifyDataSetChanged();
    }
}
