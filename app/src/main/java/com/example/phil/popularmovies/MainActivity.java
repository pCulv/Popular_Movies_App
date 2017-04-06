package com.example.phil.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Xml reference for the RecyclerView
        mMovieList = (RecyclerView) findViewById(R.id.recyclerView);
        //Layout manager for movie_list_item set to display items in a grid with a span of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);
        mMovieList.setLayoutManager(gridLayoutManager);

        // Child layout size will be fixed in the RecyclerView
        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);

        mMovieList.setAdapter(mAdapter);
    }
}
