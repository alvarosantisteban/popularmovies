package com.alvarosantisteban.popularmovies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.model.MovieReviewContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Creates the Retrofit client, calls the TheMovieDB API to retrieve the reviews of the specified movie
 * and posts the result in the Otto bus.
 */
class MovieReviewController implements Callback<MovieReviewContainer> {

    private static final String TAG = MovieReviewController.class.getSimpleName();

    void start(@NonNull String movieId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesAPI.TMDB_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);
        Call<MovieReviewContainer> call = moviesAPI.getReviews(movieId, BuildConfig.TMDB_API_KEY);
        call.enqueue(this);

    }

    @Override
    public void onResponse(@NonNull Call<MovieReviewContainer> call, @NonNull Response<MovieReviewContainer> response) {
        if (response.isSuccessful()) {
            MovieReviewContainer movieContainer = response.body();
            if (movieContainer != null) {
                OttoBus.getInstance().post(new RetrofitResultEvent(movieContainer));
            } else {
                Log.e(TAG, response.errorBody().toString());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<MovieReviewContainer> call,@NonNull Throwable t) {
        Log.e(TAG, "onFailure: " +t.toString());
    }
}
