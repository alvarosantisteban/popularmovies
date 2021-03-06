package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.data.MoviesContract;
import com.alvarosantisteban.popularmovies.model.Movie;
import com.alvarosantisteban.popularmovies.model.MovieContainer;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of movies in two columns.
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    private static final String MOVIE_LIST = "movieList";
    private static final String LIST_POS = "listPosition";

    interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private boolean isDisplayingFavourites;
    private List<Movie> movieList;
    private int currentPosition;
    private boolean hasBeenScrolledAutomatically;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.num_columns)));

        OttoBus.getInstance().register(this);

        scrollToLastVisitedPosition();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve list's state
        if(savedInstanceState != null) {
            hideProgressBar();

            movieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            currentPosition = savedInstanceState.getInt(LIST_POS);

            mRecyclerView.setAdapter(new MovieRVAdapter(movieList, mListener, getActivity()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save list's state
        outState.putParcelableArrayList(MOVIE_LIST, (ArrayList<? extends Parcelable>) movieList);
        currentPosition = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(LIST_POS, currentPosition);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        OttoBus.getInstance().unregister(this);
    }

    protected void downloadMoviesSortedBy(int endPoint) {
        isDisplayingFavourites = false;
        if (isNetworkAvailable()) {
            showProgressBar();

            Controller controller = new Controller();
            controller.start(endPoint);
        }else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    protected void askForFavourites() {
        isDisplayingFavourites = true;
        showProgressBar();

        new GetAllFavouriteMoviesAsyncTask().execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void scrollToLastVisitedPosition() {
        if(!hasBeenScrolledAutomatically) {
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    mRecyclerView.scrollToPosition(currentPosition);
                    hasBeenScrolledAutomatically = true;
                }
            };
            handler.postDelayed(r, 200);
        }
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(RetrofitResultEvent event) throws IOException {
        if(event.getMovieContainer() instanceof MovieContainer) {
            hideProgressBar();

            movieList = ((MovieContainer) event.getMovieContainer()).getMovies();
            // Set the movies in the adapter
            mRecyclerView.setAdapter(new MovieRVAdapter(movieList, mListener, getActivity()));
        }
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onFavouriteDeleted(DetailActivity.FavouriteDeletedEvent event) throws IOException {
        // If the filter is Favourites, then we need to refresh the results because a favourite got deleted
        if (isDisplayingFavourites) {
            askForFavourites();
        }
    }

    /**
     * Helper class to get the favourite movies from the DB in an asynchronous thread.
     */
    private class GetAllFavouriteMoviesAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... nothing) {
            return getContext().getContentResolver().query(MoviesContract.Movie.CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            displayFavourites(cursor);
        }

        private void displayFavourites(Cursor cursor) {
            if(cursor == null) {
                return;
            }

            List<Movie> moviesInDB = new ArrayList<>(cursor.getCount());
            try {
                while (cursor.moveToNext()) {
                    long movieId = cursor.getLong(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_MOVIE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_TITLE));
                    String posterPath = cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_POSTER_PATH));
                    String overview = cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_OVERVIEW));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_RELEASE_DATE));
                    double voteAverage = cursor.getDouble(cursor.getColumnIndex(MoviesContract.Movie.COLUMN_NAME_VOTE_AVERAGE));
                    moviesInDB.add(new Movie(movieId, title, posterPath, overview, releaseDate, voteAverage));
                }
            }finally {
                cursor.close();
            }
            hideProgressBar();

            // Set the favourite movies in the adapter
            movieList = moviesInDB;
            mRecyclerView.setAdapter(new MovieRVAdapter(movieList, mListener, getActivity()));
        }
    }
}
