package me.sankalpchauhan.popmovies.utils;

public class Constants {
    public static final String RESULTS = "results";
    public static final String TITLE = "title";
    public static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";
    public static final String BASE_BACKDROP_PATH = "http://image.tmdb.org/t/p/w500/";
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    public static final String POSTER_PATH = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String OVERVIEW = "overview";
    public static final String SORT_BY = "sort_by";
    public static final String DATABASE_NAME = "tmdb";
    public static final String MOVIE_INTENT = "MovieData";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String FAVOURITE = "fav";
    //max number of retries before fail
    public static final int maxLimit = 3;
    // wait for 5 second before retrying network request
    public static final int waitThreshold = 5000;
}
