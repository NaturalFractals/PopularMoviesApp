package com.developer.jc.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.developer.jc.popularmoviesapp.content.MoviesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // Number of movies to display
    int moviesToDisplay = 20;
    // Grid View
    GridView mGridView;
    // Grid Adapter
    GridViewAdapter mGridViewAdapter;

    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MovieEntry._ID,
            MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
            MoviesContract.MovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    public static final int COL_MOVIE_ID = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_ORIGINAL_TITLE = 3;
    public static final int COL_OVERVIEW = 4;
    public static final int COL_RELEASE_DATE = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    private Movie[] mMoviesList;

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
        //If spinner favorites is chosen load movies from SQL database
        if(id == R.id.action_favorites){
            updateMovies(3);
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
            if(i == 3){
                FetchFavoriteMoviesTask fetchFavoriteMoviesTask = new FetchFavoriteMoviesTask(getContext());
                fetchFavoriteMoviesTask.execute();
            }
        } else {
            Toast.makeText(getContext(), "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MoviesContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoritesFromCursor(cursor);
        }

        private List<Movie> getFavoritesFromCursor(Cursor cursor) {
            List<Movie> result = new ArrayList<Movie>();
            if(cursor.moveToFirst() && cursor != null) {
                do{
                    Movie movie = new Movie(cursor);
                    result.add(movie);
                } while(cursor.moveToNext());
            }
            return result;
        }

        @Override
        protected void onPostExecute(final List<Movie> mMoviesList) {
            super.onPostExecute(mMoviesList);
            if(mMoviesList != null){
                if(mGridViewAdapter != null) {
                    mGridViewAdapter.setData(mMoviesList);
                }
                MainActivityFragment.this.mMoviesList = new Movie[20];
                if(mMoviesList != null) {
                    for (int i = 0; i < mMoviesList.size(); i++) {
                        MainActivityFragment.this.mMoviesList[i] = mMoviesList.get(i);
                    }
                }
                if(MainActivityFragment.this.mMoviesList != null) {
                    mGridViewAdapter = new GridViewAdapter(getContext(), MainActivityFragment.this.mMoviesList);
                    mGridViewAdapter.setData(mMoviesList);
                    mGridView.setAdapter(mGridViewAdapter);
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), DetailActivity.class);
                            intent.putExtra("id", mMoviesList.get(position).getMovieId());
                            intent.putExtra("posterPath", mMoviesList.get(position).getDeatilFullPoster());
                            intent.putExtra("title", mMoviesList.get(position).getOriginalTitle());
                            intent.putExtra("releaseDate", mMoviesList.get(position).getReleaseDate());
                            intent.putExtra("overview", mMoviesList.get(position).getOverView());
                            intent.putExtra("vote", mMoviesList.get(position).getVoteAverage());
                            getContext().startActivity(intent);
                        }
                    });
                }
            }

        }
    }


}


