package com.alvarosantisteban.popularmovies.api;

import android.support.annotation.NonNull;

import com.alvarosantisteban.popularmovies.model.BaseMovieContainer;

/**
 * Small helper class to encapsulate the result of the retrofit request.
 */
public class RetrofitResultEvent {

    @NonNull
    private BaseMovieContainer movieContainer;

    public RetrofitResultEvent(@NonNull BaseMovieContainer movieContainer) {
        this.movieContainer = movieContainer;
    }

    @NonNull
    public BaseMovieContainer getMovieContainer() {
        return movieContainer;
    }
}
