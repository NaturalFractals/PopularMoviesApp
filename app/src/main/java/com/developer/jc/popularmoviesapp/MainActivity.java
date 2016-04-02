package com.developer.jc.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        if(id == R.id.action_popular){
            fragment.updateMovies(POPULAR, mTwoPane);
            return true;
        }

        if(id == R.id.action_favorites){
            fragment.updateMovies(FAVORITES, mTwoPane);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean isTwoPane(){
        return mTwoPane;
    }

}
