package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movie's trailers.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieTrailerContainer {

    private final int page;
    private final List<MovieTrailer> movieTrailers;
    private final int totalNumResults;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieTrailerContainer(int page, List<MovieTrailer> movieTrailers, int totalNumResults) {
        this.page = page;
        this.movieTrailers = movieTrailers;
        this.totalNumResults = totalNumResults;
    }

    @JsonCreator
    public static MovieTrailerContainer from(
            @JsonProperty("page") int page,
            @JsonProperty("results") List<MovieTrailer> movieTrailers,
            @JsonProperty("total_results") int totalNumResults) {
        return new MovieTrailerContainer(page, movieTrailers, totalNumResults);
    }

    public List<MovieTrailer> getMovieTrailers() {
        return movieTrailers;
    }
}
