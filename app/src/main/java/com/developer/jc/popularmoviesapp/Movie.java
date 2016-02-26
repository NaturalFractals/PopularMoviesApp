package com.developer.jc.popularmoviesapp;

/**
 * Created by 3j1ka9cjk119fj2nda on 1/23/2016.
 */
public class Movie {
    private String originalTitle;
    private String overView;
    private double voteAverage;
    private String releaseDate;
    private String posterPath;
    private int movieId;

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
