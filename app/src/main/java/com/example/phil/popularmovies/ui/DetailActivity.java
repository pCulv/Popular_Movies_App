package com.example.phil.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.popularmovies.APIClient;
import com.example.phil.popularmovies.AndroidDatabaseManager;
import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.example.phil.popularmovies.Review;
import com.example.phil.popularmovies.ReviewsDeserializer;
import com.example.phil.popularmovies.Video;
import com.example.phil.popularmovies.VideoDeserializer;
import com.example.phil.popularmovies.data.FavContract;
import com.example.phil.popularmovies.data.FavDbHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private ReviewsAdapter mAdapter;
    private VideoAdapter mVideoAdapter;
    private Call<ArrayList<Review>> mCall;
    private Call<ArrayList<Video>> mVideoCall;
    private APIClient mClient;
    private ArrayList<Review> mReviews = new ArrayList<>();
    private ArrayList<Video> mVideos = new ArrayList<>();
    Uri mNewUri;
    ContentValues mNewContentValues = new ContentValues();
    int rowsDeleted;

    private FavDbHelper favDbHelper = new FavDbHelper(this);

    @BindView(R.id.videos_recyclerView)
    RecyclerView videoRecyclerView;

    @BindView(R.id.reviews_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.movie_title)
    TextView movieTitleView;

    @BindView(R.id.details_poster)
    ImageView posterImageView;

    @BindView(R.id.release_date)
    TextView releaseDateView;

    @BindView(R.id.vote_average)
    TextView voteAverageView;

    @BindView(R.id.movie_description)
    TextView descriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        //adds up button to actionbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String originalTitle;
        String releaseDate;
        String formattedDate;
        Double voteAverage;
        String overview;


        Intent userClick = getIntent();
        Bundle bundle = userClick.getExtras();


        if (bundle != null) {
            originalTitle = bundle.getString("original_title");
            movieTitleView.setText(originalTitle);
            releaseDate = bundle.getString("release_date");
            formattedDate = formatDateFromString("yyyy-MM-dd", "M-dd-yyyy", releaseDate);
            releaseDateView.setText("Release Date: " + formattedDate);
            voteAverage = bundle.getDouble("vote_average");
            voteAverageView.setText(voteAverage.toString() + "/10");
            overview = bundle.getString("overview");
            descriptionView.setText(overview);
        }

        Movie movieData = getIntent().getParcelableExtra("movie");

        Uri builder = Uri.parse("http://image.tmdb.org/t/p/original").buildUpon()
                .appendEncodedPath(movieData.getPosterPath()).build();

        Picasso.with(this)
                .load(builder)
                .into(posterImageView);

        /*RecyclerView code
        Will display a list of user movie reviews
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new ReviewsAdapter(DetailActivity.this, mReviews);

        recyclerView.setAdapter(mAdapter);

        //RecyclerView code for Videos
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1,
                LinearLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(gridLayoutManager);

        videoRecyclerView.setHasFixedSize(true);
        mVideoAdapter = new VideoAdapter(DetailActivity.this, mVideos);

        videoRecyclerView.setAdapter(mVideoAdapter);


        //Retrofit network request to fetch movie reviews

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Type listType = new TypeToken<ArrayList<Review>>() {
        }.getType();


        Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(listType, new ReviewsDeserializer())
                        .create();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = retrofitBuilder.client(httpClient.build()).build();

        // Create REST adapter which points to API endpoint
        mClient = retrofit.create(APIClient.class);

        String movieId = movieData.getId().toString();

        // Fetch Movie Reviews
        mCall = mClient.getReviews(movieId, getString(R.string.api_key));


        mCall.enqueue(new Callback<ArrayList<Review>>() {

            @Override
            public void onResponse(Call<ArrayList<Review>> call, Response<ArrayList<Review>> response) {
                ArrayList<Review> reviews = response.body();

                recyclerView.setAdapter(new ReviewsAdapter(DetailActivity.this, reviews));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Review>> call, Throwable t) {
                Toast.makeText(DetailActivity.this
                        , "Network error, couldn't display User Reviews",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

        //Retrofit network request to fetch movie reviews

        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();

        Type listType1 = new TypeToken<ArrayList<Video>>() {
        }.getType();


        Gson gson1 =
                new GsonBuilder()
                        .registerTypeAdapter(listType1, new VideoDeserializer())
                        .create();

        Retrofit.Builder retrofitBuilder1 = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson1));

        Retrofit retrofit1 = retrofitBuilder1.client(httpClient1.build()).build();

        // Create REST adapter which points to API endpoint
        mClient = retrofit1.create(APIClient.class);


        // Fetch Movie Trailers

        mVideoCall = mClient.getTrailer(movieId, getString(R.string.api_key));


        mVideoCall.enqueue(new Callback<ArrayList<Video>>() {

            @Override
            public void onResponse(Call<ArrayList<Video>> call, Response<ArrayList<Video>> response) {
                ArrayList<Video> videos = response.body();

                videoRecyclerView.setAdapter(new VideoAdapter(DetailActivity.this, videos));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Video>> call, Throwable t) {
                Toast.makeText(DetailActivity.this
                        , "Network error, couldn't display Trailers",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });


    }


    public static String formatDateFromString(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e(TAG, "ParseException - dateFormat");
        }

        return outputDate;


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_details, menu);
        menu.findItem(R.id.action_share).setVisible(true);
        MenuItem item = menu.findItem(R.id.action_set_as_favorite);
        item.setVisible(true);
        item.setIcon(isFavorite() ? R.drawable.fav_add : R.drawable.fav_remove);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Movie movieData = getIntent().getParcelableExtra("movie");
        String movieId = movieData.getId().toString();
        String movieTitle = movieData.getOriginalTitle();
        String movieRating = movieData.getVoteAverage().toString();
        String moviePoster = movieData.getPosterPath();
        String movieOv = movieData.getOverview();
        String movieRelease = movieData.getReleaseDate();
        String mSelectionClause = FavContract.FavoriteEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] mSelectionArgs = {movieId + "%"};



        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey check out this movie!");
                share.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/"
                        .concat(movieId));
                startActivity(Intent.createChooser(share, "Share via"));
                break;
            case R.id.action_set_as_favorite:
                if (!isFavorite()) {

                    item.setIcon(R.drawable.fav_add);
                    //inserts Movie into favorites database if it is not already present in the database
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_TITLE, movieTitle);
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_USER_RATING, movieRating);
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_POSTER_PATH, moviePoster);
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_OVERVIEW, movieOv);
                    mNewContentValues.put(FavContract.FavoriteEntry.COLUMN_RELEASE_DATE, movieRelease);

                    mNewUri = getContentResolver()
                            .insert(FavContract.FavoriteEntry.CONTENT_URI, mNewContentValues);
                    Toast.makeText(this, movieTitle + " added to favorites", Toast.LENGTH_SHORT).show();
                } else if (isFavorite()) {

                    item.setIcon(R.drawable.fav_remove);
                    rowsDeleted = getContentResolver().delete(
                            FavContract.FavoriteEntry.CONTENT_URI,
                            mSelectionClause,
                            mSelectionArgs
                    );
//                    Uri uri = FavContract.FavoriteEntry
//                            .CONTENT_URI.buildUpon().appendPath(movieId).build();
//                    getContentResolver().delete(uri, "_id", null);
                    Toast.makeText(this, movieTitle + " removed from favorites", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.showDatabase:
                Intent dbmanager = new Intent(DetailActivity.this, AndroidDatabaseManager.class);
                startActivity(dbmanager);

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorite() {

        Movie movieData = getIntent().getParcelableExtra("movie");
        String movie_Id = movieData.getId().toString();
        final SQLiteDatabase db = favDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                + FavContract.FavoriteEntry.TABLE_NAME + " WHERE movie_id = '" + movie_Id + "'", null);
        boolean isFavorite = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return isFavorite;
    }
}

