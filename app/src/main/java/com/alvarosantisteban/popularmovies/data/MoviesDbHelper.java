package com.alvarosantisteban.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class to manage database creation and version management of movies.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "movies.db";
    private final static int DATABASE_VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREAT_MOVIES_TABLE = "CREATE TABLE " +
                MoviesContract.Movie.TABLE_NAME + " (" +
                MoviesContract.Movie._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.Movie.COLUMN_NAME_MOVIE_ID + " INTEGER NOT NULL," +
                MoviesContract.Movie.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
                MoviesContract.Movie.COLUMN_NAME_POSTER_PATH + " TEXT," +
                MoviesContract.Movie.COLUMN_NAME_OVERVIEW + " TEXT," +
                MoviesContract.Movie.COLUMN_NAME_RELEASE_DATE + " TEXT," +
                MoviesContract.Movie.COLUMN_NAME_VOTE_AVERAGE + " REAL" +
                ");";
        db.execSQL(SQL_CREAT_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Following code is commented because no changes are needed to the table so far, but if
        // they are needed in the future, they should follow the pattern proposed in the following
        // link: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
//        if (oldVersion < 2) {
//            db.execSQL(DATABASE_ALTER_MOVIE_1);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_MOVIE_2);
//        }
    }
}
