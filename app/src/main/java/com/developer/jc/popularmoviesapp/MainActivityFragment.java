package com.developer.jc.popularmoviesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // Number of movies to display
    int moviesToDisplay = 20;
    // Grid View
    GridView mGridView;

    /**
     * Null Constructor
     */
    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If spinner ratings is chosen update movies
        if (id == R.id.action_rating) {
            updateMovies(1);
            return true;
        }
        //If spinner popular is chosen update movies
        if(id == R.id.action_popular){
            updateMovies(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Executes the async task of fetching movies from the API
     */
    public void updateMovies(int i) {
        ConnectivityManager cm =
            (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            FetchMovies fetchMovies = new FetchMovies(getContext(), mGridView);
            if (i == 1) {
                fetchMovies.execute(getContext().getResources().getString(R.string.action_popular));
            }
            if (i == 2) {
                fetchMovies.execute(getContext().getResources().getString(R.string.action_highest_rating));
            }
        } else {
            Toast.makeText(getContext(), "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

}


