package com.example.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // Base URL for the API
    public static final String API_BASE_URL = "https://api.themoviedb.org/3";
    // API key parameter
    public static final String API_KEY_PARAM = "api_key";
    // Logging tag
    public static final String TAG = "MovieListActivity";

    // Client
    AsyncHttpClient client;
    // List of currently playing movies
    ArrayList<Movie> movies;
    // Recycler View
    RecyclerView rvMovies;
    // The adapter wired to the recycler view
    MovieAdapter adapter;
    // Image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Initialize the client
        client = new AsyncHttpClient();
        // Initialize the list of movies
        movies = new ArrayList<>();
        // Initialize the adapter
        adapter = new MovieAdapter(movies);

        // Resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // Get the configuration
        getConfiguration();
    }

    // Get the list of currently playing movies from the API
    private void getNowPlaying() {
        // Create URL
        String url = API_BASE_URL + "/movie/now_playing";
        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // Execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Load the results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // Iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // Notify the adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1  );
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure to get data from now playing endpoint", throwable, true);
            }
        });
    }

    // Retrieve the configuration from API
    public void getConfiguration() {
        // Create URL
        String url = API_BASE_URL + "/configuration";
        // Set request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        // Execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configurations with posterBaseUrl %s and posterSize %s",
                            config.getPosterBaseUrl(),
                            config.getPosterSize()));
                    // Pass config to adapter
                    adapter.setConfig(config);
                    // Get the now playing movies
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }


    // Log, handle and alert user of errors
    private void logError(String message, Throwable error, boolean alertUser) {
        // Log the error
        Log.e(TAG, message, error);
        // Alert the user or error
        if (alertUser) {
            // Display toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
