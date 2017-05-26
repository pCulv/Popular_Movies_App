package com.example.phil.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by phil on 5/19/17.
 */

public class MoviesContract {

    public static final String AUTHORITY = "com.example.phil.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String PATH_FAVORITES = "favorites";

    private MoviesContract() {
    }

    private interface MovieColumns {
        String COLUMN_NAME_MOVIE_ID = "movie_id";
    }

    public static final class MoviesEntry implements MovieColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
    }

    public static final class FavoriteMoviesEntry implements MovieColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorite_movies";
    }
}
