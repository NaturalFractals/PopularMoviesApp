package com.developer.jc.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class represents a Movie Review
 * @author Jesse Cochran
 */
public class Review{
    private String[] review;
    private String[] name;

    public Review(){}

    public String[] getReview() {
        return review;
    }

    public void setReview(String[] review) {
        this.review = review;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public Review(String[] name, String[] rev){
        this.name = name;
        this.review = rev;
    }
}
