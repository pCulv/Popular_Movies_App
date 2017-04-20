package com.example.phil.popularmovies;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by phil on 4/19/17.
 */

public class Deserializer implements JsonDeserializer<Movie> {
    @Override
    public Movie deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        //Get the movie results from the parsed JSON
        JsonElement results = json.getAsJsonObject().get("results");

        return new Gson().fromJson(results, Movie.class);
    }


}
