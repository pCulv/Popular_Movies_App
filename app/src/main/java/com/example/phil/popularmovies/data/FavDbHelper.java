package com.example.phil.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by phil on 6/5/17.
 */

public class FavDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 3;

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;


    public FavDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavContract.FavoriteEntry
                .TABLE_NAME + " (" +
                FavContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                FavContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL" +
//                FavContract.FavoriteEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
//                FavContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
//                FavContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL" +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }

    //code for AndroidDatabaseManager
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }


    // The following code should be used if the FavoritesProvider class is removed.

//    public void addFavorite(Movie movie){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(FavContract.FavoriteEntry.COLUMN_MOVIE_ID, movie.getId());
//        values.put(FavContract.FavoriteEntry.COLUMN_TITLE, movie.getOriginalTitle());
//        values.put(FavContract.FavoriteEntry.COLUMN_USER_RATING, movie.getVoteAverage());
//        values.put(FavContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
//        values.put(FavContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
//
//        db.insert(FavContract.FavoriteEntry.TABLE_NAME, null, values);
//        db.close();
//    }
//
//    public void deleteFavorite(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(FavContract.FavoriteEntry.TABLE_NAME, FavContract.FavoriteEntry.COLUMN_MOVIE_ID+ "=" + id, null);
//    }
//
//    public List<Movie> getAllFavorite(){
//        String[] columns = {
//                FavContract.FavoriteEntry._ID,
//                FavContract.FavoriteEntry.COLUMN_MOVIE_ID,
//                FavContract.FavoriteEntry.COLUMN_TITLE,
//                FavContract.FavoriteEntry.COLUMN_USER_RATING,
//                FavContract.FavoriteEntry.COLUMN_POSTER_PATH,
//                FavContract.FavoriteEntry.COLUMN_OVERVIEW
//
//        };
//        String sortOrder =
//                FavContract.FavoriteEntry._ID + " ASC";
//        List<Movie> favoriteList = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(FavContract.FavoriteEntry.TABLE_NAME,
//                columns,
//                null,
//                null,
//                null,
//                null,
//                sortOrder);
//
//        if (cursor.moveToFirst()){
//            do {
//                Movie movie = new Movie();
//                movie.setId(Integer.parseInt(cursor.getString(cursor
//                        .getColumnIndex(FavContract.FavoriteEntry.COLUMN_MOVIE_ID))));
//                movie.setOriginalTitle(cursor.getString(cursor
//                        .getColumnIndex(FavContract.FavoriteEntry.COLUMN_TITLE)));
//                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor
//                        .getColumnIndex(FavContract.FavoriteEntry.COLUMN_USER_RATING))));
//                movie.setPosterPath(cursor.getString(cursor
//                        .getColumnIndex(FavContract.FavoriteEntry.COLUMN_POSTER_PATH)));
//                movie.setOverview(cursor
//                        .getString(cursor.getColumnIndex(FavContract.FavoriteEntry.COLUMN_OVERVIEW)));
//
//                favoriteList.add(movie);
//
//            }while(cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        return favoriteList;
//    }
}
