package me.sankalpchauhan.popmovies.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.sankalpchauhan.popmovies.R;
import me.sankalpchauhan.popmovies.service.model.TrailerResult;

/**
 * TrailerAdapter for Trailer recyclerview in detail activity
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final TrailerAdapterOnClickHandler mClickHandler;
    List<TrailerResult> trailerResultList = new ArrayList<>();

    public TrailerAdapter(TrailerAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        if (trailerResultList.get(position).getName() != null) {
            holder.mTrailerName.setText(trailerResultList.get(position).getName());
        }

    }

    @Override
    public int getItemCount() {
        if (trailerResultList == null) {
            return 0;
        }
        return trailerResultList.size();
    }

    public void setMovieData(List<TrailerResult> trailerData) {
        trailerResultList = trailerData;
        notifyDataSetChanged();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(TrailerResult trailerItem);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTrailerName;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(trailerResultList.get(adapterPosition));
        }
    }
}
