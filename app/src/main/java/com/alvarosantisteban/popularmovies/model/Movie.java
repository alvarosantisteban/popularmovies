package com.alvarosantisteban.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a movie from The Movie DB.
 */
public class Movie {

    private final String id;
    private final String title;
    // TODO Check API and add needed fields

    public Movie(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public static List<Movie> getFakeData() {
        List<Movie> fakeMovies = new ArrayList<>(11);
        Movie movie1 = new Movie("M-0001", "Los cronocrimenes");
        Movie movie2 = new Movie("M-0002", "El sol del membrillo");
        Movie movie3 = new Movie("M-0001", "Los mejores años de nuestra vida");
        Movie movie4 = new Movie("M-0002", "Volver");
        Movie movie5 = new Movie("M-0001", "El verdugo");
        Movie movie6 = new Movie("M-0001", "Blade Runner");
        Movie movie7 = new Movie("M-0002", "Stalker");
        Movie movie8 = new Movie("M-0001", "Godzilla");
        Movie movie9 = new Movie("M-0002", "El padrino");
        Movie movie10 = new Movie("M-0001", "La insoportable levedad del ser");
        Movie movie11 = new Movie("M-0001", "Doce del patibulo");
        fakeMovies.add(movie1);
        fakeMovies.add(movie2);
        fakeMovies.add(movie3);
        fakeMovies.add(movie4);
        fakeMovies.add(movie5);
        fakeMovies.add(movie6);
        fakeMovies.add(movie7);
        fakeMovies.add(movie8);
        fakeMovies.add(movie9);
        fakeMovies.add(movie10);
        fakeMovies.add(movie11);
        return fakeMovies;
    }
}
