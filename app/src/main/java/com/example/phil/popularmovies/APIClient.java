package com.example.phil.popularmovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by phil on 4/9/17.
 */

public interface APIClient {


    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "50702822a126f3d3f8288773eab942a6";
    public static final String KEY_PARAM = "?api_key";

    @GET("/movie/{popular}")
    Call<List<Movie>> getMovies(@Path("popular") String popular);
}
