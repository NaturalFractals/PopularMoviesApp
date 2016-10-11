package com.developer.jc.popularmoviesapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.jc.popularmoviesapp.R;

/**
 * This adapter is used to load the movie trailers into the list view on the details page
 * @author Jesse Cochran
 */
public class TrailerAdapter extends BaseAdapter{
    private Context mContext;
    private String[] trailers;
    private LayoutInflater inflater;

    public TrailerAdapter(Context context, String[] trls){
        super();
        this.mContext = context;
        this.trailers = trls;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(trailers != null) {
            return trailers.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(trailers != null) {
            return trailers[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_item_trailer, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.trailerPlay);
        imageView.setImageResource(R.drawable.ic_youtube);
        TextView textView = (TextView) convertView.findViewById(R.id.youtubeLink);

        return convertView;
    }
}
