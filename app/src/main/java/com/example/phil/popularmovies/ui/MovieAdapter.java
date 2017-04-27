package com.example.phil.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phil.popularmovies.Movie;
import com.example.phil.popularmovies.R;

import java.util.List;

import butterknife.BindView;

import static com.squareup.picasso.Picasso.with;

/**
 * Created by phil on 4/1/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // For logging purposes
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private List<Movie> mMovies;

    @BindView(R.id.details_poster)
    ImageView posterImageView;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieItemView;
        Movie movie;



        public MovieViewHolder(View itemView) {
            super(itemView);

            movieItemView = (ImageView) itemView.findViewById(R.id.movie_poster);
            movieItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Movie moviePosition = mMovies.get(getAdapterPosition());

            Context context = itemView.getContext();
            Intent userClick = new Intent(context, DetailActivity.class);
            userClick.putExtra("original_title", moviePosition.getOriginalTitle());
            userClick.putExtra("release_date", moviePosition.getReleaseDate());
            userClick.putExtra("vote_average", moviePosition.getVoteAverage());
            userClick.putExtra("overview",moviePosition.getOverview());
            userClick.putExtra("movie", moviePosition);
            context.startActivity(userClick);

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

        final Movie movie = mMovies.get(position);
        Uri builder = Uri.parse("http://image.tmdb.org/t/p/w500").buildUpon()
                .appendEncodedPath(movie.getPosterPath())
                .build();

        with(mContext).load(builder).into(holder.movieItemView);

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
