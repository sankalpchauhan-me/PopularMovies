package me.sankalpchauhan.popmovies.view.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.sankalpchauhan.popmovies.R;
import me.sankalpchauhan.popmovies.database.AppDatabase;
import me.sankalpchauhan.popmovies.service.model.MovieResult;
import me.sankalpchauhan.popmovies.service.model.Review;
import me.sankalpchauhan.popmovies.service.model.ReviewResult;
import me.sankalpchauhan.popmovies.service.model.TrailerResult;
import me.sankalpchauhan.popmovies.service.model.Trailers;
import me.sankalpchauhan.popmovies.utils.AppExecutors;
import me.sankalpchauhan.popmovies.utils.Constants;
import me.sankalpchauhan.popmovies.utils.Utility;
import me.sankalpchauhan.popmovies.view.adapter.ReviewAdapter;
import me.sankalpchauhan.popmovies.view.adapter.TrailerAdapter;
import me.sankalpchauhan.popmovies.viewmodel.DetailActivityViewModel;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private MutableLiveData<Boolean> isFav = new MutableLiveData<>();
    private MovieResult movie;
    private ImageView mPoster;
    private TextView mTitle, mVote, mPlot, mReleaseDate, mRuntime, mReviewText, mTrailerText;
    private ProgressBar progressBar;
    private DetailActivityViewModel detailActivityViewModel;
    private List<ReviewResult> reviewResults = new ArrayList<>();
    private List<TrailerResult> trailerResults = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private RecyclerView mReviewRv, mTrailerRv;
    private TrailerAdapter trailerAdapter;
    private ImageView mBackdrop;
    private Button favoriteBTN;
    private AppDatabase roomDB;
    private String firstVideoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar ab = getSupportActionBar();
        Intent previousActivityIntent = getIntent();
        if (previousActivityIntent != null) {
            if (previousActivityIntent.hasExtra(Constants.MOVIE_INTENT)) {
                movie = (MovieResult) previousActivityIntent.getSerializableExtra(Constants.MOVIE_INTENT);
            }
        }
        if (ab != null) {
            ab.setTitle(getResources().getString(R.string.movie_detail));
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mPoster = findViewById(R.id.poster);
        mTitle = findViewById(R.id.title);
        mVote = findViewById(R.id.vote);
        mPlot = findViewById(R.id.plot);
        mReleaseDate = findViewById(R.id.release_date);
        mRuntime = findViewById(R.id.runtime);
        progressBar = findViewById(R.id.progress_circular);
        mReviewRv = findViewById(R.id.reviews_rv);
        mTrailerRv = findViewById(R.id.trailer_rv);
        mTrailerText = findViewById(R.id.trailer_text);
        mReviewText = findViewById(R.id.review_text);
        mBackdrop = findViewById(R.id.backdrop);
        favoriteBTN = findViewById(R.id.favoriteBTN);
        roomDB = AppDatabase.getInstance(getApplicationContext());
        isFav.setValue(false);
        isFav.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                favoriteBTN.setText("Mark As \n Favorite");
                if (aBoolean.equals(true)) {
                    favoriteBTN.setText(getResources().getString(R.string.unmark_favorite));
                }
            }
        });
        populateUI();
        SetUpRecyclerViews();
        isMovieFavorite();

        loadData(movie.getId());

        favoriteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToRoom();
            }
        });

    }

    public void loadData(Integer movieId) {
        detailActivityViewModel = new ViewModelProvider(this).get(DetailActivityViewModel.class);
        detailActivityViewModel.init(movieId);
        detailActivityViewModel.getMovieReview().observe(this, new Observer<Review>() {
            @Override
            public void onChanged(Review review) {
                if (review != null) {
                    List<ReviewResult> reviewResultList = review.getReviewResults();
                    reviewResults.addAll(reviewResultList);
                    reviewAdapter.setMovieData(reviewResults);
                    if (reviewAdapter.getItemCount() == 0) {
                        mReviewText.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(getLocalClassName(), "Response is null");
                    Toast.makeText(DetailActivity.this, "Something Went Wrong Cannot Fetch Reviews", Toast.LENGTH_LONG).show();
                    if (!Utility.isOnline()) {
                        //Toast.makeText(DetailActivity.this, "No Network Connection", Toast.LENGTH_LONG).show();
                        mReviewText.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
        detailActivityViewModel.getMovieTrailer().observe(this, new Observer<Trailers>() {
            @Override
            public void onChanged(Trailers trailers) {
                if (trailers != null) {
                    List<TrailerResult> trailerResultList = trailers.getTrailerResults();
                    trailerResults.addAll(trailerResultList);
                    if (!trailerResults.isEmpty()) {
                        firstVideoUrl = trailerResults.get(0).getKey();
                    }
                    trailerAdapter.setMovieData(trailerResults);
                    if (trailerAdapter.getItemCount() == 0) {
                        mTrailerText.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(getLocalClassName(), "Response is null");
                    Toast.makeText(DetailActivity.this, "Something Went Wrong Cannot Fetch Trailers", Toast.LENGTH_LONG).show();
                    if (!Utility.isOnline()) {
                        //Toast.makeText(DetailActivity.this, "No Network Connection", Toast.LENGTH_LONG).show();
                        mTrailerText.setText(getResources().getString(R.string.no_network_connection));
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
//                Intent upIntent = NavUtils.getParentActivityIntent(this);
//                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//
//                    TaskStackBuilder.create(this)
//                            .addNextIntentWithParentStack(upIntent)
//                            .startActivities();
//                } else {
//                    NavUtils.navigateUpTo(this, upIntent);
//                }
//                return true;
                onBackPressed();
                return true;
            }
            case R.id.share:
                if (!firstVideoUrl.equals("")) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Constants.YOUTUBE_BASE_URL + firstVideoUrl);
                    startActivity(Intent.createChooser(sharingIntent, "Share Trailer via"));
                } else {
                    Toast.makeText(this, getResources().getString(R.string.trailer_unavailaible), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUI() {
        Picasso.get().load(Constants.BASE_POSTER_PATH + movie.getPosterPath()).placeholder(R.drawable.ic_broken_image_grey_24dp).into(mPoster, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
        });
        Picasso.get().load(Constants.BASE_BACKDROP_PATH + movie.getBackdropPath()).into(mBackdrop, new Callback() {
            @Override
            public void onSuccess() {
                mBackdrop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        mTitle.setText(movie.getTitle());
        if (movie.getVoteAverage() != 0.0 || movie.getVoteAverage() != -1) {
            mVote.setText(String.format("%s/10", String.valueOf(movie.getVoteAverage())));
        } else {
            mVote.setText("N/A");
        }
        mPlot.setText(movie.getOverview());
        String[] yearSplit = movie.getReleaseDate().split("-");
        mReleaseDate.setText(yearSplit[0]);
        mRuntime.setText(String.format("Lang.: %s", movie.getOriginalLanguage()));
    }

    public void SetUpRecyclerViews() {
        reviewAdapter = new ReviewAdapter();
        mReviewRv.setLayoutManager(new LinearLayoutManager(this));
        mReviewRv.setHasFixedSize(true);
        mReviewRv.setAdapter(reviewAdapter);
        mReviewRv.setItemAnimator(new DefaultItemAnimator());
        mReviewRv.setNestedScrollingEnabled(true);

        trailerAdapter = new TrailerAdapter(this);
        mTrailerRv.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRv.setHasFixedSize(true);
        mTrailerRv.setAdapter(trailerAdapter);
    }

    @Override
    public void onClick(TrailerResult trailerItem) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_BASE_URL + trailerItem.getKey()));
        startActivity(i);
    }

    public void saveDataToRoom() {
        final MovieResult favMovie = new MovieResult(movie.getId(), movie.getPosterPath(), movie.getBackdropPath(), movie.getVoteCount(), movie.getPopularity(), movie.getOriginalLanguage(), movie.getOriginalTitle(), movie.getTitle(), movie.getVoteAverage(), movie.getOverview(), movie.getReleaseDate());
        if (!isFav.getValue()) {
            //Log.d("Test", isFav.getValue().toString());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    roomDB.movieDao().InsertMovieToRoom(favMovie);
                }
            });
        } else {
            //Log.d("test", "I am deleted");
            isFav.setValue(false);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    roomDB.movieDao().DeleteMovieFromRoom(favMovie);
                }
            });
        }

    }

    private void isMovieFavorite() {
        roomDB.movieDao().getCurrentMovieById(movie.getId()).observe(DetailActivity.this, new Observer<MovieResult>() {
            @Override
            public void onChanged(MovieResult movieResult) {
                if (movieResult != null) {
                    if (movieResult.getId().equals(movie.getId())) {
                        isFav.setValue(true);
                        //Log.d("test", "I am here");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

}
