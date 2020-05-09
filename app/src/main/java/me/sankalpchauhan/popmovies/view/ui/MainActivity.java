package me.sankalpchauhan.popmovies.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import me.sankalpchauhan.popmovies.BuildConfig;
import me.sankalpchauhan.popmovies.R;
import me.sankalpchauhan.popmovies.service.model.MovieResult;
import me.sankalpchauhan.popmovies.service.model.Movies;
import me.sankalpchauhan.popmovies.utils.Constants;
import me.sankalpchauhan.popmovies.utils.Utility;
import me.sankalpchauhan.popmovies.view.adapter.MovieAdapter;
import me.sankalpchauhan.popmovies.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    ArrayList<MovieResult> movieArrayList = new ArrayList<>();
    ActionBar ab;
    private ProgressBar progressBar;
    private RecyclerView movieRecyclerView;
    private TextView errorMessage;
    private String sortingPref = "";
    private MovieAdapter movieAdapter;
    private Button retry;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ab = getSupportActionBar();

        if (savedInstanceState != null) {
            sortingPref = savedInstanceState.getString(Constants.SORT_BY);
        }
        progressBar = findViewById(R.id.progressbar);
        movieRecyclerView = findViewById(R.id.movie_rv);
        errorMessage = findViewById(R.id.tv_error_message_display);
        retry = findViewById(R.id.retry_button);
        apiKeyCheckForReference();

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columns, RecyclerView.VERTICAL, false);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);
        loadMovieData(sortingPref);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: retry request with clone()
                Intent intent = getIntent();
                finishAffinity();
                finish();
                //Log.d(getLocalClassName(), retryMessage);
                startActivity(intent);
            }
        });

    }

    private void loadMovieData(String sortingPref) {
//        if (Utility.isOnline()) {
        errorMessage.setVisibility(View.INVISIBLE);
        movieRecyclerView.setVisibility(View.VISIBLE);
        //new FetchedMovieData().execute(sortingPref);

//        }
//        else {
//            errorMessage.setText(getResources().getString(R.string.connection_error));
//            errorMessage.setVisibility(View.VISIBLE);
//            retry.setVisibility(View.VISIBLE);
//        }
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.init();
        if (sortingPref.equals(Constants.POPULAR) || sortingPref.equals("")) {
            mainActivityViewModel.getPopularMovieRepository().observe(this, new Observer<Movies>() {
                @Override
                public void onChanged(Movies movieResult) {
                    progressBar.setVisibility(View.GONE);
                    if (movieResult != null) {
                        if (sortingPref.equals(Constants.POPULAR)) {
                            ab.setTitle("Most Popular");
                        }
                        movieArrayList.clear();
                        movieAdapter.setMovieData(null);
                        List<MovieResult> movies = movieResult.getMovieResults();
                        movieArrayList.addAll(movies);
                        movieAdapter.setMovieData(movieArrayList);
                        retry.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(getLocalClassName(), "Response is null");
                        Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        retry.setVisibility(View.VISIBLE);
                        errorMessage.setVisibility(View.VISIBLE);
                        if (!Utility.isOnline()) {
                            errorMessage.setText(getResources().getString(R.string.please_check_network_connection));
                        }
                    }
                }
            });
        } else if (sortingPref.equals(Constants.TOP_RATED)) {
            mainActivityViewModel.getTopRatedMovieRepository().observe(this, new Observer<Movies>() {
                @Override
                public void onChanged(Movies movieResult) {
                    progressBar.setVisibility(View.GONE);
                    if (movieResult != null) {
                        ab.setTitle("Top Rated");
                        movieArrayList.clear();
                        movieAdapter.setMovieData(null);
                        List<MovieResult> movies = movieResult.getMovieResults();
                        movieArrayList.addAll(movies);
                        movieAdapter.setMovieData(movieArrayList);
                        retry.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(getLocalClassName(), "Response is null");
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                        retry.setVisibility(View.VISIBLE);
                        errorMessage.setVisibility(View.VISIBLE);
                        if (!Utility.isOnline()) {
                            errorMessage.setText(getResources().getString(R.string.please_check_network_connection));
                        }
                    }
                }
            });
        } else if (sortingPref.equals(Constants.FAVOURITE)) {
            mainActivityViewModel.getFavMovies().observe(this, new Observer<List<MovieResult>>() {
                @Override
                public void onChanged(List<MovieResult> movieResults) {
                    movieArrayList.clear();
                    movieAdapter.setMovieData(null);
                    if (movieResults != null) {
                        ab.setTitle("Favorites");
                        movieArrayList.addAll(movieResults);
                        movieAdapter.setMovieData(movieArrayList);
                        retry.setVisibility(View.INVISIBLE);
                    } else {
                        Log.d(getLocalClassName(), "No Movies Favorite");
                        //Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        errorMessage.setText(getResources().getString(R.string.no_movies_favorite));
                        //retry.setVisibility(View.VISIBLE);
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                }
            });
            if (movieAdapter.getItemCount() == 0) {

            }
        }
    }

    @Override
    public void onClick(MovieResult movieItem) {
        Intent activityIntent = new Intent(this, DetailActivity.class);
        activityIntent.putExtra(Constants.MOVIE_INTENT, movieItem);
        startActivity(activityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.highest_rated_sort:
                sortingPref = Constants.TOP_RATED;
                loadMovieData(sortingPref);
                return true;
            case R.id.most_popular_sort:
                sortingPref = Constants.POPULAR;
                loadMovieData(sortingPref);
                return true;
            case R.id.favorites:
                sortingPref = Constants.FAVOURITE;
                loadMovieData(sortingPref);
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovieData(sortingPref);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.SORT_BY, sortingPref);
    }

    /**
     * For Debug Purposes
     */
    public void apiKeyCheckForReference() {
        if (BuildConfig.API_KEY.equals("YOUR_API_KEY")) {
            errorMessage.setText("API Key Not Placed \nTo successfully run the project place The Movie DB API key inside gradle.properties in THE_MOVIE_DB_API_KEY variable");
            errorMessage.setVisibility(View.VISIBLE);
            Log.e("API KEY ERROR!!", "\n \n \n API Key Not Placed To successfully run the project place The Movie DB API key inside gradle.properties in THE_MOVIE_DB_API_KEY variable \n \n \n");
        }
    }

    /**
     * AsyncTask was used in stage-1 of popular movies app keeping here for reference
     */
//    class FetchedMovieData extends AsyncTask<String, Void, List<Movie>> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//            retry.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        protected List<Movie> doInBackground(String... strings) {
//
//            String sortingParam = strings[0];
//            URL tmdbUrl = NetworkUtils.buildUrl(sortingParam);
//
//            try {
//                String jsonMovieRespone = NetworkUtils.getResponseFromHttpUrl(tmdbUrl);
//                //Log.d("Test", jsonMovieRespone);
//                return JsonUtils.getMoviesFromJson(jsonMovieRespone);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(List<Movie> movies) {
//            super.onPostExecute(movies);
//            progressBar.setVisibility(View.INVISIBLE);
//            if (movies != null) {
//                movieRecyclerView.setVisibility(View.VISIBLE);
//                errorMessage.setVisibility(View.INVISIBLE);
//                movieAdapter.setMovieData(movies);
//            } else {
//                movieRecyclerView.setVisibility(View.INVISIBLE);
//                errorMessage.setVisibility(View.VISIBLE);
//                retry.setVisibility(View.VISIBLE);
//            }
//        }
//    }
}
