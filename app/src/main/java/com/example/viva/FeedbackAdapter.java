package com.example.viva;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackListHolder> {

    List<Feedback> mFeedbacks;
    private Context mContext;
    FirebaseFirestore db;

    public FeedbackAdapter(List<Feedback> mFeedbacks, Context context) {
        this.mFeedbacks = mFeedbacks;
        this.mContext= context;
    }

    @NonNull
    @Override
    public FeedbackAdapter.FeedbackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_adapter, parent, false);
        return new FeedbackListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackListHolder holder, int position) {
        final Feedback userFeedback = mFeedbacks.get(position);

        final String feedbacks ="Feedback: " + userFeedback.getFeedbacks();
        final String feedback_date ="Posted on: " + userFeedback.getDate();

        holder.TvFeedback.setText(feedbacks);
        holder.TvDate.setText(feedback_date);
    }

    @Override
    public int getItemCount() {
        return mFeedbacks.size();
    }

    class FeedbackListHolder  extends RecyclerView.ViewHolder{

        TextView TvFeedback,TvDate;

        public FeedbackListHolder (@NonNull View itemView) {
            super(itemView);
            TvFeedback = itemView.findViewById(R.id.Tvfeedback);
            TvDate = itemView.findViewById(R.id.TvFeedbackDate);
        }
    }
}
