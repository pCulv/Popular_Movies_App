package com.example.phil.popularmovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by phil on 4/9/17.
 */

public interface APIClient {

    @GET("/3/movie/popular")
    Call<List<Movie>> getPopular(@Query("api_key") String API_KEY);

    @GET("/3/movie/top_rated")
    Call<List<Movie>> getTopRated(@Query("api_key") String API_KEY);

    @GET("/3/movie/{movie_id}")
    Call<List<Movie>> getDetails(@Query("api_key") String API_KEY);



}
