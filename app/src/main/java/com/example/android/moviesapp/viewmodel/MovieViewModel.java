package com.example.android.moviesapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
    String query;
    String currentList;
    int topRatedPageNumber;
    int popularPageNumber;
    int customPageNumber;
    String lastCalledList;
    int recyclerViewPosition;
    List<Movie> popularMovieListCollection = new ArrayList<>();
    List<Movie> topRatedMovieListCollection = new ArrayList<>();
    Observable<List<Movie>> popularMovieList;
    Observable<List<Movie>> topRatedMovieList;
    Observable<List<Movie>> customQueryMovieList;

    public void reducePageNumberByOne() {
        if (lastCalledList.equals("topRated")) {

            topRatedPageNumber--;
        } else if (lastCalledList.equals("popular")) {
            popularPageNumber--;
        }
    }

    public void setRecyclerViewPosition(int recyclerViewPosition) {
        this.recyclerViewPosition = recyclerViewPosition;
    }

    public String getCurrentList() {
        return currentList;
    }

    public void setCurrentList(String currentList) {
        this.currentList = currentList;
    }

    public int getTopRatedPageNumber() {
        return topRatedPageNumber;
    }

    public void setTopRatedPageNumber(int topRatedPageNumber) {
        if (topRatedPageNumber > this.topRatedPageNumber && topRatedPageNumber > 0) {
            lastCalledList = "topRated";
            topRatedMovieList = NetworkUtils.loadTopRatedMoviesList(topRatedPageNumber)
                    .observeOn(Schedulers.computation())
                    .flatMap(s -> {
                        topRatedMovieListCollection.addAll(s);
                        return Observable.just(topRatedMovieListCollection);
                    });
            this.topRatedPageNumber = topRatedPageNumber;
        }
    }

    public int getPopularPageNumber() {
        return popularPageNumber;
    }

    public void setPopularPageNumber(int popularPageNumber) {
        if (popularPageNumber > this.popularPageNumber && popularPageNumber > 0) {
            lastCalledList = "popular";
            popularMovieList = NetworkUtils.loadPopularMoviesList(popularPageNumber)
                    .observeOn(Schedulers.computation())
                    .flatMap(s -> {
                        popularMovieListCollection.addAll(s);
                        return Observable.just(popularMovieListCollection);
                    });
            this.popularPageNumber = popularPageNumber;
        }
    }

    public int getCustomPageNumber() {
        return customPageNumber;
    }

    public void setCustomQuery(String query, int customPageNumber) {
        if (query != null) {
            customQueryMovieList = NetworkUtils.getCustomMovieByQuery(query, customPageNumber);
            Log.e("creating custom", "creating custom");
            this.query = query;
            this.customPageNumber = customPageNumber;
        }

    }

    public String getQuery() {
        return query;
    }

    public Observable<List<Movie>> getPopularMovieList() {
        return popularMovieList;
    }

    public void setPopularMovieList(Observable<List<Movie>> popularMovieList) {
        this.popularMovieList = popularMovieList;
    }

    public Observable<List<Movie>> getTopRatedMovieList() {
        return topRatedMovieList;
    }

    public void setTopRatedMovieList(Observable<List<Movie>> topRatedMovieList) {
        this.topRatedMovieList = topRatedMovieList;
    }

    public Observable<List<Movie>> getCustomQueryMovieList() {
        return customQueryMovieList;
    }

}
