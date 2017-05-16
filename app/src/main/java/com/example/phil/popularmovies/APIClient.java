package com.example.phil.popularmovies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by phil on 4/9/17.
 */

public interface APIClient {

    @GET("/3/movie/popular")
    Call<List<Movie>> getPopular(@Query("api_key") String API_KEY);

    @GET("/3/movie/top_rated")
    Call<List<Movie>> getTopRated(@Query("api_key") String API_KEY);

    // TODO: change to use List<Review>
    @GET("/3/movie/{id}/reviews")
    Call<List<Review>> getReviews(@Path("id") String id, @Query("api_key") String API_KEY);

    @GET("/3/movie/{id}/videos")
    Call<List<Movie>> getTrailer(@Path("id") String id, @Query("api_key") String API_KEY);


}
