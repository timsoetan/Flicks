package com.example.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

    // Values from API
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private Double voteAverage;

    public Movie() {

    }

    // Initialize JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
    }

    // Get the title
    public String getTitle() {
        return title;
    }

    // Get the overview
    public String getOverview() {
        return overview;
    }

    // Get poster path
    public String getPosterPath() {
        return posterPath;
    }

    // Get backdrop path
    public String getBackdropPath() {
        return backdropPath;
    }

    // Get vote average
    public Double getVoteAverage() {
        return voteAverage;
    }
}
