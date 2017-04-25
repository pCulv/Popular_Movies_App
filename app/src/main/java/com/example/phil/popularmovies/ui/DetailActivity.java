package com.example.phil.popularmovies.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phil.popularmovies.APIClient;
import com.example.phil.popularmovies.Deserializer;
import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
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
    private Call<List<Movie>> mCall;
    private APIClient mClient;
    private Context mContext;
    private Movie movie;

//    @BindView(R.id.movie_title)
//    TextView movieTitleView;

    @BindView(R.id.details_poster)
    ImageView posterImageView;

//    @BindView(R.id.release_date)
//    TextView releaseDateView;
//
//    @BindView(R.id.runtime)
//    TextView runtimeView;
//
//    @BindView(R.id.movie_rating)
//    TextView ratingView;
//
//    @BindView(R.id.movie_description)
//    TextView descriptionView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_detail);

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
        mCall = mClient.getDetails(getString(R.string.api_key));



        mCall.enqueue(new Callback<List<Movie>>() {

            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
//                List<Movie> movie = response.body();


                Picasso.with(mContext).load(movie.getPosterPath()).into(posterImageView);


                Log.i("Url", response.raw().toString());
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "ERROR NO NETWORK CONNECTION", Toast.LENGTH_SHORT).show();

                t.printStackTrace();
            }
        });
    }
}
