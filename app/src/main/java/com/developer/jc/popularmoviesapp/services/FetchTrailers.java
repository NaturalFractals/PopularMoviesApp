package com.developer.jc.popularmoviesapp.services;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.developer.jc.popularmoviesapp.Movie;
import com.developer.jc.popularmoviesapp.Trailer;
import com.developer.jc.popularmoviesapp.activities.MainActivityFragment;
import com.developer.jc.popularmoviesapp.adapters.TrailerAdapter;

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

/**
 * Fetches information for a Movie's trailers
 * @author Jesse Cochran
 */
public class FetchTrailers extends AsyncTask<Movie, Void, String[]> {
    BufferedReader reader;
    HttpURLConnection urlConnection;
    JSONArray results;
    JSONObject json;
    Context mContext;
    String[] trailers = new String[20];
    Movie movie;
    private ListView movieTrailerView;
    private String[] mTrailers;
    private TrailerAdapter mTrailerAdapter;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public FetchTrailers(Context context) {
        mContext = context;
    }

    @Override
    protected void onPostExecute(String[] trailers) {
        super.onPostExecute(trailers);
        movie.setTrailers(trailers);
    }

    @Override
    protected String[] doInBackground(Movie... params) {
        //Api key for moviedb request
        String apiKey = "YOUR_API_KEY";
        try {
            movie = params[0];
            final String MOVIE_ID = params[0].getMovieId() + "";
            final String API_KEY = "api_key";
            final String FETCH_MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + MOVIE_ID.trim()
                    + "/videos";

            //create the request to MovieDB api, and then open the connection
            Uri builtUri = Uri.parse(FETCH_MOVIE_BASE_URL).buildUpon()
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
            if (s != null) {
                //if String is not null, parse the JSON
                json = new JSONObject(s);
                parseTrailers(json);
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
        return trailers;
    }

    public void parseTrailers(JSONObject json) throws JSONException{
        //results array in main object
        results = json.getJSONArray("results");
        //loop through array and create 20 Movie objects
        for (int i = 0; i < results.length(); i++) {
            JSONObject currentTrailer = results.getJSONObject(i);
            Trailer trailer = new Trailer();

            trailer.setKey(currentTrailer.getString("key"));
            if(trailer.getKey() == null){
                return;
            }
            trailers[0 + i] = trailer.getKey();
        }
    }

}
