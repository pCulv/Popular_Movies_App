package com.example.phil.popularmovies.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.phil.popularmovies.R;
import com.example.phil.popularmovies.Review;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by phil on 5/6/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private Context mContext;
    private List<Review> mReviews = new ArrayList<>();
    private Review review;
    private LinearLayout reviewsListItem;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mReviews = reviews;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView contentTextView;


        public ReviewsViewHolder(View itemView) {
            super(itemView);

            authorTextView = (TextView) itemView.findViewById(R.id.tv_item_author);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_item_content);

        }

        void bind(int itemIndex) {


            authorTextView.setText("author");
            contentTextView.setText("Help me please");
        }

    }

    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        this.mContext = viewGroup.getContext();
        int layoutIdForReviews = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);


        View view = inflater.inflate(layoutIdForReviews, viewGroup, false);
        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        //TODO: populate itemView with Author and content

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }


}
