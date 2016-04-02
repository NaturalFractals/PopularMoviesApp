package com.developer.jc.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.developer.jc.popularmoviesapp.adapters.GridViewAdapter;
import com.developer.jc.popularmoviesapp.content.MoviesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 * @author Jesse Cochran
 */
public class MainActivityFragment extends Fragment {
    // Number of movies to display
    int moviesToDisplay = 20;
    // Grid View
    GridView mGridView;
    // Grid Adapter
    GridViewAdapter mGridViewAdapter;
    private boolean mTwoPane;

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
    public static final int COL_OVERVIEW = 3;
    public static final int COL_RELEASE_DATE = 4;
    public static final int COL_ORIGINAL_TITLE = 5;
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
        mTwoPane = ((MainActivity)getActivity()).isTwoPane();
        updateMovies(1, mTwoPane);
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
            updateMovies(1, mTwoPane);
            return true;
        }
        //If spinner popular is chosen update movies
        if(id == R.id.action_popular){
            updateMovies(2, mTwoPane);
            return true;
        }
        //If spinner favorites is chosen load movies from SQL database
        if(id == R.id.action_favorites){
            updateMovies(3, mTwoPane);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Executes the async task of fetching movies from the API
     */
    public void updateMovies(int i, boolean twoPane) {
        mTwoPane = twoPane;
        ConnectivityManager cm =
            (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        FragmentActivity a = getActivity();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            FetchMovies fetchMovies = new FetchMovies(getContext(), mGridView, mTwoPane, a);
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

    /**
     * This class fetches the user's favorited movies from the database
     * @author Jesse Cochran
     */
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
                    for(int i = 0; i < mMoviesList.size(); i++){
                        FetchReviews fetchReviews = new FetchReviews(getContext());
                        fetchReviews.execute(mMoviesList.get(i));
                        FetchTrailers fetchTrailers = new FetchTrailers(getContext());
                        fetchTrailers.execute(mMoviesList.get(i));
                    }
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(!mTwoPane) {
                                Intent intent = new Intent(getContext(), DetailActivity.class);
                                intent.putExtra("id", mMoviesList.get(position).getMovieId());
                                intent.putExtra("posterPath", mMoviesList.get(position).getDeatilFullPoster());
                                intent.putExtra("title", mMoviesList.get(position).getOriginalTitle());
                                intent.putExtra("releaseDate", mMoviesList.get(position).getReleaseDate());
                                intent.putExtra("overview", mMoviesList.get(position).getOverView());
                                intent.putExtra("vote", mMoviesList.get(position).getVoteAverage());

                                //Create bundle of trailers to send to detail activity
                                Bundle trailerBundle = new Bundle();
                                trailerBundle.putStringArray("trailer", mMoviesList.get(position).getTrailers());
                                intent.putExtra("trailer", trailerBundle);

                                //Create bundle of reviews to send to detail activty
                                Bundle reviewBundle = new Bundle();
                                reviewBundle.putStringArray("reviews", mMoviesList.get(position).getReviews().getReview());
                                intent.putExtra("reviews", reviewBundle);

                                //Create bundle of names to send to detail activity
                                Bundle nameBundle = new Bundle();
                                nameBundle.putStringArray("names", mMoviesList.get(position).getReviews().getName());
                                intent.putExtra("names", nameBundle);

                                //Start detail activity
                                getContext().startActivity(intent);
                            } else {
                                Bundle args = new Bundle();
                                args.putInt("id", mMoviesList.get(position).getMovieId());
                                args.putString("posterPath", mMoviesList.get(position).getDeatilFullPoster());
                                args.putString("title", mMoviesList.get(position).getOriginalTitle());
                                args.putString("releaseDate", mMoviesList.get(position).getReleaseDate());
                                args.putString("overview", mMoviesList.get(position).getOverView());
                                args.putDouble("vote", mMoviesList.get(position).getVoteAverage());
                                args.putStringArray("trailer", mMoviesList.get(position).getTrailers());
                                args.putStringArray("reviews", mMoviesList.get(position).getReviews().getReview());
                                args.putStringArray("names", mMoviesList.get(position).getReviews().getName());

                                DetailActivityFragment fragment = new DetailActivityFragment();
                                fragment.setArguments(args);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_detail_twopane, fragment, "DF")
                                        .commit();
                            }
                        }
                    });
                }
            }

        }

    }


}


