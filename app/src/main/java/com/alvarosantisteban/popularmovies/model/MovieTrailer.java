package com.alvarosantisteban.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a movie trailer from The Movie DB.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MovieTrailer implements Parcelable{

    private final String id;
    private final String language;
    private final String key;
    private final String name;
    private final String site;
    private final String type;

    @JsonIgnoreProperties(ignoreUnknown=true)
    public MovieTrailer(String id, String language, String key, String name, String site, String type) {
        this.id = id;
        this.language = language;
        this.key = key;
        this.name = name;
        this.site = site;
        this.type = type;

    }

    @JsonCreator
    public static MovieTrailer from(
            @JsonProperty("id") String id,
            @JsonProperty("language") String language,
            @JsonProperty("key") String key,
            @JsonProperty("name") String name,
            @JsonProperty("site") String site,
            @JsonProperty("type") String type) {
        return new MovieTrailer(id, language, key, name, site, type);
    }

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    protected MovieTrailer(Parcel in) {
        id = in.readString();
        language = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        type = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(language);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
    }
}
