package com.alvarosantisteban.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alvarosantisteban.popularmovies.model.Movie;

/**
 * The activity displaying a set of movies, displayed as a grid.
 */
public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(Movie item) {
        // TODO Open the detail site of the movie
    }
}
