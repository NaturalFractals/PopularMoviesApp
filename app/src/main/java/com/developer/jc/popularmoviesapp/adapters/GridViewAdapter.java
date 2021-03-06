package com.developer.jc.popularmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.developer.jc.popularmoviesapp.Movie;
import com.developer.jc.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for loading images into a Grid View
 * @author Jesse Cochran
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

    /**
     * This method takes a list of movies as the parameter and sets the local instance
     * variable to this list of movies. This allows the adapter to properly access the
     * list of movies.
     * @param movies List of movies from fetchMovies task
     */
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
        //Create new view holder, and use picasso to load movies into the view holder
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


