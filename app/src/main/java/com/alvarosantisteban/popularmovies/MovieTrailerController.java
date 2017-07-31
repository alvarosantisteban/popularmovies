package com.alvarosantisteban.popularmovies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.model.MovieTrailerContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Creates the Retrofit client, calls the TheMovieDB API to retrieve the trailers of the specified movie
 * and posts the result in the Otto bus.
 */
class MovieTrailerController implements Callback<MovieTrailerContainer> {

    private static final String TAG = MovieTrailerController.class.getSimpleName();

    void start(@NonNull String movieId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesAPI.TMDB_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);
        Call<MovieTrailerContainer> call = moviesAPI.getTrailers(movieId, BuildConfig.TMDB_API_KEY);
        call.enqueue(this);

    }

    @Override
    public void onResponse(@NonNull Call<MovieTrailerContainer> call, @NonNull Response<MovieTrailerContainer> response) {
        if (response.isSuccessful()) {
            MovieTrailerContainer movieContainer = response.body();
            if (movieContainer != null) {
                OttoBus.getInstance().post(new RetrofitResultEvent(movieContainer));
            } else {
                Log.e(TAG, response.errorBody().toString());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<MovieTrailerContainer> call,@NonNull Throwable t) {
        Log.e(TAG, "onFailure: " +t.toString());
    }
}
