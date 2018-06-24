package com.example.android.moviesapp.adapter;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.moviesapp.DetailActivity;
import com.example.android.moviesapp.R;
import com.example.android.moviesapp.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private static final int SCALE_ANIMATION_DURATION = 400;
    public static List<Movie> dataList;
    ViewHolder holder;
    int lastItemPosition;

    public MovieListAdapter(List<Movie> dataList) {
        MovieListAdapter.dataList = dataList;
    }

    public void setDataList(List<Movie> dataList) {
        MovieListAdapter.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.holder = holder;
        holder.bind(dataList.get(position));
        if (lastItemPosition > holder.getAdapterPosition())
            startAnimation(holder.itemView);
    }

    private void startAnimation(View v) {
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.1f);
        animation.setDuration(SCALE_ANIMATION_DURATION);
        v.startAnimation(animation);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
        lastItemPosition = holder.getAdapterPosition();
    }

    public int getLastAdapterPosition() {
        return holder.getAdapterPosition() - 10;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        @Nullable
        @BindView(R.id.moviePoster)
        ImageView movieListItemPoster;
        @Nullable
        @BindView(R.id.movieTitle)
        TextView movieListItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            movieListItemName.setText("");
            Glide.with(itemView).load(movie.getPosterPath()).into(movieListItemPoster);
        }

        @OnClick(R.id.moviePoster)
        public void launchDetailActivity(View v) {
            Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", String.valueOf(dataList.get(getAdapterPosition()).getId()));
            intent.putExtra("position", getAdapterPosition());
            itemView.getContext().startActivity(intent);
        }
    }
}
