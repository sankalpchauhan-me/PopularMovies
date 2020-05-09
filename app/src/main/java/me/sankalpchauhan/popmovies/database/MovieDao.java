package me.sankalpchauhan.popmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import me.sankalpchauhan.popmovies.service.model.MovieResult;

@Dao
public interface MovieDao {

    @Query("SELECT * from movie ORDER BY popularity")
    LiveData<List<MovieResult>> getAllFavMovies();

    @Query("SELECT * from movie WHERE id = :movieId")
    LiveData<MovieResult> getCurrentMovieById(Integer movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertMovieToRoom(MovieResult movieResult);

    @Delete
    void DeleteMovieFromRoom(MovieResult movieResult);


}
