package com.example.phil.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.phil.popularmovies.ui.MovieAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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

    private void sendNetworkRequest(Movie movie) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APIClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        APIClient client = retrofit.create(APIClient.class);
        Call<Movie> call = client.getId("id");

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                mMovieList.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
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


}
