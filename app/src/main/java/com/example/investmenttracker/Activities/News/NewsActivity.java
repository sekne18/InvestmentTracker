package com.example.investmenttracker.Activities.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.investmenttracker.API.API_News;
import com.example.investmenttracker.Adapters.NewsAdapter;
import com.example.investmenttracker.Database.model.News;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.investmenttracker.Helper.CheckConnection;
import static com.example.investmenttracker.Helper.openDialogForNetworkConnection;


public class NewsActivity extends AppCompatActivity implements API_News.OnAsyncRequestComplete {

    private ImageView img;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private boolean isDetailsActive;
    private ArrayList<News> mNewsList;
    private API_News api_news;
    private ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        backbutton();
        mNewsList = new ArrayList<News>();
        mRecyclerView = this.findViewById(R.id.recyclerNews);
        pgsBar = (ProgressBar)findViewById(R.id.pBar);
        api_news = new API_News(this);
        api_news.execute("https://min-api.cryptocompare.com/data/v2/news/?lang=EN");
    }

    private void pullDataFromApiToArray() {
        for (int i = 0; i < api_news.News.size(); i++) {
            mNewsList.add(new News(api_news.News.get(i).get("title"),api_news.News.get(i).get("body"),api_news.News.get(i).get("imageurl"),api_news.News.get(i).get("url"),api_news.News.get(i).get("source")));
        }
    }

    @Override
    protected void onResume() {
        if (!Helper.connected)
            openDialogForNetworkConnection(this);
        super.onResume();
    }

    @Override
    protected void onStart() {
        Helper.connected = CheckConnection(this);
        new Helper.InternetCheck(internet -> { Helper.connected = internet; });
        super.onStart();
    }

    private void buildRecycleView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new NewsAdapter(mNewsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        setListeners(mAdapter);
    }

    private void setListeners(NewsAdapter mAdapter) {
        mAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (isDetailsActive) {
                    NewsAdapter.NewsViewHolder.setDetailsEnabled(false);
                    isDetailsActive = false;
                } else {
                    NewsAdapter.NewsViewHolder.setDetailsEnabled(true);
                    isDetailsActive = true;
                }
            }
        });
    }


    private void backbutton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onPreExecute() {
        pgsBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute(Map<Integer, Map<String, String>> news) {
        pgsBar.setVisibility(View.GONE);
        pullDataFromApiToArray();
        buildRecycleView();
    }
}