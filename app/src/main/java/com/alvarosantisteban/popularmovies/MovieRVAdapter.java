package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.model.Movie;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Movie} and makes a call to the
 * specified {@link MoviesFragment.OnListFragmentInteractionListener}.
 */
class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.ViewHolder> {

    private final List<Movie> mMovieList;
    private final MoviesFragment.OnListFragmentInteractionListener mListener;
    private final Context mContext;

    MovieRVAdapter(List<Movie> items, MoviesFragment.OnListFragmentInteractionListener listener, Context context) {
        mMovieList = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mMovieList == null) {
            return;
        }
        final Movie movie = mMovieList.get(position);
        holder.setPosterImage(MoviesAPI.TMDB_IMAGE_BASE_URL + MoviesAPI.TMDB_IMAGE_QUALITY_PATH +movie.getPosterPath());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mPoster;
        final Context mContext;

        ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            mPoster = (ImageView) view.findViewById(R.id.movie_poster);
            mContext = context;
        }

        void setPosterImage(String imageUrl) {
            Glide.with(mContext).load(imageUrl)
                    .into(mPoster);
        }
    }
}
