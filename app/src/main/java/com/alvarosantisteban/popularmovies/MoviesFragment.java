package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alvarosantisteban.popularmovies.model.Movie;
import com.alvarosantisteban.popularmovies.model.MovieContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A fragment representing a list of movies in two columns.
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();
    
    private static final String APPID_PARAM = "api_key";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    protected static final String POPULAR_MOVIES_ENDPOINT = "movie/popular";
    protected static final String TOP_RATED_ENDPOINT = "movie/top_rated";

    protected static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    protected static final String TMDB_IMAGE_QUALITY_PATH = "w342";

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }

    private static final int NUM_COLUMNS = 2;
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
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, NUM_COLUMNS));
        }

        OttoBus.getInstance().register(this);

        downloadMoviesSortedBy(POPULAR_MOVIES_ENDPOINT);

        return view;
    }

    protected void downloadMoviesSortedBy(String endPoint) {
        if (isNetworkAvailable()) {
            try {
                URL moviesUrl = getUrl(endPoint);
                new DownloadMoviesAsyncTask().execute(moviesUrl);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.toString());
            }
        }else {
            Toast.makeText(getContext(), getString(R.string.error_no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    private URL getUrl(String endPoint) throws MalformedURLException{
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendEncodedPath(endPoint)
                .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return new URL(builtUri.toString());
    }
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(DownloadMoviesAsyncTask.AsyncTaskResultEvent event) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MovieContainer movieContainer = mapper.readValue(event.getResult(), MovieContainer.class);

        // Set the movies in the adapter
        mRecyclerView.setAdapter(new MovieRecyclerViewAdapter(movieContainer.getMovies(), mListener, getActivity()));
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
}
