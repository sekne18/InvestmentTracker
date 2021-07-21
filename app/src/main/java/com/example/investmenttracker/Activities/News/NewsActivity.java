package com.example.investmenttracker.Activities.News;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.investmenttracker.API.API_News;
import com.example.investmenttracker.Adapters.NewsAdapter;
import com.example.investmenttracker.Database.model.News;
import com.example.investmenttracker.Helper;
import com.example.investmenttracker.R;

import java.util.ArrayList;

import static com.example.investmenttracker.Helper.CheckConnection;
import static com.example.investmenttracker.Helper.openDialogForNetworkConnection;


public class NewsActivity extends AppCompatActivity {

    API_News api_news;
    private ImageView img;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private boolean isDetailsActive;
    private ArrayList<News> mNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        backbutton();
        mNewsList = new ArrayList<News>();
        api_news = new API_News();
        mRecyclerView = this.findViewById(R.id.recyclerNews);
        api_news.RefreshDataFromAPI();

        while (api_news.News.isEmpty()) {
            Log.i("Cakanje","Cakanje na podatke novic");
        }

        pullDataFromApiToArray();
        buildRecycleView();

    }

    private void pullDataFromApiToArray() {
        for (int i = 0; i < api_news.News.size(); i++) {
            mNewsList.add(new News(api_news.News.get(i).get("title"),api_news.News.get(i).get("body"),api_news.News.get(i).get("imageurl"),api_news.News.get(i).get("url")));
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
}