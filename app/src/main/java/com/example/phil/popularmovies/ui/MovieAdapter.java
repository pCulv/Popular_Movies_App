package com.example.phil.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by phil on 4/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // For logging purposes
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieItemView;
        Movie mMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieItemView = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent userClick = new Intent(context, DetailActivity.class);
            userClick.putExtra("movie", mMovie);
            context.startActivity(userClick);

        }

        public void bindMovie(Movie movie) {
            mMovie = movie;
            String BASE_URL = "http://image.tmdb.org/t/p/";

            movie.setPosterPath(BASE_URL + String.valueOf(imageWidth) + "/" + movie.getPosterPath());

            Picasso.with(mContext).load(movie.getPosterPath()).into(movieItemView);

        }
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        this.mContext = viewGroup.getContext();
        int layoutIdForMovies = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);


        View view = inflater.inflate(layoutIdForMovies, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        Movie movieImage = mMovies.get(position);

        holder.bindMovie(movieImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    int imageWidth;

    public void setImageWidth(int imageWidth) {
        //make poster size more pixel perfect
        if (imageWidth>700) {
            this.imageWidth = 500;
        }else if(imageWidth>500){
            this.imageWidth = 500;
        }else if (imageWidth>300){
            this.imageWidth=342;
        }else{
            this.imageWidth=185;
        }

    }


}
