package com.example.phil.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.phil.popularmovies.data.FavContract.FavoriteEntry.TABLE_NAME;



public class FavoritesProvider extends ContentProvider {

    private FavDbHelper mFavDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, TABLE_NAME, MOVIE);
        matcher.addURI(authority, TABLE_NAME + "/#", MOVIE_WITH_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavDbHelper = new FavDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection
            , @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the Movie directory
            case MOVIE:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID: {
                retCursor = mFavDbHelper.getReadableDatabase()
                        .query(FavContract.FavoriteEntry.TABLE_NAME, projection,
                                FavContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ? ", selectionArgs,
                                null, null, sortOrder);

                break;
            }
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("getType has not been implemented.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri =
                            ContentUris.withAppendedId(FavContract.FavoriteEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case MOVIE_WITH_ID:
                String movieId = uri.getPath();
                ContentValues cv = new ContentValues();
                cv.put(FavContract.FavoriteEntry.COLUMN_MOVIE_ID, movieId);
                id = db.insertWithOnConflict(FavContract.FavoriteEntry.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
                returnUri = ContentUris.withAppendedId(FavContract.FavoriteEntry.CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        String id;
        String mSelection;
        String[] mSelectionArgs;
        int movieDeleted;

        switch (match) {
            case MOVIE:
                movieDeleted = db.delete(
                        FavContract.FavoriteEntry.TABLE_NAME, null, null);
                break;
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                mSelection = FavContract.FavoriteEntry.COLUMN_MOVIE_ID + "=?";
                mSelectionArgs = new String[]{id};
                movieDeleted = db.delete(
                        FavContract.FavoriteEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);


        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Update has not been implemented.");
    }
}
