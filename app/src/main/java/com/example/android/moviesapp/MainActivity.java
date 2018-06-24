package com.example.android.moviesapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.example.android.moviesapp.adapter.MovieListAdapter;
import com.example.android.moviesapp.model.Movie;
import com.example.android.moviesapp.receiver.NetworkReceiver;
import com.example.android.moviesapp.utils.NetworkUtils;
import com.example.android.moviesapp.viewmodel.MovieViewModel;
import com.example.android.moviesapp.viewmodel.MovieViewModelFactory;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String POPULAR_MOVIE_LIST = "popular";
    private static final String TOP_RATED_MOVIE_LIST = "top_rated";
    private static final String CUSTOM_MOVIE_LIST = "custom";
    public static boolean isActivityVisible;
    @BindView(R.id.movies_list)
    public RecyclerView movieListRecyclerView;
    @BindView(R.id.error_view)
    public FrameLayout errorLayout;
    @BindView(R.id.loading_movie_animation)
    public LottieAnimationView animationView;
    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_navigation_view)
    NavigationView navigationView;
    List<Movie> dataList = new ArrayList<>();
    List<String> suggestionsList = new ArrayList<>();
    int popularCurrentPageNumber;
    int topRatedCurrentPageNumber;
    int customCurrentPageNumber;
    String currentList;
    MovieListAdapter adapter;
    Disposable movieListDisposable;
    Observable<List<Movie>> suggestionObservable;
    Disposable suggestionDisposable;
    MovieViewModel viewModel;
    NetworkReceiver reciever;
    NavigationView.OnNavigationItemSelectedListener navigationListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.top_rated) {
                if (viewModel.getTopRatedMovieList() != null) {
                    viewModel.setCurrentList(TOP_RATED_MOVIE_LIST);
                    loadRecyclerViewWithObservable(viewModel.getTopRatedMovieList());
                } else {
                    loadTopRatedMoviesIntoRecyclerView();
                }
            } else if (item.getItemId() == R.id.most_favourite) {
                if (viewModel.getPopularMovieList() != null) {
                    viewModel.setCurrentList(POPULAR_MOVIE_LIST);
                    loadRecyclerViewWithObservable(viewModel.getPopularMovieList());
                } else {

                    loadPopularMoviesIntoRecyclerView();
                }
            }
            drawerLayout.closeDrawer(Gravity.START);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_navigation);
        ButterKnife.bind(this);

        reciever = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(reciever, filter);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.closing_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(navigationListener);
        navigationView.getMenu().getItem(2).setChecked(true);
        activateSearchBar();
        MovieViewModelFactory factory = new MovieViewModelFactory();
        viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        popularCurrentPageNumber = viewModel.getPopularPageNumber() > 0 ? viewModel.getPopularPageNumber() : 1;
        topRatedCurrentPageNumber = viewModel.getTopRatedPageNumber() > 0 ? viewModel.getTopRatedPageNumber() : 1;
        customCurrentPageNumber = viewModel.getCustomPageNumber() > 0 ? viewModel.getCustomPageNumber() : 1;
        currentList = viewModel.getCurrentList() != null ? viewModel.getCurrentList() : "null";
        GridLayoutManager layoutManager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 5);
        }
        movieListRecyclerView.setLayoutManager(layoutManager);
        adapter = new MovieListAdapter(dataList);
        addScrollListener();
        movieListRecyclerView.setAdapter(adapter);
        if (currentList.equals(TOP_RATED_MOVIE_LIST)) {
            loadTopRatedMoviesIntoRecyclerView();
        } else if (currentList.equals(CUSTOM_MOVIE_LIST))
            loadCustomMoviesIntoRecyclerView(viewModel.getQuery());
        else if (currentList.equals(POPULAR_MOVIE_LIST)) {
            popularCurrentPageNumber = viewModel.getPopularPageNumber();
            loadPopularMoviesIntoRecyclerView();
        } else
            loadPopularMoviesIntoRecyclerView();
    }

    private void addScrollListener() {
        movieListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    if (currentList.equals(TOP_RATED_MOVIE_LIST))
                        loadTopRatedMoviesIntoRecyclerView();
                    else if (currentList.equals(POPULAR_MOVIE_LIST))
                        loadPopularMoviesIntoRecyclerView();
                    viewModel.setRecyclerViewPosition(((MovieListAdapter) movieListRecyclerView.getAdapter()).getLastAdapterPosition());
                }
            }
        });
    }

    public void loadPopularMoviesIntoRecyclerView() {
        currentList = POPULAR_MOVIE_LIST;
        viewModel.setCurrentList(POPULAR_MOVIE_LIST);
        viewModel.setPopularPageNumber(viewModel.getPopularPageNumber() + 1);
        Observable<List<Movie>> movieList = viewModel.getPopularMovieList();
        loadRecyclerViewWithObservable(movieList);
    }

    private void loadTopRatedMoviesIntoRecyclerView() {
        currentList = TOP_RATED_MOVIE_LIST;
        viewModel.setCurrentList(TOP_RATED_MOVIE_LIST);
        viewModel.setTopRatedPageNumber(viewModel.getTopRatedPageNumber() + 1);
        Observable<List<Movie>> movieList = viewModel.getTopRatedMovieList();
        loadRecyclerViewWithObservable(movieList);
    }

    private void loadCustomMoviesIntoRecyclerView(String query) {
        currentList = CUSTOM_MOVIE_LIST;
        viewModel.setCurrentList(currentList);
        viewModel.setCustomQuery(query, 1);
        Observable<List<Movie>> movieList = viewModel.getCustomQueryMovieList();
        loadRecyclerViewWithObservable(movieList);
    }

    private void loadRecyclerViewWithObservable(Observable<List<Movie>> movieList) {
        if (movieList != null)
            movieListDisposable = movieList
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(list -> {
                        adapter.setDataList(list);
                        if (animationView.getVisibility() == View.VISIBLE) {
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                            movieListRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }, error -> {
                        if (movieListRecyclerView.getAdapter().getItemCount() <= 0) {
                            movieListRecyclerView.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.VISIBLE);
                            viewModel.reducePageNumberByOne();
                        } else {
                            Log.e("error", error.getLocalizedMessage());
                            movieListRecyclerView.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                            Snackbar.make(movieListRecyclerView, "Connection error!", Snackbar.LENGTH_SHORT).show();
                            viewModel.reducePageNumberByOne();
                        }
                    }, () -> {
                        if (movieListDisposable != null) movieListDisposable.dispose();
                    });
    }

    private void activateSearchBar() {
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.equals("")) {
                    loadCustomMoviesIntoRecyclerView(query);
                } else {
                    loadPopularMoviesIntoRecyclerView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.equals("")) {
                    suggestionObservable = NetworkUtils.getCustomMovieByQuery(newText, 1);
                    suggestionsList.clear();
                    suggestionDisposable = suggestionObservable
                            .flatMap(s -> Observable.fromIterable(s))
                            .observeOn(Schedulers.computation())
                            .filter(s -> s.getTitle() != null && !s.getTitle().equals("null"))
                            .take(6)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> suggestionsList.add(s.getTitle()),
                                    Throwable::printStackTrace,
                                    () -> {
                                        if (suggestionsList != null && suggestionsList.size() > 0)
                                            searchView.setSuggestions(suggestionsList.toArray(new String[0]));
                                    }
                            );
                } else if (newText != null && newText.equals("")) {
                    searchView.setSuggestions(null);
                }
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MenuItem search = menu.findItem(R.id.search_new_movie);
        searchView.setMenuItem(search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_new_movie) {
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(reciever);
        if (movieListDisposable != null)
            movieListDisposable.dispose();
        if (suggestionDisposable != null)
            suggestionDisposable.dispose();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityVisible = false;
    }
}
