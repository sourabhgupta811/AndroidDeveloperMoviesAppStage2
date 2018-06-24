package com.example.android.moviesapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.example.android.moviesapp.MainActivity;
import com.example.android.moviesapp.utils.NetworkUtils;

public class NetworkReceiver extends BroadcastReceiver {
    MainActivity activity;
    public NetworkReceiver(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context!=null && MainActivity.isActivityVisible) {
            NetworkInfo activeNetworkInfo = NetworkUtils.getNetworkInfo(context);
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected() && activity.errorLayout.getVisibility() == View.VISIBLE) {
                activity.animationView.playAnimation();
                activity.animationView.setVisibility(View.VISIBLE);
//                activity.movieListRecyclerView.setVisibility(View.VISIBLE);
                activity.errorLayout.setVisibility(View.GONE);
                activity.loadPopularMoviesIntoRecyclerView();
            }
        }
    }
}
