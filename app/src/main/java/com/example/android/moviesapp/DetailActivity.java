package com.example.android.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.moviesapp.adapter.MovieListAdapter;
import com.example.android.moviesapp.model.Cast;
import com.example.android.moviesapp.model.FinalMoviePrototype;
import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.model.Review;
import com.example.android.moviesapp.model.Video;
import com.example.android.moviesapp.model.VideoListApiResponse;
import com.example.android.moviesapp.utils.MovieUtils;
import com.example.android.moviesapp.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.detail_large_background_poster) ImageView largeBackgroundPoster;
    @BindView(R.id.detail_movie_poster) ImageView moviePoster;
    @BindView(R.id.detail_movie_name) TextView movieName;
    @BindView(R.id.detail_movie_overview) TextView movieOverview;
    @BindView(R.id.detail_movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.detail_movie_ratings) TextView movieRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("id");
        int position = getIntent().getIntExtra("position",-1);
        FinalMoviePrototype prototype=MovieUtils.getMovieModelWithId(id,position);
        Movie movie = prototype.getMovie();
        if(movie!=null){
            Glide.with(this).load(movie.getBackdropPath()).into(largeBackgroundPoster);
            Glide.with(this).load(movie.getPosterPath()).into(moviePoster);
            movieName.setText(movie.getTitle());
            movieRating.setText(String.valueOf((int)movie.getVoteAverage().doubleValue())+" /10");
            movieOverview.setText(movie.getOverview());
            movieReleaseDate.setText(movie.getReleaseDate());
        }
    }
}
