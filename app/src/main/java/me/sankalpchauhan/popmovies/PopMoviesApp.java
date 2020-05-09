package me.sankalpchauhan.popmovies;

import android.app.Application;
import android.content.Context;

public class PopMoviesApp extends Application {
    private static PopMoviesApp instance;

    public static PopMoviesApp getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
