package com.alvarosantisteban.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Helper class with static methods.
 */
public class Utils {

    public static boolean isTabletOrLandscape(@NonNull Context context) {
        return context.getResources().getBoolean(R.bool.isTablet) || context.getResources().getBoolean(R.bool.isLandscape);
    }

    @NonNull
    public static String extractYear(@NonNull String date) {
        if(date.contains("-")) {
            return date.substring(0, date.indexOf("-"));
        }
        return date;
    }
}
