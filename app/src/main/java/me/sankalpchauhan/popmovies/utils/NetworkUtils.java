package me.sankalpchauhan.popmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import me.sankalpchauhan.popmovies.BuildConfig;


/**
 * Was used in stage-1 of Popular Movies, keeping here for reference
 */
//public final class NetworkUtils {
//    private static final String TAG = NetworkUtils.class.getSimpleName();
//    private static final String BASE_URL = "https://api.themoviedb.org/3";
//
//
//    public static URL buildUrl(String sortParam) {
//        Uri builtUri;
//        String apiKey = "api_key";
//        if (sortParam.trim().isEmpty()) {
//            builtUri = Uri.parse(BASE_URL).buildUpon()
//                    .appendPath("discover")
//                    .appendPath("movie")
//                    .appendQueryParameter(apiKey, BuildConfig.API_KEY)
//                    .build();
//        } else {
//            builtUri = Uri.parse(BASE_URL).buildUpon()
//                    .appendPath("movie")
//                    .appendPath(sortParam)
//                    .appendQueryParameter(apiKey, BuildConfig.API_KEY)
//                    .build();
//        }
//        URL url = null;
//        try {
//            url = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        if (!BuildConfig.API_KEY.equals("YOUR_API_KEY")) {
//            Log.v(TAG, "Built URI " + url);
//        } else {
//            Log.e(TAG, "\n \n \n API Key Not Placed To successfully run the project place The Movie DB API key inside gradle.properties in THE_MOVIE_DB_API_KEY variable \n \n \n");
//        }
//
//        return url;
//    }
//
//    public static String getResponseFromHttpUrl(URL url) throws IOException {
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        InputStream in = null;
//        try {
//            in = urlConnection.getInputStream();
//
//            Scanner scanner = new Scanner(in);
//            scanner.useDelimiter("\\A");
//
//            boolean hasInput = scanner.hasNext();
//            if (hasInput) {
//                return scanner.next();
//            } else {
//                return null;
//            }
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            urlConnection.disconnect();
//        }
//    }
//}

