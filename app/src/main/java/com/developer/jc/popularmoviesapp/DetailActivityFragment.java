package com.developer.jc.popularmoviesapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

        //Set movie rating
        movieVoteAverage.setText(REAL_FORMATTER.format(intent.getExtras().getDouble("vote")));
        //Set movie Title
        movieTitle.setText(intent.getExtras().getString("title"));
        //Set Movie Description
        movieDescription.setText((intent.getExtras().getString("overview")));
        //Set movie release date
        movieReleaseDate.setText((intent.getExtras().getString("releaseDate")));
        //Set movie Image
        Picasso.with(getContext())
                .load(intent.getExtras().getString("posterPath"))
                .into(movieImage);

        return view;
    }
}
