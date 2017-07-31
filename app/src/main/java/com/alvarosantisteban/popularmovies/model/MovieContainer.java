package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movies.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieContainer extends BaseMovieContainer{

    private final List<Movie> movies;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieContainer(int page, List<Movie> movies, int totalNumResults) {
        super(page, totalNumResults);
        this.movies = movies;
    }

    @JsonCreator
    public static MovieContainer from(
            @JsonProperty("page") int page,
            @JsonProperty("results") List<Movie> results,
            @JsonProperty("total_results") int totalNumResults) {
        return new MovieContainer(page, results, totalNumResults);
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
