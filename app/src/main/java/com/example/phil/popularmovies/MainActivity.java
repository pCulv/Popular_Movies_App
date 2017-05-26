package com.example.phil.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.phil.popularmovies.ui.MovieAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
    private Call<List<Movie>> mCall;
    private APIClient mClient;
    private ArrayList<Movie> favlist;

    private List<Movie> mMovies = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

        Type listType = new TypeToken<List<Movie>>() {
        }.getType();


        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(listType, new Deserializer())
                        .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(httpClient.build()).build();

        // Create REST adapter which points to API endpoint
        mClient = retrofit.create(APIClient.class);

        // Fetch Popular Movies
        mCall = mClient.getPopular(getString(R.string.api_key));


        mCall.enqueue(new Callback<List<Movie>>() {

            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                List<Movie> movies = response.body();

                mMovieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handles click events of menu items and will sort movies by popularity or top rating/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
    // If user clicks "Sort by Most Popular" in option menu
        if (id == R.id.action_sort_by_popular) {
            //api call for most popular
            mCall = mClient.getPopular(getString(R.string.api_key));

            mCall.enqueue(new Callback<List<Movie>>() {

                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    List<Movie> movies = response.body();

                    mMovieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                    Log.i("Url", response.raw().toString());
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                    t.printStackTrace();
                }
            });
        }
        // If user clicks "Sort by Rating" in options menu
        if (id == R.id.action_sort_by_rating){
            //api call for top rated
            mCall = mClient.getTopRated(getString(R.string.api_key));

            mCall.enqueue(new Callback<List<Movie>>() {

                @Override
                public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                    List<Movie> movies = response.body();

                    mMovieList.setAdapter(new MovieAdapter(MainActivity.this, movies));
                    Log.i("Url", response.raw().toString());
                }

                @Override
                public void onFailure(Call<List<Movie>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                    t.printStackTrace();
                }
            });
        }
        //If user clicks "Favorite Movies" in options menu
        if (id == R.id.action_sort_by_favorite) {

        }

        return super.onOptionsItemSelected(item);
    }

}

