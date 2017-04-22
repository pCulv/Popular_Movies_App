package com.example.phil.popularmovies;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by phil on 4/19/17.
 */

public class Deserializer implements JsonDeserializer<Movie> {
    @Override
    public Movie deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();
//
//
        final JsonArray jsonResultArray = jsonObject.get("results").getAsJsonArray();
//        final String[] movies = new String[jsonResultArray.size()];
//        for (int i = 0; i < movies.length; i++) {
//            final JsonElement jsonMovie = jsonResultArray.get(i);
//            movies[i] = jsonMovie.getAsString();
//        }
//
//        final Movie movie = new Movie();
//        movie.setResults(movies);



        //Get the movie results from the parsed JSON
//        JsonElement results = json.getAsJsonObject().get("results");

        Type listType = new TypeToken<ArrayList<Movie>>(){}.getType();



        return new Gson().fromJson(jsonResultArray, listType);

//        return movie;
    }


}
