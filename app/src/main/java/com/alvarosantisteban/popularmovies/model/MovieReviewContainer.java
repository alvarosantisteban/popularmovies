package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movie's reviews.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieReviewContainer {

    private final int page;
    private final List<MovieReview> reviews;
    private final int totalNumResults;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieReviewContainer(int page, List<MovieReview> reviews, int totalNumResults) {
        this.page = page;
        this.reviews = reviews;
        this.totalNumResults = totalNumResults;
    }

    @JsonCreator
    public static MovieReviewContainer from(
            @JsonProperty("page") int page,
            @JsonProperty("results") List<MovieReview> reviews,
            @JsonProperty("total_results") int totalNumResults) {
        return new MovieReviewContainer(page, reviews, totalNumResults);
    }

    public List<MovieReview> getMovieReviews() {
        return reviews;
    }
}
