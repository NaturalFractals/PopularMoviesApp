package com.developer.jc.popularmoviesapp;

/**
 * Represents a Trailer for a Movie
 * @author Jesse Cochran
 */
public class Trailer{
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = "http://youtube.com/watch?v=" + key;

    }

}
