package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }

    private OnListFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, getResources().getInteger(R.integer.num_columns)));
        }

        OttoBus.getInstance().register(this);

        return view;
    }

    protected void downloadMoviesSortedBy(int endPoint) {
        if (isNetworkAvailable()) {
            Controller controller = new Controller();
            controller.start(endPoint);
        }else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    protected void askForFavourites() {
        new GetAllFavouriteMoviesAsyncTask().execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(RetrofitResultEvent event) throws IOException {
        if(event.getMovieContainer() instanceof MovieContainer) {
            MovieContainer movieContainer = (MovieContainer) event.getMovieContainer();
            // Set the movies in the adapter
            mRecyclerView.setAdapter(new MovieRVAdapter(movieContainer.getMovies(), mListener, getActivity()));
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
        OttoBus.getInstance().unregister(this);
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
            mRecyclerView.setAdapter(new MovieRVAdapter(moviesInDB, mListener, getActivity()));
        }
    }
}
