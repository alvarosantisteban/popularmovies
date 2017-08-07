package com.alvarosantisteban.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alvarosantisteban.popularmovies.api.MoviesAPI;
import com.alvarosantisteban.popularmovies.api.RetrofitResultEvent;
import com.alvarosantisteban.popularmovies.data.MoviesContract;
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

    private static final int ADD_MOVIE = 1;
    private static final int REMOVE_MOVIE = 2;
    private static final int GET_MOVIE_INFO = 3;

    private Movie movie;
    private boolean isFav;

    private LinearLayout trailersSection;
    private LinearLayout reviewsSection;
    private RecyclerView trailersRv;
    private RecyclerView reviewsRv;
    private Button favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        OttoBus.getInstance().register(this);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        ImageView poster = (ImageView) findViewById(R.id.detail_movie_poster);
        TextView title = (TextView) findViewById(R.id.detail_movie_title);
        TextView description = (TextView) findViewById(R.id.detail_movie_description);
        TextView usersRating = (TextView) findViewById(R.id.detail_movie_rating);
        TextView releaseDate = (TextView) findViewById(R.id.detail_movie_release_date);
        favButton = (Button) findViewById(R.id.detail_movie_fav_button);

        trailersSection = (LinearLayout) findViewById(R.id.trailer_section);
        reviewsSection = (LinearLayout) findViewById(R.id.reviews_section);
        trailersRv = (RecyclerView) trailersSection.findViewById(R.id.detail_rv);
        reviewsRv = (RecyclerView) reviewsSection.findViewById(R.id.detail_rv);

        trailersRv.setLayoutManager(new LinearLayoutManager(this, Utils.isTabletOrLandscape(this) ? LinearLayoutManager.HORIZONTAL : LinearLayoutManager.VERTICAL, false));
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
            releaseDate.setText(Utils.extractYear(movie.getReleaseDate()));

            // Start an AsyncTask to check in the DB if the movie is marked as favourite
            new OperateWithDBMovieAsyncTask().execute(GET_MOVIE_INFO);
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

    public void onFavButtonClicked(View view) {
        // Change the message on the button
        isFav = !isFav;
        toggleButtonText(isFav);
        
        // Update DB
        new OperateWithDBMovieAsyncTask().execute(isFav ? ADD_MOVIE : REMOVE_MOVIE);
    }

    public void toggleButtonText(boolean isFav) {
        favButton.setText(isFav ? R.string.detail_unfav_button : R.string.detail_fav_button);
    }

    /**
     * Helper class to do operations with the content provider in an asynchronous thread.
     */
    private class OperateWithDBMovieAsyncTask extends AsyncTask <Integer, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Integer... operationToBeDone) {
            switch (operationToBeDone[0]) {
                case ADD_MOVIE:
                    addMovieToDB();
                    return null;
                case REMOVE_MOVIE:
                    removeMovieFromDB();
                    return null;
                case GET_MOVIE_INFO:
                    Uri uriToQuery = MoviesContract.Movie.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
                    return getContentResolver().query(uriToQuery, null, null, null, null, null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            // Modify the favourite state only if that was the wanted operation
            if(cursor != null) {
                isFav = cursor.getCount() > 0;
                toggleButtonText(isFav);
            }
        }

        private void addMovieToDB() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.Movie.COLUMN_NAME_MOVIE_ID, movie.getId());
            contentValues.put(MoviesContract.Movie.COLUMN_NAME_TITLE, movie.getOriginalTitle());

            getContentResolver().insert(MoviesContract.Movie.CONTENT_URI, contentValues);
        }

        private void removeMovieFromDB() {
            Uri uriToDelete = MoviesContract.Movie.CONTENT_URI.buildUpon().appendPath(String.valueOf(movie.getId())).build();
            getContentResolver().delete(uriToDelete, null, null);
        }
    }
}
