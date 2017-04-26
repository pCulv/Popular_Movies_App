package com.example.phil.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phil.popularmovies.APIClient;
import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class DetailActivity extends AppCompatActivity {
    private Call<List<Movie>> mCall;
    private APIClient mClient;
    private Context mContext;
    private Movie movieData = new Movie();
    private List<Movie> mMovies;
    int position;

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
            voteAverageView.setText(voteAverage.toString());
            overview = bundle.getString("overview");
            descriptionView.setText(overview);


        }


        Uri builder = Uri.parse("http://image.tmdb.org/t/p/w500").buildUpon()
                .appendEncodedPath(movieData.getPosterPath()).build();

        Picasso.with(this)
                .load(builder)
                .into(posterImageView);

    }


    }

