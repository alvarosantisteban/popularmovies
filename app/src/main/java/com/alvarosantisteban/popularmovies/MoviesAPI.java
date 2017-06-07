package com.alvarosantisteban.popularmovies;

import com.alvarosantisteban.popularmovies.model.MovieContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * The interface to communicate with the MovieDB using Retrofit.
 */
interface MoviesAPI {

    // FIXME I would ideally use @Header or an interceptor, but it appears not to be recognised by the API

    @GET("movie/popular")
    Call<MovieContainer> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieContainer> getTopRatedMovies(@Query("api_key") String apiKey);
}
