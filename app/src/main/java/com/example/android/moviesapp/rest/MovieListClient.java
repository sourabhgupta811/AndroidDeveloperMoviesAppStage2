package com.example.android.moviesapp.rest;

import com.example.android.moviesapp.model.Cast;
import com.example.android.moviesapp.model.CastListApiResponse;
import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.model.MovieListApiResponse;
import com.example.android.moviesapp.model.ReviewListApiResponse;
import com.example.android.moviesapp.model.VideoListApiResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieListClient {
    @GET("3/movie/top_rated")
    Observable<MovieListApiResponse> getTopRatedMovieListResponse(@Query("api_key") String api_key,@Query("page") int page);
    @GET("3/movie/popular")
    Observable<MovieListApiResponse> getPopularMovieListResponse(@Query("api_key") String api_key,@Query("page") int page);
    @GET("/3/movie/{id}")
    Observable<Movie> getMovieById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/reviews")
    Observable<ReviewListApiResponse> getReviewById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/videos")
    Observable<VideoListApiResponse> getVideoById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/movie/{id}/credits")
    Observable<CastListApiResponse> getCastById(@Path("id") String id, @Query("api_key") String api_key);
    @GET("/3/search/multi")
    Observable<MovieListApiResponse> getCustomMovieByQuery(@Query("api_key") String api_key, @Query("query") String query,@Query("language") String language, @Query("page") int page);

}
