package com.developer.jc.popularmoviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developer.jc.popularmoviesapp.R;

import org.w3c.dom.Text;

/**
 * This adapter is used to load the movie reviews into the list view on the details page
 * @author Jesse Cochran
 */
public class ReviewAdapter extends BaseAdapter{
    private Context mContext;
    private String[] mReviews;
    private String[] mNames;
    private LayoutInflater mInflater;

    public ReviewAdapter(Context context, String[] n, String[] r){
        super();
        this.mReviews = r;
        this.mNames = n;
        this.mContext = context;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(mReviews != null)
            return mReviews.length;

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mReviews != null && mNames != null){
            return mReviews[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.list_item_review, parent, false);
        }

        TextView reviewText = (TextView) convertView.findViewById(R.id.reviewLabel);
        String review = "\"" + mReviews[position] + "\"" + "\n -" + mNames[position];
        reviewText.setText(review);

        return convertView;

    }
}
