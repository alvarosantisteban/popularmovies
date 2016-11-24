package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static final String POPULAR_MOVIES_ENDPOINT = "movie/popular";

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Movie movie);
    }

    private static final int NUM_COLUMNS = 2;
    private OnListFragmentInteractionListener mListener;

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
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, NUM_COLUMNS));
//            recyclerView.setAdapter(new MovieRecyclerViewAdapter(Movie.getFakeData(), mListener));
        }

        OttoBus.getInstance().register(this);

        try {
            URL moviesUrl = getUrl();
            new DownloadMoviesAsyncTask().execute(moviesUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.toString());
        }

        return view;
    }

    private URL getUrl() throws MalformedURLException{
        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendEncodedPath(POPULAR_MOVIES_ENDPOINT)
                .appendQueryParameter(APPID_PARAM, BuildConfig.TMDB_API_KEY)
                .build();
        return new URL(builtUri.toString());
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(DownloadMoviesAsyncTask.AsyncTaskResultEvent event) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MovieContainer movieContainer = mapper.readValue(event.getResult(), MovieContainer.class);
        Log.d(TAG, "-" +movieContainer.getMovies().get(0).getOriginalTitle() +"-");
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
