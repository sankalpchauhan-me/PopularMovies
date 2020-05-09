package me.sankalpchauhan.popmovies.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.sankalpchauhan.popmovies.R;
import me.sankalpchauhan.popmovies.service.model.MovieResult;
import me.sankalpchauhan.popmovies.utils.Constants;

/**
 * Adapter for recyclerview in main activity
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final MovieAdapterOnClickHandler mClickHandler;
    private List<MovieResult> movieData = new ArrayList<>();

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        mClickHandler = movieAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        if (movieData.get(position).getPosterPath() != null) {
            Picasso.get().load(Constants.BASE_POSTER_PATH + movieData.get(position).getPosterPath()).placeholder(R.drawable.ic_broken_image_grey_24dp).into(holder.moviePoster, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    holder.errorMessage.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    holder.errorMessage.setVisibility(View.VISIBLE);
                    holder.errorMessage.setText(movieData.get(position).getTitle());
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (movieData == null) {
            return 0;
        }
        return movieData.size();
    }

    public void setMovieData(List<MovieResult> movieData1) {
        movieData = movieData1;
        notifyDataSetChanged();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieResult movieItem);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;
        final ProgressBar progressBar;
        final TextView errorMessage;


        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.poster);
            progressBar = itemView.findViewById(R.id.progress_circular);
            errorMessage = itemView.findViewById(R.id.errorMessage);
            moviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(movieData.get(adapterPosition));
        }
    }
}
