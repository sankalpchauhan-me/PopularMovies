package me.sankalpchauhan.popmovies.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import me.sankalpchauhan.popmovies.BuildConfig;
import me.sankalpchauhan.popmovies.database.AppDatabase;
import me.sankalpchauhan.popmovies.service.model.MovieResult;
import me.sankalpchauhan.popmovies.service.model.Movies;
import me.sankalpchauhan.popmovies.service.repository.MovieRepository;

/**
 * MainActivityViewModel to persist data and survive configuration changes while limiting precious api network calls
 */
public class MainActivityViewModel extends AndroidViewModel {
    // Constant for logging
    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private MutableLiveData<Movies> movieResultMutableLiveData;
    private MovieRepository movieRepository;
    private LiveData<List<MovieResult>> favMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favMovies = database.movieDao().getAllFavMovies();
    }

    public void init() {
        if (movieResultMutableLiveData != null) {
            return;
        }
        movieRepository = movieRepository.getInstance();
    }

    public LiveData<Movies> getPopularMovieRepository() {
        movieResultMutableLiveData = movieRepository.getPopularMovies(BuildConfig.API_KEY);
        return movieResultMutableLiveData;
    }

    public LiveData<Movies> getTopRatedMovieRepository() {
        movieResultMutableLiveData = movieRepository.getTopRatedMovies(BuildConfig.API_KEY);
        return movieResultMutableLiveData;
    }

    public LiveData<List<MovieResult>> getFavMovies() {
        return favMovies;
    }

}
