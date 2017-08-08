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

/**
 * The activity displaying a set of movies, displayed as a grid.
 */
public class MainActivity extends AppCompatActivity implements MoviesFragment.OnListFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    public static final int POS_MOST_POPULAR = 0;
    public static final int POS_TOP_RATED = 1;
    public static final int POS_FAVOURITES = 2;
    protected static final String EXTRA_MOVIE = "ExtraMovie";
    private static final String SPINNER_POS = "spinnerPos";

    // Used to distinguish between real user touches and automatic calls on onItemSelected
    private boolean hasUserTouchedSpinner = false;

    private Spinner spinner;
    private int spinnerPos = POS_MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            spinnerPos = savedInstanceState.getInt(SPINNER_POS, POS_MOST_POPULAR);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_POS, spinner.getSelectedItemPosition());
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
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerPos);
        spinner.setOnItemSelectedListener(this);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hasUserTouchedSpinner = true;
                return false;
            }
        });
        filterBySpinnerPos(spinnerPos);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!hasUserTouchedSpinner) {
            return;
        }

        hasUserTouchedSpinner = false;

        filterBySpinnerPos(position);
    }

    private void filterBySpinnerPos(int position) {
        MoviesFragment fragment = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
        if(fragment != null) {
            switch (position) {
                case POS_FAVOURITES:
                    fragment.askForFavourites();
                    break;
                default:
                    fragment.downloadMoviesSortedBy(position);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    // FIXME (2) Display loading bar

    // FIXME (3) Notify adapter if a favourite was created/removed
}
