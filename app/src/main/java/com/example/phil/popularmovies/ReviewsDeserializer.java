package com.example.phil.popularmovies;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by phil on 5/6/17.
 */

public class ReviewsDeserializer implements JsonDeserializer<List<Review>> {


    @Override
    public List<Review> deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {

        //Get the movie results from the parsed JSON
        JsonElement results = json.getAsJsonObject().get("results").getAsJsonArray();

        return new Gson().fromJson(results, type);

    }


}

