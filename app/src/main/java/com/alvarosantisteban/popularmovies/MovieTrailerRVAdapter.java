package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarosantisteban.popularmovies.model.MovieTrailer;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieTrailer} and makes a call to the
 * specified {@link OnListInteractionListener}.
 */
class MovieTrailerRVAdapter extends RecyclerView.Adapter<MovieTrailerRVAdapter.ViewHolder> {

    private final List<MovieTrailer> mMovieList;
    private final OnListInteractionListener mListener;
    private final Context mContext;

    MovieTrailerRVAdapter(List<MovieTrailer> items, OnListInteractionListener listener, Context context) {
        mMovieList = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_trailer_item, parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mMovieList == null) {
            return;
        }
        final MovieTrailer movieTrailer = mMovieList.get(position);
        holder.setTexts(movieTrailer);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface that an item has been selected.
                    mListener.onItemClicked(movieTrailer);
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
        final TextView title;
        final TextView site;
        final Context mContext;

        ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.trailer_title);
            site = (TextView) view.findViewById(R.id.trailer_site);
            mContext = context;
        }

        void setTexts(MovieTrailer movieTrailer) {
            title.setText(movieTrailer.getName());
            site.setText(movieTrailer.getSite());
        }
    }
}
