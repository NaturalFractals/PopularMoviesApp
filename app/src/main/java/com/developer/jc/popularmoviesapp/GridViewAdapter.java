package com.developer.jc.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    private LayoutInflater inflater;
    public GridViewAdapter(Context context, Movie[] movies) {
        super();
        this.mContext = context;
        this.mMovieList = movies;

        inflater = LayoutInflater.from(mContext);
    }

    public void setData(List<Movie> movies){
        if(movies != null){
            for(int i = 0; i < movies.size(); i++) {
                mMovieList[i] = movies.get(i);
            }
        }
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

        if(convertView == null){
            convertView = inflater.inflate(R.layout.images, parent, false);
        }

        ViewHolder holder = new ViewHolder();
        holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
        if(mMovieList[position] != null) {
            String url = mMovieList[position].getFullPoster();
            Picasso.with(mContext).load(url)
                    .into(holder.mImageView);
        }

        return convertView;
    }
}


