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

import com.example.phil.popularmovies.R;
import com.example.phil.popularmovies.Video;

import java.util.ArrayList;
import java.util.List;

import static com.squareup.picasso.Picasso.with;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private List<Video> mVideos = new ArrayList<>();


    public VideoAdapter(Context context, List<Video> videos) {
        this.mContext = context;
        this.mVideos = videos;

    }

    public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView youTubePlayerView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            youTubePlayerView = (ImageView) itemView.findViewById(R.id.youtube_thumbnail);
            youTubePlayerView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //
            Video videoPosition = mVideos.get(getAdapterPosition());
            Context context = itemView.getContext();
            String VIDEO_ID = videoPosition.getKey();
            /*Intent for trailer videos. Opens YouTube app if installed on phone, if not browser
            will open on phone.
             */
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + VIDEO_ID));
            context.startActivity(intent);

        }
    }

    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        int layoutIdForVideos = R.layout.videos_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);


        View view = inflater.inflate(layoutIdForVideos, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        final Video video = mVideos.get(position);
        //YouTube Thumbnails
        Uri builder = Uri.parse("https://img.youtube.com/vi/").buildUpon()
                .appendEncodedPath(video.getKey() + "/0.jpg")
                .build();

        with(mContext).load(builder).into(holder.youTubePlayerView);


    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }
}
