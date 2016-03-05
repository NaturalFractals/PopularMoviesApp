package com.developer.jc.popularmoviesapp.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.developer.jc.popularmoviesapp.Movie;

/**
 * Content Provider for PopularMovies
 */
public class MoviesProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mMoviesDBHelper;

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String contentAuthority = MoviesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(contentAuthority, MoviesContract.PATH_MOVIES, MOVIE);
        uriMatcher.addURI(contentAuthority, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    /**
     * Initialize the provider.The Android system calls this mtehod immediately after it creates
     * the provider. The content provider object is not created until the content resolver object
     * attempts to access it
     * @return whether or not the object was created
     */
    @Override
    public boolean onCreate() {
        mMoviesDBHelper = new MoviesDBHelper(getContext());
        return true;
    }

    /**
     * Retrieves data from the provider. A cursor object is returned
     * @param uri Universal Resource Identifier of query
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull final Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursorToReturn;
        switch(sUriMatcher.match(uri)) {
            case MOVIE: {
                cursorToReturn = mMoviesDBHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                return cursorToReturn;
            }
            case MOVIE_WITH_ID:{
                cursorToReturn = mMoviesDBHelper.getReadableDatabase().query(
                        MoviesContract.MovieEntry.TABLE_NAME,
                        projection,
                        MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                return cursorToReturn;
            }
            default:{
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Returns a MIME type corresponding to a content URI
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case MOVIE: {
                return MoviesContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_ID:{
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unkown uri:" + uri);
            }
        }
    }

    /**
     * Inserts a new row into the provider. Returns a content URI from the newly inserted row.
     * @param uri
     * @param values
     * @return content URI from newly inserted row
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        Uri uriToReturn;
        switch(sUriMatcher.match(uri)){
            case MOVIE: {
                long _id = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);
                if(_id <= 0) {
                    Toast.makeText(getContext(), "not added", Toast.LENGTH_LONG).show();
                }
                if(_id > 0){
                    uriToReturn = MoviesContract.MovieEntry.buildMovieUri(_id);
                    Toast.makeText(getContext(), "added!!!", Toast.LENGTH_LONG).show();
                } else {
                    throw new android.database.SQLException("Failed to insert row");
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unkown uri:" + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return uriToReturn;
    }

    /**
     * Deletes a row from the content provider.
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        switch(match){
            case MOVIE:{
                numDeleted = db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                //reset ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MovieEntry.TABLE_NAME + "'");

                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri:" + uri);
            }

        }
        return numDeleted;
    }

    /**
     * Updates an existing row in the provider.
     * @param uri Universal Resource Identifier of row
     * @param values
     * @param selection
     * @param selectionArgs
     * @return number of rows updated
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesDBHelper.getWritableDatabase();
        int numUpdated = 0;

        if(values == null){
            throw new IllegalArgumentException("Cannot have null content values.");
        }

        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                numUpdated = db.update(MoviesContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                break;
            }
            default:{
                throw new UnsupportedOperationException("Uknnown uri:" + uri);
            }
        }

        if(numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numUpdated;
    }
}
