package me.sankalpchauhan.popmovies.service.repository;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import me.sankalpchauhan.popmovies.utils.Utility;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static me.sankalpchauhan.popmovies.PopMoviesApp.getContext;
import static me.sankalpchauhan.popmovies.utils.Constants.maxLimit;
import static me.sankalpchauhan.popmovies.utils.Constants.waitThreshold;


/**
 * Retry Policy in case the request fails initially
 * Local caching is also enabled so that the app can be used offline & network calls can be saved
 */

public class RetryPolicy {
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";
    public static final String REVALIDATE_STRATEGY = "must-revalidate";
    public static final Integer MAX_AGE = 5000;
    public static final Integer MAX_FILE_SIZE = 10 * 1024 * 1024; //10 MB
    public static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header(CACHE_CONTROL);
            if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                    cacheControl.contains(REVALIDATE_STRATEGY) || cacheControl.contains("max-age=0")) {
                return originalResponse.newBuilder()
                        .removeHeader(PRAGMA)
                        .header(CACHE_CONTROL, "public, max-age=" + MAX_AGE)
                        .build();
            } else {
                return originalResponse;
            }
        }
    };

    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!Utility.isOnline()) {
                request = request.newBuilder()
                        .removeHeader(PRAGMA)
                        .header(CACHE_CONTROL, "public, only-if-cached")
                        .build();
            }
            return chain.proceed(request);
        }
    };


    static OkHttpClient okClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File httpCacheDirectory = new File(getContext().getCacheDir(), "offlineCache");
        Cache cache = new Cache(httpCacheDirectory, MAX_FILE_SIZE);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .cache(cache)
                //Retry Policy
                .addInterceptor(new ErrorInterceptor())
                //Logging
                .addInterceptor(loggingInterceptor)
                //Caching
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
                .build();

        return client;
    }

    //Retry Policy
    public static class ErrorInterceptor implements Interceptor {
        Response response;
        int tryCount = 0;

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            response = sendReqeust(chain, request);
            while (response == null && tryCount < maxLimit) {
                Log.d("intercept", "Request failed - " + tryCount);
                tryCount++;
                try {
                    Thread.sleep(waitThreshold);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                response = sendReqeust(chain, request);
            }
            return chain.proceed(request);
        }

        private Response sendReqeust(Chain chain, Request request) {
            try {
                response = chain.proceed(request);
                if (!response.isSuccessful())
                    return null;
                else
                    return response;
            } catch (IOException e) {
                return null;
            }
        }
    }

}
