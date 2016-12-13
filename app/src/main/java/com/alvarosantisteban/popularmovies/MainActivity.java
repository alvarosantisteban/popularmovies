package com.alvarosantisteban.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alvarosantisteban.popularmovies.model.Movie;

import static com.alvarosantisteban.popularmovies.MoviesFragment.POPULAR_MOVIES_ENDPOINT;
import static com.alvarosantisteban.popularmovies.MoviesFragment.TOP_RATED_ENDPOINT;

/**
 * The activity displaying a set of movies, displayed as a grid.
 */
public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    private static final int POS_MOST_POPULAR = 0;
    private static final int POS_TOP_RATED = 1;
    protected static final String EXTRA_MOVIE = "ExtraMovie";

    // Used to distinguish between real user touches and automatic calls on onItemSelected
    private boolean hasUserTouchedSpinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(Movie item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, item);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hasUserTouchedSpinner = true;
                return false;
            }
        });
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!hasUserTouchedSpinner) {
            return;
        }

        hasUserTouchedSpinner = false;
        String endPoint = POPULAR_MOVIES_ENDPOINT;
        switch (position) {
            case POS_MOST_POPULAR:
                endPoint = POPULAR_MOVIES_ENDPOINT;
                break;
            case POS_TOP_RATED:
                endPoint = TOP_RATED_ENDPOINT;
                break;
        }

        MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
        if(fragment != null) {
            fragment.downloadMoviesSortedBy(endPoint);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
