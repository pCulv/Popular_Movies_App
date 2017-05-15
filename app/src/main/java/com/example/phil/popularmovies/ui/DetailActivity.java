package com.example.phil.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phil.popularmovies.APIClient;
import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.example.phil.popularmovies.Review;
import com.example.phil.popularmovies.ReviewsDeserializer;
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

public class DetailActivity extends AppCompatActivity {

    private ReviewsAdapter mAdapter;
    private Call<List<Review>> mCall;
    private APIClient mClient;
    private List<Review> mReviews = new ArrayList<>();

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


    }


    }

