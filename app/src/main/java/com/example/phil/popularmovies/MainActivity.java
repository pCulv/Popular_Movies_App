package com.example.phil.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.phil.popularmovies.ui.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{

    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;

    private ArrayList<Movie> mMovies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_main);
        //Xml reference for the RecyclerView
        mMovieList = (RecyclerView) findViewById(R.id.recyclerView);
        //Layout manager for movie_list_item set to display items in a grid with a span of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);
        mMovieList.setLayoutManager(gridLayoutManager);

        // Child layout size will be fixed in the RecyclerView
        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(MainActivity.this, mMovies);

        mMovieList.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return true;
    }

    public static class MovieLoader extends AsyncTaskLoader {

        public MovieLoader(Context context) {
            super(context);
        }

        @Override
        public Object loadInBackground() {


            return null;
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }
}
