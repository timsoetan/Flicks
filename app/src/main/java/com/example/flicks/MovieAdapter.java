package com.example.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flicks.models.Config;
import com.example.flicks.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // List of movies
    ArrayList<Movie> movies;
    // Config needed for image URLs
    Config config;
    // Context for rendering
    Context context;

    // Initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the context from the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // Return a n ew ViewHolder
        return new ViewHolder(movieView);
    }

    // Binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the movie data at the specified position
        Movie movie = movies.get(position);
        // Populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // Determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // Build URL for poster image
        String imageUrl = null;

        // Load the poster image if in portrait mode
        if (isPortrait) {
            imageUrl = config.getPosterImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            imageUrl = config.getPosterImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // Get the correct placeholder and imageview for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;


        // Load image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 15, 0))
                .placeholder(placeholderId)
                .error(R.drawable.flicks_movie_placeholder)
                .into(imageView);
    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            // Lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            // Add this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Gets item position
            int position = getAdapterPosition();
            // Make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // Get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // Create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // Serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Show the activity
                context.startActivity(intent);
            }
        }
    }
}
