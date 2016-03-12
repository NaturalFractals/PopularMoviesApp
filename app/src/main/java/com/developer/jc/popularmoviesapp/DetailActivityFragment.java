package com.developer.jc.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.jc.popularmoviesapp.adapters.ReviewAdapter;
import com.developer.jc.popularmoviesapp.adapters.TrailerAdapter;
import com.developer.jc.popularmoviesapp.content.MoviesContract;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private TextView movieTitle;
    private TextView movieReleaseDate;
    private TextView movieDescription;
    private ImageView movieImage;
    private TextView movieVoteAverage;
    private Button movieFavorite;
    private ListView movieTrailerView;
    private ListView movieReviewView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private String mTitle;
    private String mReleaseDate;
    private String mDescription;
    private String mImage;
    private String mVoteAverage;

    private String[] mTrailers;
    private String[] mNames;
    private String[] mReviews;

    private static DecimalFormat REAL_FORMATTER = new DecimalFormat("0.###");

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        //Get Intent from Main Activity Fragment
        Intent intent = getActivity().getIntent();
        //Bind Views for layout
        movieTitle = (TextView) view.findViewById(R.id.movieTitleLabel);
        movieDescription = (TextView) view.findViewById(R.id.movieDescriptionLabel);
        movieReleaseDate = (TextView) view.findViewById(R.id.releaseDateLabel);
        movieImage = (ImageView) view.findViewById(R.id.moviePictureLabel);
        movieVoteAverage = (TextView) view.findViewById(R.id.voteAverageLabel);
        movieFavorite = (Button) view.findViewById(R.id.favoritesButton);
        movieTrailerView = (ListView) view.findViewById(R.id.trailerList);
        movieReviewView = (ListView) view.findViewById(R.id.reviewList);

        mVoteAverage = REAL_FORMATTER.format(intent.getExtras().getDouble("vote"));
        mTitle = intent.getExtras().getString("title");
        mDescription = (intent.getExtras().getString("overview"));
        mReleaseDate = intent.getExtras().getString("releaseDate");
        mImage = intent.getExtras().getString("posterPath");
        Bundle bt = intent.getBundleExtra("trailer");
        mTrailers = bt.getStringArray("trailer");
        Bundle br = intent.getBundleExtra("reviews");
        mReviews = br.getStringArray("reviews");
        Bundle bn = intent.getBundleExtra("names");
        mNames = bn.getStringArray("names");

        //Set format of movie release date to year only
        mReleaseDate = mReleaseDate.substring(0, 4);

        //Set movie rating
        movieVoteAverage.setText(mVoteAverage + "/10");
        //Set movie Title
        movieTitle.setText(mTitle);
        //Set Movie Description
        movieDescription.setText(mDescription);
        //Set movie release date
        movieReleaseDate.setText(mReleaseDate);
        //Set Trailer list view
        mTrailerAdapter = new TrailerAdapter(getContext(), mTrailers);
        movieTrailerView.setAdapter(mTrailerAdapter);
        //Set Review List View
        mReviewAdapter = new ReviewAdapter(getContext(), mNames, mReviews);
        movieReviewView.setAdapter(mReviewAdapter);

        //Start youtube video on click of list view
        movieTrailerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTrailers[position])));
            }
        });

        //Set movie Image
        Picasso.with(getContext())
                .load(mImage)
                .into(movieImage);

        final long stringId = intent.getExtras().getInt("id");

        boolean isFavorite = false;

        movieFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMovie(stringId);
            }
        });
        return view;
    }

    /**
     * Inserts a movie into the database
     * @param id ID of movie to insert
     */
    private void insertMovie(long id){
        final Uri uri = MoviesContract.MovieEntry.buildMovieUri(id);
        final Cursor cursor = getActivity().getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if(cursor != null && !cursor.moveToFirst()) {
            final ContentValues values = new ContentValues();

            values.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, id);
            values.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, mImage);
            values.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, mDescription);
            values.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, mReleaseDate);
            values.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mTitle);
            values.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, mVoteAverage);

            getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);
        }
        cursor.close();
    }

    private void deleteMovie(long id){
        final Uri uri = MoviesContract.MovieEntry.buildMovieUri(id);
        final Cursor cursor = getActivity().getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if(cursor != null && !cursor.moveToFirst()) {
            final ContentValues values = new ContentValues();

        }
    }

    public void refreshTrailers(Intent intent){
        mTrailerAdapter.notifyDataSetChanged();
    }


    
}
