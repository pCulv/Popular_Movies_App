package com.example.phil.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by phil on 5/22/17.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MoviesDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_MOVIE_ID + " TEXT UNIQUE, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_DATE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_RATING + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.FavoriteMoviesEntry.TABLE_NAME + " (" +
                MoviesContract.FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.FavoriteMoviesEntry.COLUMN_NAME_MOVIE_ID + " TEXT UNIQUE, FOREIGN KEY (" +
                MoviesContract.FavoriteMoviesEntry.COLUMN_NAME_MOVIE_ID + ") REFERENCES " +
                MoviesContract.MoviesEntry.TABLE_NAME + " ("
                + MoviesContract.MoviesEntry.COLUMN_NAME_MOVIE_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
    }
}
