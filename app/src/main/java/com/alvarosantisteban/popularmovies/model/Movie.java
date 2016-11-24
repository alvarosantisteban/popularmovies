package com.alvarosantisteban.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a movie from The Movie DB.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Movie {

    private final long id;
    private final String originalTitle;
    private final String originalLanguage;
    private final String posterPath;
    private final boolean adult;
    private final String overview;
    private final String releaseDate;
    private final double popularity;
    private final int voteCount;
    private final boolean video;
    private final double voteAverage;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public Movie(long id, String title, String originalLanguage, String posterPath, boolean adult, String overview, String releaseDate, double popularity, int voteCount, boolean video, double voteAverage) {
        this.id = id;
        this.originalTitle = title;
        this.originalLanguage = originalLanguage;
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;

    }

    @JsonCreator
    public static Movie from(
            @JsonProperty("id") long id,
            @JsonProperty("original_title") String originalTitle,
            @JsonProperty("original_language") String originalLanguage,
            @JsonProperty("poster_path") String posterPath,
            @JsonProperty("adult") boolean adult,
            @JsonProperty("overview") String overview,
            @JsonProperty("release_date") String releaseDate,
            @JsonProperty("popularity") double popularity,
            @JsonProperty("vote_count") int voteCount,
            @JsonProperty("video") boolean video,
            @JsonProperty("vote_average") double voteAverage) {
        return new Movie(id, originalTitle, originalLanguage, posterPath, adult, overview, releaseDate, popularity, voteCount, video, voteAverage);
    }

    public long getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }
}
