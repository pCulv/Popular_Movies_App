package com.example.phil.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.phil.popularmovies.ui.MovieAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;

    private List<Movie> mMovies = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();

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

        //Retrofit network request

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(Movie.class, new Deserializer())
                        .create();



            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create(gson));

            Retrofit retrofit = builder.client(httpClient.build()).build();

            // Create REST adapter which points to API endpoint
            APIClient client = retrofit.create(APIClient.class);

            // TODO: Implement switch statement based on if the user decides to sort by Popular
            // or top rated movies
            // Fetch Popular Movies
            Call<List<Movie>> call = client.getPopular("50702822a126f3d3f8288773eab942a6");


            call.enqueue(new Callback<List<Movie>>() {
                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    List<Movie> movies = response.body();


                    mMovieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                    Log.i("Url", response.raw().toString());
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });

        }


    }

