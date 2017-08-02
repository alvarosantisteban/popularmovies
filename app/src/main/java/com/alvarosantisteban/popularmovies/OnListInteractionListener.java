package com.alvarosantisteban.popularmovies;

import com.alvarosantisteban.popularmovies.model.MovieReview;
import com.alvarosantisteban.popularmovies.model.MovieTrailer;

/**
 * Interface to pass events from adapters to activity.
 */
public interface OnListInteractionListener {

    void onItemClicked(MovieReview movieReview);
    void onItemClicked(MovieTrailer movieTrailer);
}
