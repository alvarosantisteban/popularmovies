package com.alvarosantisteban.popularmovies.data;

import android.provider.BaseColumns;

/**
 * The contract that specifies the layout of how the DB of the app is organised.
 */
public final class MoviesContract {

    private MoviesContract(){}

    public static class Movie implements BaseColumns {

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_TITLE= "title";
        public static final String COLUMN_NAME_MOVIE_ID = "movieId";
    }
}
