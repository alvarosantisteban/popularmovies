package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a container of movies.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class BaseMovieContainer {

    private final int page;
    private final int totalNumResults;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public BaseMovieContainer(int page, int totalNumResults) {
        this.page = page;
        this.totalNumResults = totalNumResults;
    }
}
