package com.alvarosantisteban.popularmovies;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Small task to asynchronously download the json from the URL passed by parameter and post the
 * result on the OttoBus.
 */
public class DownloadMoviesAsyncTask extends AsyncTask<URL, Void, String> {

    private static final String TAG = DownloadMoviesAsyncTask.class.getSimpleName();

    @Nullable
    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        try {
            // Create the request to TMDB, and open the connection
            urlConnection = (HttpURLConnection) urls[0].openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            // If the code didn't successfully get the movies data, there's no point in attempting
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return moviesJsonStr;
    }

    @Override
    protected void onPostExecute(String json) {
        OttoBus.getInstance().post(new AsyncTaskResultEvent(json));
    }

    /**
     * Small helper class to encapsulate the result of an async task.
     */
    public class AsyncTaskResultEvent {

        @Nullable private String result;

        public AsyncTaskResultEvent(@Nullable String result) {
            this.result = result;
        }

        @Nullable
        public String getResult() {
            return result;
        }
    }
}
