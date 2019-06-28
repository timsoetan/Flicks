package com.example.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    // Base URL for loading posters
    String posterBaseUrl;
    // Poster size for fetched images
    String posterSize;
    // The backdrop size to use when fetching images
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // Get the base URL of the poster
        posterBaseUrl = images.getString("secure_base_url");
        // Get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");
        // Parse the backdrop sizes and use the option at index 1 or w780 as a fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    // Helper method for creating URLs
    public String getPosterImageUrl(String size, String path) {
        return String.format("%s%s%s", posterBaseUrl, size, path);
    }

    public String getPosterBaseUrl() {
        return posterBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
