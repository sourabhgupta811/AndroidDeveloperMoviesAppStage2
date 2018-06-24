package com.example.android.moviesapp.utils;

import com.example.android.moviesapp.adapter.MovieListAdapter;
import com.example.android.moviesapp.model.Cast;
import com.example.android.moviesapp.model.FinalMoviePrototype;
import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.model.Review;
import com.example.android.moviesapp.model.Video;

import java.util.List;

import io.reactivex.Observable;

public class MovieUtils {
    public static FinalMoviePrototype getMovieModelWithId(String id,int position){
        Observable<List<Video>> videoObservable = NetworkUtils.loadVideoWithId(id);
        Observable<List<Review>> reviewObservable = NetworkUtils.loadReviewWithId(id);
        Observable<List<Cast>> castObservable = NetworkUtils.loadCastWithId(id);

        Movie movie = MovieListAdapter.dataList.get(position);
//        Observable.zip(videoObservable,reviewObservable,castObservable,(a,b,c)->{moviePrototype = new FinalMoviePrototype(b,a,c,movie)})
//                .subscribe(System.out::println);
        return new FinalMoviePrototype(reviewObservable,videoObservable,castObservable,movie);

    }
}
