package com.developer.jc.popularmoviesapp.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Content Provider for PopularMovies
 */
public class MoviesProvider extends ContentProvider{

    /**
     * Initialize the provider.The Android system calls this mtehod immediately after it creates
     * the provider. The content provider object is not created until the content resolver object
     * attempts to access it
     * @return whether or not the object was created
     */
    @Override
    public boolean onCreate() {
        return false;
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
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    /**
     * Returns a MIME type corresponding to a content URI
     * @param uri
     * @return
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
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
        return null;
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
        return 0;
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
        return 0;
    }
}
