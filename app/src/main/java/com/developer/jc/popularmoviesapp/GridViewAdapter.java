package com.developer.jc.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 3j1ka9cjk119fj2nda on 1/23/2016.
 */
public class GridViewAdapter extends BaseAdapter{
    private Context mContext;
    private Movie[] mMovieList;

    public GridViewAdapter(Context context, Movie[] movies) {
        this.mContext = context;
        this.mMovieList = movies;
    }

    @Override
    public int getCount() {
        return mMovieList.length;
    }

    @Override
    public Object getItem(int position) {
        return mMovieList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(mMovieList[position].getPosterPath()).into(imageView);
        return imageView;
    }
}


