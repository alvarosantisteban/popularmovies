package com.alvarosantisteban.popularmovies.api;

import com.alvarosantisteban.popularmovies.model.MovieContainer;
import com.alvarosantisteban.popularmovies.model.MovieReviewContainer;
import com.alvarosantisteban.popularmovies.model.MovieTrailerContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The interface to communicate with the MovieDB using Retrofit.
 */
public interface MoviesAPI {

    String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    String TMDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    String TMDB_IMAGE_QUALITY_PATH = "w342";

    // FIXME I would ideally use @Header or an interceptor, but it appears not to be recognised by the API

    @GET("movie/popular")
    Call<MovieContainer> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieContainer> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerContainer> getTrailers(@Path("id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewContainer> getReviews(@Path("id") String movieId, @Query("api_key") String apiKey);
}
