package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alvarosantisteban.popularmovies.model.Movie;
import com.alvarosantisteban.popularmovies.model.MovieContainer;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import static com.alvarosantisteban.popularmovies.MainActivity.POS_MOST_POPULAR;

/**
 * A fragment representing a list of movies in two columns.
 */
public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();

    protected static final String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    protected static final String TMDB_IMAGE_QUALITY_PATH = "w342";

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

        downloadMoviesSortedBy(POS_MOST_POPULAR);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(Controller.RetrofitResultEvent event) throws IOException {
        MovieContainer movieContainer = event.getMovieContainer();
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
