package com.alvarosantisteban.popularmovies;

import com.squareup.otto.Bus;

/**
 * Small helper class to provide the Otto bus as singleton.
 */
public class OttoBus {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}
