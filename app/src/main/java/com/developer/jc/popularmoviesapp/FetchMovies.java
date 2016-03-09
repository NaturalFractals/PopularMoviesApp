package com.developer.jc.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FetchMovies extends AsyncTask<String, Void, Void>{
    BufferedReader reader;
    HttpURLConnection urlConnection;
    JSONArray results;
    JSONObject json;
    // List of movie objects
    Movie[] movies = new Movie[20];
    Context mContext;
    // Grid View
    GridView mGridView;
    // GridViewAdapter
    GridViewAdapter mGridViewAdapter;


    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public FetchMovies(Context context, GridView gridview){
        mContext = context;
        mGridView = gridview;
    }

    @Override
    protected Void doInBackground(String... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String query = "";
        if(params[0] == mContext.getResources().getString(R.string.action_popular)) {
            query = sharedPreferences.getString(mContext.getString(R.string.pref_key), mContext.getString(R.string.pref_popular_value));
        } else if (params[0] == mContext.getResources().getString(R.string.action_highest_rating)){
            query = sharedPreferences.getString(mContext.getString(R.string.pref_key), mContext.getString(R.string.pref_rating_value));
        }

        //Api key for moviedb request
        String apiKey = "API KEY HERE";
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
        mGridViewAdapter = new GridViewAdapter(mContext, movies);
        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", movies[position].getMovieId());
                intent.putExtra("posterPath", movies[position].getDeatilFullPoster());
                intent.putExtra("title", movies[position].getOriginalTitle());
                intent.putExtra("releaseDate", movies[position].getReleaseDate());
                intent.putExtra("overview", movies[position].getOverView());
                intent.putExtra("vote", movies[position].getVoteAverage());
                FetchTrailers fetchTrailers = new FetchTrailers(mContext);
                fetchTrailers.execute(movies[position]);
                FetchReviews fetchReviews = new FetchReviews(mContext);
                fetchReviews.execute(movies[position]);
                mContext.startActivity(intent);
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

            movie.setMovieId(currentMovie.getInt("id"));
            movie.setPosterPath(currentMovie.getString("poster_path"));
            movie.setOverView(currentMovie.getString("overview"));
            movie.setReleaseDate(currentMovie.getString("release_date"));
            movie.setOriginalTitle(currentMovie.getString("original_title"));
            movie.setVoteAverage(currentMovie.getDouble("vote_average"));
            movies[i] = (movie);
        }
    }

}