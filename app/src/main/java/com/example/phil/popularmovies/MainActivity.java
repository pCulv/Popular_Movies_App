package com.example.phil.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.phil.popularmovies.data.FavContract;
import com.example.phil.popularmovies.ui.MovieAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter mAdapter;
    private Cursor mCursor;
    private ArrayList<Movie> favlist = new ArrayList<>();
    Movie favMovie;

    private RecyclerView mRecyclerView;


    private static final int FAVORITES_LOADER = 0;
    public static final String MOVIES_KEY = "movies_key";


    private ArrayList<Movie> mMovies = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Xml reference for the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //Layout manager for movie_list_item set to display items in a grid with a span of 2
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Child layout size will be fixed in the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(MainActivity.this, mMovies);

        mRecyclerView.setAdapter(mAdapter);


        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            mAdapter = new MovieAdapter(MainActivity.this, mMovies);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Type listType = new TypeToken<ArrayList<Movie>>() {
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
            APIClient mClient = retrofit.create(APIClient.class);

            // Fetch Popular Movies
            Call<ArrayList<Movie>> mCall = mClient.getPopular(getString(R.string.api_key));

            mCall.enqueue(new Callback<ArrayList<Movie>>() {

                @Override
                public void onResponse(Call<ArrayList<Movie>> call,
                                       Response<ArrayList<Movie>> response) {
                    mMovies = response.body();

                    mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, mMovies));
                    Log.i("Url", response.raw().toString());
                }

                @Override
                public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION",
                            Toast.LENGTH_SHORT).show();

                    t.printStackTrace();
                }
            });
        }
    }
    //method for retrieving favorite movies from SQLite database

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

        switch (id) {
            case R.id.action_sort_by_popular:
                //api call for most popular
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                Type listType = new TypeToken<ArrayList<Movie>>() {
                }.getType();

                Gson gson =
                        new GsonBuilder()
                                .registerTypeAdapter(listType, new Deserializer())
                                .create();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson));

                Retrofit retrofit = builder.client(httpClient.build()).build();
                APIClient mClient = retrofit.create(APIClient.class);
                Call<ArrayList<Movie>> mCall = mClient.getPopular(getString(R.string.api_key));

                mCall.enqueue(new Callback<ArrayList<Movie>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Movie>> call,
                                           Response<ArrayList<Movie>> response) {
                        mMovies = response.body();

                        mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, mMovies));
                        Log.i("Url", response.raw().toString());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION",
                                Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                break;
            case R.id.action_sort_by_rating:
                //api call for top rated
                OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();

                Type listType2 = new TypeToken<ArrayList<Movie>>() {
                }.getType();

                Gson gson2 =
                        new GsonBuilder()
                                .registerTypeAdapter(listType2, new Deserializer())
                                .create();

                Retrofit.Builder builder2 = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create(gson2));

                Retrofit retrofit2 = builder2.client(httpClient2.build()).build();
                APIClient mClient2 = retrofit2.create(APIClient.class);
                mCall = mClient2.getTopRated(getString(R.string.api_key));

                mCall.enqueue(new Callback<ArrayList<Movie>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Movie>> call, Response<ArrayList<Movie>> response) {
                        mMovies = response.body();

                        mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, mMovies));
                        Log.i("Url", response.raw().toString());
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Movie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                break;
            //Sorts recyclerview by favorite movies
            case R.id.action_sort_by_favorite:
                mMovies.clear();
                getSupportLoaderManager().initLoader(FAVORITES_LOADER, null, this);

                String[] projection = {
                        FavContract.FavoriteEntry.COLUMN_MOVIE_ID,
                        FavContract.FavoriteEntry.COLUMN_TITLE,
                        FavContract.FavoriteEntry.COLUMN_USER_RATING,
                        FavContract.FavoriteEntry.COLUMN_POSTER_PATH,
                        FavContract.FavoriteEntry.COLUMN_OVERVIEW,
                        FavContract.FavoriteEntry.COLUMN_RELEASE_DATE
                };

                mCursor = getContentResolver().query(FavContract.FavoriteEntry.CONTENT_URI,
                        projection, null, null, null);
                int movieIdIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_MOVIE_ID);
                int movieTitleIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_TITLE);
                int movieRatingIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_USER_RATING);
                int moviePosterIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_POSTER_PATH);
                int movieOVIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_OVERVIEW);
                int movieReleaseIndex =
                        mCursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_RELEASE_DATE);

                //query for favorite movies from content resolver and sqlite db
                if (mCursor != null) {
                    while (mCursor.moveToNext()) {
                        int movieId = mCursor.getInt(movieIdIndex);
                        String movieTitle = mCursor.getString(movieTitleIndex);
                        double userRating = mCursor.getInt(movieRatingIndex);
                        String moviePoster = mCursor.getString(moviePosterIndex);
                        String movieOV = mCursor.getString(movieOVIndex);
                        String movieRelease = mCursor.getString(movieReleaseIndex);

                        favMovie = new Movie(id, movieTitle, moviePoster);
                        favMovie.setId(movieId);
                        favMovie.setOriginalTitle(movieTitle);
                        favMovie.setVoteAverage(userRating);
                        favMovie.setPosterPath(moviePoster);
                        favMovie.setOverview(movieOV);
                        favMovie.setReleaseDate(movieRelease);


                        mMovies.add(movieIdIndex, favMovie);

                    }
                }
                break;
            case R.id.action_settings:
                Intent startSettings = new Intent(this, SettingsActivity.class);
                startActivity(startSettings);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, mMovies);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(
                MainActivity.this,
                FavContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return loader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new MovieAdapter(this, mMovies);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovies.clear();
    }
}

