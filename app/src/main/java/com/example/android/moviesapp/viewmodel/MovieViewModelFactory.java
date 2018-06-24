package com.example.android.moviesapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    public MovieViewModelFactory(){
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MovieViewModel();
    }
}
