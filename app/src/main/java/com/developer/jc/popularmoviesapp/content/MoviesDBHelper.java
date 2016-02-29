package com.developer.jc.popularmoviesapp.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 3j1ka9cjk119fj2nda on 2/26/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE" + MoviesContract.MovieEntry.TABLE_NAME + " (" +

                MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + "DOUBLE NOT NULL) ";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
