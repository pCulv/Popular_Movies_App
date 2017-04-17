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
    public static final String API_KEY = "";
    public static final String KEY_PARAM = "?api_key";


    @GET("/movie/{id}")
    Call<List<Movie>> getId(@Path("id") String id);

}
