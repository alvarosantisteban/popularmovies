package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a container of movie's reviews.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieReviewContainer extends BaseMovieContainer{

    private final List<MovieReview> reviews;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieReviewContainer(int page, List<MovieReview> reviews, int totalNumResults) {
        super(page, totalNumResults);
        this.reviews = reviews;
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
