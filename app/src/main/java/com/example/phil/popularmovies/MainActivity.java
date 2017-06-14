package com.example.phil.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import java.util.List;

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
    private Call<List<Movie>> mCall;
    private APIClient mClient;
    private static final int FAVORITES_LOADER = 0;



    private List<Movie> mMovies = new ArrayList<>();
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

                mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, movies));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

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
                mCall = mClient.getPopular(getString(R.string.api_key));

                mCall.enqueue(new Callback<List<Movie>>() {

                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        List<Movie> movies = response.body();

                        mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, movies));
                        Log.i("Url", response.raw().toString());
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                break;
            case R.id.action_sort_by_rating:
                //api call for top rated
                mCall = mClient.getTopRated(getString(R.string.api_key));

                mCall.enqueue(new Callback<List<Movie>>() {

                    @Override
                    public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                        List<Movie> movies = response.body();

                        mRecyclerView.setAdapter(new MovieAdapter(MainActivity.this, movies));
                        Log.i("Url", response.raw().toString());
                    }

                    @Override
                    public void onFailure(Call<List<Movie>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                        t.printStackTrace();
                    }
                });
                break;
            //Sorts recyclerview by favorite movies
            case R.id.action_sort_by_favorite:
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

                        favlist.add(movieIdIndex, favMovie);
                    }
                }


        }
        return super.onOptionsItemSelected(item);
    }

    //TODO: add app lifecycle awareness
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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
        mAdapter = new MovieAdapter(this, favlist);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

