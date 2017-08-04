package com.alvarosantisteban.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Subclass of the Application to allow having Stetho.
 */
public class MovieApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
