package com.example.phil.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.popularmovies.APIClient;
import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.example.phil.popularmovies.Review;
import com.example.phil.popularmovies.ReviewsDeserializer;
import com.example.phil.popularmovies.Video;
import com.example.phil.popularmovies.VideoDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ReviewsAdapter mAdapter;
    private VideoAdapter mVideoAdapter;
    private Call<List<Review>> mCall;
    private Call<List<Video>> mVideoCall;
    private APIClient mClient;
    private List<Review> mReviews = new ArrayList<>();
    private List<Video> mVideos = new ArrayList<>();
    private RecyclerView mRecyclerView;

    //Variables for Youtube API
    public static final String API_KEY = "AIzaSyCFUvOl19U5pg56BQy3Rarev3N7RnLFtTQ";
    public static final String TEST_VIDEO_ID = "wUn05hdkhjM";

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

    @BindView(R.id.checkBox)
    CheckBox favoritesStarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        String originalTitle;
        String releaseDate;
        Double voteAverage;
        String overview;

        Intent userClick = getIntent();
        Bundle bundle = userClick.getExtras();

        if (bundle != null) {
            originalTitle = bundle.getString("original_title");
            movieTitleView.setText(originalTitle);
            releaseDate = bundle.getString("release_date");
            releaseDateView.setText(releaseDate);
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

        //Listen for clicks of favorites checkbox
        favoritesStarView.setOnClickListener(this);

        //Retrofit network request to fetch movie reviews

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Type listType = new TypeToken<List<Review>>() {
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


        mCall.enqueue(new Callback<List<Review>>() {

            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                List<Review> reviews = response.body();

                recyclerView.setAdapter(new ReviewsAdapter(DetailActivity.this, reviews));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(DetailActivity.this
                        , "Network error, couldn't display User Reviews",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });

        //Retrofit network request to fetch movie reviews

        OkHttpClient.Builder httpClient1 = new OkHttpClient.Builder();

        Type listType1 = new TypeToken<List<Video>>() {
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


        // Fetch Movie Reviews
        mVideoCall = mClient.getTrailer(movieId, getString(R.string.api_key));


        mVideoCall.enqueue(new Callback<List<Video>>() {

            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                List<Video> videos = response.body();

                videoRecyclerView.setAdapter(new VideoAdapter(DetailActivity.this, videos));
                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Toast.makeText(DetailActivity.this
                        , "Network error, couldn't display Trailers",
                        Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });



    }

    @Override
    public void onClick(View v) {

    }
}

