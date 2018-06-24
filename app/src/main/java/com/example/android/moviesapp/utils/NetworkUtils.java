package com.example.android.moviesapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.moviesapp.model.Cast;
import com.example.android.moviesapp.model.CastListApiResponse;
import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.model.MovieListApiResponse;
import com.example.android.moviesapp.model.Review;
import com.example.android.moviesapp.model.ReviewListApiResponse;
import com.example.android.moviesapp.model.Video;
import com.example.android.moviesapp.model.VideoListApiResponse;
import com.example.android.moviesapp.rest.MovieListClient;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private static final String SCHEMA="https";
    private static final String API_KEY="9b7f898dc2aaf753630ac6af5f63a500";
    private static final String BASE_URL="api.themoviedb.org";
//    private static final String PATH="3/discover/movie";
    private static final String PATH="3/movie/popular";
    private static final String LANGUGAE = "en-US";
    private String sortOrder;
    private static final String SORT_QUERY="sort_by";
    private static final String API_QUERY="api_key";
    public static final Retrofit retrofit = getRetrofitInstance();

    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    private static Retrofit getRetrofitInstance() {
        Retrofit.Builder builder = new Retrofit.Builder();
        Uri uri=new Uri.Builder().scheme(SCHEMA).authority(BASE_URL).build();
        Log.e("response",uri.toString());
        builder.baseUrl(uri.toString())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return builder.build();
    }

    public static Observable<List<Movie>> loadPopularMoviesList(int page){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<MovieListApiResponse> response=client.getPopularMovieListResponse(API_KEY,page);
        return response.subscribeOn(Schedulers.io())
                .map(s-> s.getResults());
    }

    public static Observable<List<Movie>> loadTopRatedMoviesList(int page){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<MovieListApiResponse> response=client.getTopRatedMovieListResponse(API_KEY,page);
        return response.subscribeOn(Schedulers.io())
                .map(s-> s.getResults());
    }

    public static Observable<List<Video>> loadVideoWithId(String id){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<VideoListApiResponse> videoCall = client.getVideoById(id,API_KEY);
        return videoCall.subscribeOn(Schedulers.io())
                .map(s-> s.getResults());
    }

    public static Observable<List<Review>> loadReviewWithId(String id){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<ReviewListApiResponse> reviewCall = client.getReviewById(id,API_KEY);
        return reviewCall.subscribeOn(Schedulers.io())
                .map(s-> s.getResults());
    }
    public static Observable<List<Cast>> loadCastWithId(String id){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<CastListApiResponse> castCall = client.getCastById(id,API_KEY);
        return castCall.subscribeOn(Schedulers.io())
                .map(s-> s.getCast());
    }

    public static Observable<List<Movie>> getCustomMovieByQuery(String query,int page){
        MovieListClient client=retrofit.create(MovieListClient.class);
        Observable<MovieListApiResponse> response=client.getCustomMovieByQuery(API_KEY,query,LANGUGAE,page);
        return response.subscribeOn(Schedulers.io())
                .map(s-> s.getResults());
    }
}
