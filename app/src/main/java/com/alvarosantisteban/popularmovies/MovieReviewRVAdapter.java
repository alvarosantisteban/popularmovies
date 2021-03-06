package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarosantisteban.popularmovies.model.MovieReview;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MovieReview} and makes a call to the
 * specified {@link OnListInteractionListener}.
 */
class MovieReviewRVAdapter extends RecyclerView.Adapter<MovieReviewRVAdapter.ViewHolder> {

    private final List<MovieReview> mMovieList;
    private final OnListInteractionListener mListener;
    private final Context mContext;

    MovieReviewRVAdapter(List<MovieReview> items, OnListInteractionListener listener, Context context) {
        mMovieList = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review_item, parent, false);
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mMovieList == null) {
            return;
        }
        final MovieReview movieReview = mMovieList.get(position);
        holder.setTexts(movieReview);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemClicked(movieReview);
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
        final TextView author;
        final TextView content;
        final Context mContext;

        ViewHolder(View view, Context context) {
            super(view);
            mView = view;
            author = (TextView) view.findViewById(R.id.review_author);
            content = (TextView) view.findViewById(R.id.review_content);
            mContext = context;
        }

        public void setTexts(MovieReview movieReview) {
            author.setText(movieReview.getAuthor());
            content.setText(movieReview.getContent());
        }
    }
}
