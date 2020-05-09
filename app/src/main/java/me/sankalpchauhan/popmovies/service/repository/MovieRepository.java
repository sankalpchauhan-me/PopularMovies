package me.sankalpchauhan.popmovies.service.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import me.sankalpchauhan.popmovies.service.model.Movies;
import me.sankalpchauhan.popmovies.service.model.Review;
import me.sankalpchauhan.popmovies.service.model.Trailers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The movie repository fetches all the requests from the netowrk
 */

public class MovieRepository {
    private static MovieRepository movieRepository;
    private TMDBApi movieApi;

    public MovieRepository() {
        movieApi = RetrofitService.createService(TMDBApi.class);
    }

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            movieRepository = new MovieRepository();
        }
        return movieRepository;
    }

    public MutableLiveData<Movies> getTopRatedMovies(String apikey) {
        final MutableLiveData<Movies> moviesMutableLiveData = new MutableLiveData<>();
        movieApi.getTopRatedMovies(apikey).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    moviesMutableLiveData.setValue(response.body());
                } else {
                    moviesMutableLiveData.setValue(null);
                    Log.d("API Request", "Request failed with error" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                //Log.d("Test", t.toString());
                moviesMutableLiveData.setValue(null);
            }
        });
        return moviesMutableLiveData;
    }

    public MutableLiveData<Movies> getPopularMovies(String apikey) {
        final MutableLiveData<Movies> moviesMutableLiveData = new MutableLiveData<>();
        movieApi.getPopularMovies(apikey).enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    moviesMutableLiveData.setValue(response.body());
                } else {
                    moviesMutableLiveData.setValue(null);
                    Log.d("API Request", "Request failed with error" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                Log.e("Test", t.toString());
                moviesMutableLiveData.setValue(null);
            }
        });
        return moviesMutableLiveData;
    }

    public MutableLiveData<Review> getMovieReview(String apikey, Integer movieid) {
        final MutableLiveData<Review> reviewMutableLiveData = new MutableLiveData<>();
        movieApi.getMovieReviews(movieid, apikey).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    reviewMutableLiveData.setValue(response.body());
                } else {
                    reviewMutableLiveData.setValue(null);
                    Log.d("API Request", "Request failed with error" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                //Log.d("Test", t.toString());
                reviewMutableLiveData.setValue(null);
            }
        });

        return reviewMutableLiveData;
    }

    public MutableLiveData<Trailers> getMovieTrailers(String apikey, Integer movieid) {
        final MutableLiveData<Trailers> trailersMutableLiveData = new MutableLiveData<>();
        movieApi.getMovieTrailers(movieid, apikey).enqueue(new Callback<Trailers>() {
            @Override
            public void onResponse(Call<Trailers> call, Response<Trailers> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    trailersMutableLiveData.setValue(response.body());
                } else {
                    trailersMutableLiveData.setValue(null);
                    Log.d("API Request", "Request failed with error" + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Trailers> call, Throwable t) {
                //Log.d("Test", t.toString());
                trailersMutableLiveData.setValue(null);
            }
        });

        return trailersMutableLiveData;
    }
}
