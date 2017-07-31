package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movie's trailers.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieTrailerContainer extends BaseMovieContainer {

    private final List<MovieTrailer> movieTrailers;

    @JsonIgnoreProperties(ignoreUnknown=true)
    MovieTrailerContainer(int page, List<MovieTrailer> movieTrailers, int totalNumResults) {
        super(page, totalNumResults);
        this.movieTrailers = movieTrailers;
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
