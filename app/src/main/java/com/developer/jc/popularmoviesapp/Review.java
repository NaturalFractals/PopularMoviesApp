package com.developer.jc.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 3j1ka9cjk119fj2nda on 2/29/2016.
 */
public class Review{
    private String review;
    private String name;

    public Review(){}

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Review(String name, String rev){
        this.name = name;
        this.review = rev;
    }
}
