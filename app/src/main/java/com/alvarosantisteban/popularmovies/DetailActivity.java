package com.alvarosantisteban.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.model.BaseMovieContainer;
import com.alvarosantisteban.popularmovies.model.Movie;
import com.alvarosantisteban.popularmovies.model.MovieReviewContainer;
import com.alvarosantisteban.popularmovies.model.MovieTrailerContainer;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import static com.alvarosantisteban.popularmovies.MainActivity.EXTRA_MOVIE;

/**
 * Displays the basic information of a film.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        OttoBus.getInstance().register(this);

        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        ImageView poster = (ImageView) findViewById(R.id.detail_movie_poster);
        TextView title = (TextView) findViewById(R.id.detail_movie_title);
        TextView description = (TextView) findViewById(R.id.detail_movie_description);
        TextView usersRating = (TextView) findViewById(R.id.detail_movie_rating);
        TextView releaseDate = (TextView) findViewById(R.id.detail_movie_release_date);

        if (movie != null) {
            // Set the title of the activity
            setTitle(movie.getOriginalTitle());

            requestExtraInfoForMovie(movie.getId());

            Glide.with(this).load(MoviesAPI.TMDB_IMAGE_BASE_URL + MoviesAPI.TMDB_IMAGE_QUALITY_PATH +movie.getPosterPath())
                    .into(poster);
            title.setText(movie.getOriginalTitle());
            description.setText(movie.getOverview());
            usersRating.setText(getString(R.string.film_vote, movie.getVoteAverage()));
            releaseDate.setText(getString(R.string.film_release_date, movie.getReleaseDate()));
        }
    }

    private void requestExtraInfoForMovie(long movieId) {
        requestTrailersForMovie(movieId);
        requestReviewsForMovie(movieId);
    }

    private void requestTrailersForMovie(long movieId) {
        MovieTrailerController controller = new MovieTrailerController();
        controller.start(String.valueOf(movieId));
    }

    private void requestReviewsForMovie(long movieId) {
        MovieReviewController controller = new MovieReviewController();
        controller.start(String.valueOf(movieId));
    }

    @SuppressWarnings("unused") // Used to receive results from Otto bus
    @Subscribe
    public void onAsyncTaskResult(RetrofitResultEvent event) throws IOException {
        BaseMovieContainer container = event.getMovieContainer();
        if(container instanceof MovieReviewContainer) {
            MovieReviewContainer movieReviewContainer = (MovieReviewContainer) container;

            if (movieReviewContainer.getMovieReviews().size() > 0) {
                String review = movieReviewContainer.getMovieReviews().get(0).getContent();
                Toast.makeText(this, review, Toast.LENGTH_LONG).show();
            }
        } else if (container instanceof MovieTrailerContainer) {
            MovieTrailerContainer movieTrailerContainer = (MovieTrailerContainer) container;

            if (movieTrailerContainer.getMovieTrailers().size() > 0) {
                String review = movieTrailerContainer.getMovieTrailers().get(0).getType();
                Toast.makeText(this, review, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.getInstance().unregister(this);
    }
}
