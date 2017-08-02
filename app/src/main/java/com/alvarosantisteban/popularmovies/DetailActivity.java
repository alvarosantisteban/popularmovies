package com.alvarosantisteban.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.model.BaseMovieContainer;
import com.alvarosantisteban.popularmovies.model.Movie;
import com.alvarosantisteban.popularmovies.model.MovieReview;
import com.alvarosantisteban.popularmovies.model.MovieReviewContainer;
import com.alvarosantisteban.popularmovies.model.MovieTrailer;
import com.alvarosantisteban.popularmovies.model.MovieTrailerContainer;
import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import java.io.IOException;

import static com.alvarosantisteban.popularmovies.MainActivity.EXTRA_MOVIE;

/**
 * Displays the basic information of a film.
 */
public class DetailActivity extends AppCompatActivity implements OnListInteractionListener {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private LinearLayout trailersSection;
    private LinearLayout reviewsSection;
    private RecyclerView trailersRv;
    private RecyclerView reviewsRv;

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

        trailersSection = (LinearLayout) findViewById(R.id.trailer_section);
        reviewsSection = (LinearLayout) findViewById(R.id.reviews_section);
        trailersRv = (RecyclerView) trailersSection.findViewById(R.id.detail_rv);
        reviewsRv = (RecyclerView) reviewsSection.findViewById(R.id.detail_rv);

        trailersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        TextView reviewsLabel = (TextView) reviewsSection.findViewById(R.id.detail_label);
        reviewsLabel.setText(R.string.detail_movie_reviews_label);

        if (movie != null) {
            // Set the title of the activity
            setTitle(movie.getOriginalTitle());

            requestExtraInfoForMovie(movie.getId());

            Glide.with(this).load(MoviesAPI.TMDB_IMAGE_BASE_URL + MoviesAPI.TMDB_IMAGE_QUALITY_PATH +movie.getPosterPath())
                    .into(poster);
            title.setText(movie.getOriginalTitle());
            description.setText(movie.getOverview());
            usersRating.setText(getString(R.string.film_vote, movie.getVoteAverage()));
            releaseDate.setText(extractYear(movie.getReleaseDate()));
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
                // Set the reviews in the recyclerview
                reviewsRv.setAdapter(new MovieReviewRVAdapter(movieReviewContainer.getMovieReviews(), this, this));
            } else {
                // Hide the whole section
                reviewsSection.setVisibility(View.GONE);
            }
        } else if (container instanceof MovieTrailerContainer) {
            MovieTrailerContainer movieTrailerContainer = (MovieTrailerContainer) container;

            if (movieTrailerContainer.getMovieTrailers().size() > 0) {
                // Set the trailers in the recyclerview
                trailersRv.setAdapter(new MovieTrailerRVAdapter(movieTrailerContainer.getMovieTrailers(), this, this));
            } else {
                // Hide the whole section
                trailersSection.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.getInstance().unregister(this);
    }

    @Override
    public void onItemClicked(MovieReview movieReview) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieReview.getUrl())));
    }

    @Override
    public void onItemClicked(MovieTrailer movieTrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +movieTrailer.getKey())));
    }

    @NonNull
    private static String extractYear(@NonNull String date) {
        if(date.contains("-")) {
            return date.substring(0, date.indexOf("-"));
        }
        return date;
    }
}
