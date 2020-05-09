package me.sankalpchauhan.popmovies.service.repository;

import me.sankalpchauhan.popmovies.service.model.Movies;
import me.sankalpchauhan.popmovies.service.model.Review;
import me.sankalpchauhan.popmovies.service.model.Trailers;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The API End Points
 */
public interface TMDBApi {
    @GET("movie/popular")
    Call<Movies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<Movies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<Review> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<Trailers> getMovieTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
