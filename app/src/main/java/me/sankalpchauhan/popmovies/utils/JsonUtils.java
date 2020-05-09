package me.sankalpchauhan.popmovies.utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



/**
 * Was used in stage-1 of Popular Movies keeping here for reference
 */
//public final class JsonUtils {
//    /**
//     * Reference JSON
//     * "results": [
//     * {
//     * "popularity": 538.99,
//     * "vote_count": 3109,
//     * "video": false,
//     * "poster_path": "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
//     * "id": 419704,
//     * "adult": false,
//     * "backdrop_path": "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
//     * "original_language": "en",
//     * "original_title": "Ad Astra",
//     * "genre_ids": [
//     * 18,
//     * 878
//     * ],
//     * "title": "Ad Astra",
//     * "vote_average": 6,
//     * "overview": "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
//     * "release_date": "2019-09-17"
//     * },
//     * <p>
//     * String title, String posterPath, String releaseDate, double voteAverage, String plotSynopsis
//     */
//
//    public static List<Movie> getMoviesFromJson(String json) throws JSONException {
//        List<Movie> parsedMovie = new ArrayList<>();
//        //final String OWM_MESSAGE_CODE = "status_message";
//
//        JSONObject movieJson = new JSONObject(json);
//        JSONArray resultArr = movieJson.getJSONArray(Constants.RESULTS);
//
//        for (int i = 0; i <= resultArr.length() - 1; i++) {
//            JSONObject movieResult = resultArr.getJSONObject(i);
//            String title;
//            String posterPath;
//            String releaseDate;
//            double voteAverage;
//            String plotSynopsis;
//
//            title = movieResult.optString(Constants.TITLE, "N/A");
//            posterPath = Constants.BASE_POSTER_PATH + movieResult.optString(Constants.POSTER_PATH, "N/A");
//            releaseDate = movieResult.optString(Constants.RELEASE_DATE, "N/A");
//            voteAverage = movieResult.optDouble(Constants.VOTE_AVERAGE, -1);
//            plotSynopsis = movieResult.optString(Constants.OVERVIEW, "N/A");
//            Movie newMovie = new Movie(title, posterPath, releaseDate, voteAverage, plotSynopsis);
//            //Log.i("Test", newMovie.getPosterPath()+"\n");
//
//            parsedMovie.add(newMovie);
//        }
//
//        return parsedMovie;
//    }
//}
