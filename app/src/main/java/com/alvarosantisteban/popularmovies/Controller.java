package com.alvarosantisteban.popularmovies;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.model.MovieContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.alvarosantisteban.popularmovies.MainActivity.POS_TOP_RATED;

/**
 * Creates the Retrofit client, calls the TheMovieDB API for the specific endpoint and posts the
 * result in the Otto bus.
 */
class Controller implements Callback<MovieContainer> {

    private static final String TAG = Controller.class.getSimpleName();

    void start(int endPoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesAPI.TMDB_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(new ObjectMapper()))
                .build();

        MoviesAPI moviesAPI = retrofit.create(MoviesAPI.class);

        Call<MovieContainer> call;
        switch (endPoint) {
            case POS_TOP_RATED:
                call = moviesAPI.getMoviesSortedBy(MoviesAPI.SORTED_BY_TOP_RATED, BuildConfig.TMDB_API_KEY);
                break;
            default:
                call = moviesAPI.getMoviesSortedBy(MoviesAPI.SORTED_BY_POPULAR, BuildConfig.TMDB_API_KEY);
        }
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<MovieContainer> call, @NonNull Response<MovieContainer> response) {
        if (response.isSuccessful()) {
            MovieContainer movieContainer = response.body();
            if (movieContainer != null) {
                OttoBus.getInstance().post(new RetrofitResultEvent(movieContainer));
            } else {
                Log.e(TAG, response.errorBody().toString());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<MovieContainer> call,@NonNull Throwable t) {
        Log.e(TAG, "onFailure: " +t.toString());
    }
}
