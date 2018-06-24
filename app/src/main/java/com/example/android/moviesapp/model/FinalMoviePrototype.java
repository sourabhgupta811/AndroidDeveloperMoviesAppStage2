package com.example.android.moviesapp.model;

import java.util.List;

import io.reactivex.Observable;

public class FinalMoviePrototype {
    private Observable<List<Review>> reviews;
    private Observable<List<Video>> videos;
    private Observable<List<Cast>> casts;

    public Observable<List<Review>> getReviews() {
        return reviews;
    }

    public void setReviews(Observable<List<Review>> reviews) {
        this.reviews = reviews;
    }

    public Observable<List<Video>> getVideos() {
        return videos;
    }

    public void setVideos(Observable<List<Video>> videos) {
        this.videos = videos;
    }

    public Observable<List<Cast>> getCasts() {
        return casts;
    }

    public void setCasts(Observable<List<Cast>> casts) {
        this.casts = casts;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public FinalMoviePrototype(Observable<List<Review>> reviews, Observable<List<Video>> videos, Observable<List<Cast>> casts, Movie movie) {
        this.reviews = reviews;
        this.videos = videos;
        this.casts = casts;
        this.movie = movie;

    }

    Movie movie;

}
