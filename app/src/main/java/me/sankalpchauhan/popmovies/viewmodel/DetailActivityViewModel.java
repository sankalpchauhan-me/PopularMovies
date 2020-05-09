package me.sankalpchauhan.popmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import me.sankalpchauhan.popmovies.BuildConfig;
import me.sankalpchauhan.popmovies.service.model.Review;
import me.sankalpchauhan.popmovies.service.model.Trailers;
import me.sankalpchauhan.popmovies.service.repository.MovieRepository;

/**
 * DetailnActivityViewModel to persist data and survive configuration changes while limiting precious api network calls
 */
public class DetailActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Review> reviewMutableLiveData;
    private MutableLiveData<Trailers> trailersMutableLiveData;
    private MovieRepository movieRepository;

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(Integer movieId) {
        if (reviewMutableLiveData != null && trailersMutableLiveData != null) {
            return;
        }
        movieRepository = movieRepository.getInstance();
        reviewMutableLiveData = movieRepository.getMovieReview(BuildConfig.API_KEY, movieId);
        trailersMutableLiveData = movieRepository.getMovieTrailers(BuildConfig.API_KEY, movieId);
    }

    public LiveData<Trailers> getMovieTrailer() {
        return trailersMutableLiveData;
    }

    public LiveData<Review> getMovieReview() {
        return reviewMutableLiveData;
    }
}
