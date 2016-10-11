package com.developer.jc.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.developer.jc.popularmoviesapp.R;

/**
 * Represents the main activity of the application.
 * @author Jesse Cochran
 */
public class MainActivity extends AppCompatActivity {

    private final int HIGHEST_RATING = 1;
    private final int POPULAR = 2;
    private final int FAVORITES = 3;

    public boolean mTwoPane;
    private static final String MAIN_TAG = "";
    private static final String DF_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //If the screen width is large enough then it creates the fragment for the two pane UI
        if(findViewById(R.id.fragment_detail_twopane) != null) {
            mTwoPane = true;
            if(savedInstanceState == null) {
                DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_twopane, detailActivityFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        MainActivityFragment fragment = (MainActivityFragment) fm.findFragmentById(R.id.fragment);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rating){
            fragment.updateMovies(HIGHEST_RATING, mTwoPane);
            return true;
        }
        //Popular movies on spinner
        if(id == R.id.action_popular){
            fragment.updateMovies(POPULAR, mTwoPane);
            return true;
        }
        //Favorites movies on spinner(stored in mySQLite database
        if(id == R.id.action_favorites){
            fragment.updateMovies(FAVORITES, mTwoPane);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }
}
