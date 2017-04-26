package com.example.phil.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private Movie movieData = new Movie();
    private List<Movie> mMovies;


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




//        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png").into(posterImageView);
    }


    }

