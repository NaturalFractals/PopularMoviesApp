package com.developer.jc.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.PreferenceChangeListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // List of movie objects
    Movie[] movies = new Movie[20];
    // GridViewAdapter
    GridViewAdapter mGridViewAdapter;
    // Number of movies to display
    int moviesToDisplay = 20;
    // Grid View
    GridView mGridView;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateMovies(1);
            return true;
        }

        if(id == R.id.action_settings){
            updateMovies(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Executes the async task of fetching movies from the API
     */
    public void updateMovies(int i) {
        FetchMovies fetchMovies = new FetchMovies();
        if(i == 1) {
            fetchMovies.execute(getContext().getResources().getString(R.string.action_popular));
        }
        if(i == 2){
            fetchMovies.execute(getContext().getResources().getString(R.string.action_refresh));
        }
    }

    public class FetchMovies extends AsyncTask<String, Void, Void> {
        BufferedReader reader;
        HttpURLConnection urlConnection;
        JSONArray results;
        JSONObject json;

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String query = "";
            if(params[0] == getContext().getResources().getString(R.string.action_popular)) {
                query = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default_value));
            } else if (params[0] == getContext().getResources().getString(R.string.action_refresh)){
                query = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_value));
            }

            //Api key for moviedb request
            String apiKey = "ad5fab0d067530588fcc840ad9ff35de";
            try {
                final String FETCH_MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY = "sort_by";
                final String API_KEY = "api_key";

                //create the request to MovieDB api, and then open the connection
                Uri builtUri = Uri.parse(FETCH_MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY, query)
                        .appendQueryParameter(API_KEY, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    Log.d(LOG_TAG, "input stream is null");
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                String s = buffer.toString();
                if(s != null) {
                    //if String is not null, parse the JSON
                    json = new JSONObject(s);
                    parsePopularMovieJson(json);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "error closing stream", e);
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mGridViewAdapter = new GridViewAdapter(getActivity(), movies);
            mGridView.setAdapter(mGridViewAdapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra("posterPath", movies[position].getDeatilFullPoster());
                    intent.putExtra("title", movies[position].getOriginalTitle());
                    intent.putExtra("releaseDate", movies[position].getReleaseDate());
                    intent.putExtra("overview", movies[position].getOverView());
                    intent.putExtra("vote", movies[position].getVoteAverage());
                    startActivity(intent);
                }
            });
        }

        /**
         * Parses Json string and creates Array of Movies
         * @param json
         * @throws JSONException
         */
        public void parsePopularMovieJson(JSONObject json) throws JSONException {
            //results array in main object
            results = json.getJSONArray("results");
            //loop through array and create 20 Movie objects
            for (int i = 0; i < results.length(); i++) {
                JSONObject currentMovie = results.getJSONObject(i);
                Movie movie = new Movie();

                movie.setPosterPath(currentMovie.getString("poster_path"));
                movie.setOverView(currentMovie.getString("overview"));
                movie.setReleaseDate(currentMovie.getString("release_date"));
                movie.setOriginalTitle(currentMovie.getString("original_title"));
                movie.setVoteAverage(currentMovie.getDouble("vote_average"));
                movies[i] = (movie);
            }
        }
    }

}


