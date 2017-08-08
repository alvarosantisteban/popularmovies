package com.alvarosantisteban.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The contract that specifies the layout of how the DB of the app is organised.
 */
public final class MoviesContract {

    public static final String AUTHORITY = "com.alvarosantisteban.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" +AUTHORITY);

    public static final String PATH_MOVIES = "movie";

    private MoviesContract(){}

    public static class Movie implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_TITLE= "title";
        public static final String COLUMN_NAME_MOVIE_ID = "movieId";
        public static final String COLUMN_NAME_POSTER_PATH = "posterPath";
        public static final String COLUMN_NAME_OVERVIEW= "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "voteAverage";
    }
}
