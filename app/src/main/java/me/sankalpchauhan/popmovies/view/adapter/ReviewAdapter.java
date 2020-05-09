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
import me.sankalpchauhan.popmovies.service.model.ReviewResult;

/**
 * ReviewAdapter for Review recyclerview in detail activity
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private int mExpandedPosition = RecyclerView.NO_POSITION;
    private List<ReviewResult> reviewResultList = new ArrayList<>();

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        if (reviewResultList.get(position).getAuthor() != null) {
            holder.mAuthour.setText(reviewResultList.get(position).getAuthor());
        }
        if (reviewResultList.get(position).getContent() != null) {
            holder.mContent.setText(reviewResultList.get(position).getContent());
            holder.mContentFull.setText(reviewResultList.get(position).getContent());
        }

        final boolean isExpanded = position == mExpandedPosition;
        holder.mContentFull.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.mReadMore.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        holder.mContent.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        holder.itemView.setActivated(isExpanded);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (reviewResultList == null) {
            return 0;
        }
        return reviewResultList.size();
    }

    public void setMovieData(List<ReviewResult> reviewData) {
        reviewResultList = reviewData;
        notifyDataSetChanged();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView mAuthour, mContent, mContentFull, mReadMore;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthour = itemView.findViewById(R.id.authour);
            mContent = itemView.findViewById(R.id.content);
            mContentFull = itemView.findViewById(R.id.content_full);
            mReadMore = itemView.findViewById(R.id.readMore);
        }
    }
}
