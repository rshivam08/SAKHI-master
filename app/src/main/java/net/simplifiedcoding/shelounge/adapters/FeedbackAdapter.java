package net.simplifiedcoding.shelounge.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import net.simplifiedcoding.shelounge.R;
import net.simplifiedcoding.shelounge.models.Feedback;

import java.util.List;

/**
 * Created by tjuhi on 6/1/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FViewHolder> {
    private List<Feedback> feedbacks;

    public FeedbackAdapter(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;

    }

    @Override
    public FeedbackAdapter.FViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);

        return new FViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackAdapter.FViewHolder holder, int position) {
        holder.mName.setText(feedbacks.get(position).getUser());
        holder.mComment.setText(feedbacks.get(position).getComment());

        holder.mRate.setRating(Float.parseFloat(feedbacks.get(position).getRating()));
        holder.mDate.setText("on " + feedbacks.get(position).getDate());
        holder.mRate.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public class FViewHolder extends RecyclerView.ViewHolder {
        private TextView mName, mComment, mDate;
        private RatingBar mRate;

        public FViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.user);
            mComment = (TextView) itemView.findViewById(R.id.comment);
            mRate = (RatingBar) itemView.findViewById(R.id.rating);
            mDate = (TextView) itemView.findViewById(R.id.date);

        }


    }
}
