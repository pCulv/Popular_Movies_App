package com.example.phil.popularmovies;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by phil on 4/9/17.
 */

public interface APIClient {

    @GET("/3/movie/popular")
    Call<ArrayList<Movie>> getPopular(@Query("api_key") String API_KEY);

    @GET("/3/movie/top_rated")
    Call<ArrayList<Movie>> getTopRated(@Query("api_key") String API_KEY);

    @GET("/3/movie/{id}/reviews")
    Call<ArrayList<Review>> getReviews(@Path("id") String id, @Query("api_key") String API_KEY);

    @GET("/3/movie/{id}/videos")
    Call<ArrayList<Video>> getTrailer(@Path("id") String id, @Query("api_key") String API_KEY);




}
