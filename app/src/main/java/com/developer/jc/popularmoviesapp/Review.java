package com.developer.jc.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 3j1ka9cjk119fj2nda on 2/29/2016.
 */
public class Review implements Parcelable{


    public Review(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Review> CREATOR
            = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
