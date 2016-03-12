package com.developer.jc.popularmoviesapp;

import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a movie from the database
 */
public class Movie {
    private String originalTitle;
    private String overView;
    private double voteAverage;
    private String releaseDate;
    private String posterPath;
    private int movieId;
    private Review reviews;
    private String[] trailers;

    public Movie() {}

    public Movie(Cursor cursor){
        this.movieId = cursor.getInt(MainActivityFragment.COL_MOVIE_ID);
        this.posterPath = cursor.getString(MainActivityFragment.COL_POSTER_PATH);
        this.overView = cursor.getString(MainActivityFragment.COL_OVERVIEW);
        this.originalTitle = cursor.getString(MainActivityFragment.COL_ORIGINAL_TITLE);
        this.voteAverage = cursor.getDouble(MainActivityFragment.COL_VOTE_AVERAGE);
        this.releaseDate = cursor.getString(MainActivityFragment.COL_RELEASE_DATE);
    }

    public Review getReviews() {
        return reviews;
    }

    public void setReviews(Review reviews) {
        this.reviews = reviews;
    }

    public String[] getTrailers() {
        return trailers;
    }

    public void setTrailers(String[] trailers) {
        this.trailers = trailers;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFullPoster() {
        return "http://image.tmdb.org/t/p/" + "w500" + posterPath;
    }

    public String getDeatilFullPoster() { return "http://image.tmdb.org/t/p/" + "w342" + posterPath; }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
