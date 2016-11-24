package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movies.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieContainer {

    private final int page;
    private final List<Movie> movies;
    private final int totalNumResults;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieContainer(int page, List<Movie> movies, int totalNumResults) {
        this.page = page;
        this.movies = movies;
        this.totalNumResults = totalNumResults;
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
