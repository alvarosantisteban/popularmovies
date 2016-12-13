package com.alvarosantisteban.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvarosantisteban.popularmovies.model.Movie;
import com.bumptech.glide.Glide;

import static com.alvarosantisteban.popularmovies.MainActivity.EXTRA_MOVIE;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        ImageView poster = (ImageView) findViewById(R.id.detail_movie_poster);
        TextView title = (TextView) findViewById(R.id.detail_movie_title);
        TextView description = (TextView) findViewById(R.id.detail_movie_description);
        TextView usersRating = (TextView) findViewById(R.id.detail_movie_rating);
        TextView releaseDate = (TextView) findViewById(R.id.detail_movie_release_date);

        if (movie != null) {
            Glide.with(this).load(MoviesFragment.TMDB_IMAGE_BASE_URL + MoviesFragment.TMDB_IMAGE_QUALITY_PATH +movie.getPosterPath())
                    .into(poster);
            title.setText(movie.getOriginalTitle());
            description.setText(movie.getOverview());
            usersRating.setText("Average vote: " +movie.getVoteAverage());
            releaseDate.setText(movie.getReleaseDate());
        }
    }
}
