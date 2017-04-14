package com.example.phil.popularmovies;

import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by phil on 4/9/17.
 */

public class APIBuilder implements RequestInterceptor {


    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = "";
    private static final String KEY_PARAM = "?api_key";
    private static APIMethods api;
    private static APIBuilder instance;

    public APIBuilder() {

    }
    private   interface APIMethods{
        @GET("/movie/popular")
        ListResponse getPopular();
        @GET("/movie/top_rated")
        ListResponse getTopRated();
        @GET("/movie/{id}")
        Movie getMovieDetails(@Path("id") String id);
    }
    public static APIBuilder getInstance() {
        if(instance== null) {
            instance = new APIBuilder();
        }
        return instance;
    }
    private APIMethods getApi() {
        if (api == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setRequestInterceptor(this)
                    .setEndpoint(APIBuilder.BASE_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            api = restAdapter.create(APIMethods.class);

        } return api;
    }

    @Override
    public void intercept(RequestFacade requestFacade) {
        requestFacade.addQueryParam(APIBuilder.KEY_PARAM, APIBuilder.API_KEY);
    }

    public List<Movie> getPopular() {
        ListResponse response = getApi().getPopular();

        return response.getResults();
    }

    public List<Movie> getTopRated() {
        ListResponse response = getApi().getTopRated();

        return response.getResults();
    }
    public Movie getMovieDetails(String id) {
        Movie movieDetails = getApi().getMovieDetails(id);
        return movieDetails;
    }

}
