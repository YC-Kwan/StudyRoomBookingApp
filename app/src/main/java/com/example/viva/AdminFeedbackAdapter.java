package com.example.viva;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminFeedbackAdapter extends RecyclerView.Adapter<AdminFeedbackAdapter.AdminFeedbackListHolder> {

    private Context mContext;
    List<Feedback> mFeedbacks;
    FirebaseFirestore db;
    private String post_key = "";
    private String userFeedbacks = "";

    public AdminFeedbackAdapter(List<Feedback> feedbacks, Context context) {
        this.mFeedbacks = feedbacks;
        this.mContext= context;
    }

    @NonNull
    @Override
    public AdminFeedbackAdapter.AdminFeedbackListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_feedback_adapter, parent, false);
        return new AdminFeedbackListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFeedbackAdapter.AdminFeedbackListHolder holder, int position) {
        final Feedback userFeedback = mFeedbacks.get(position);

        final String feedbacks ="Feedback: " + userFeedback.getFeedbacks();
        final String feedback_date ="Posted on: " + userFeedback.getDate();
        final String feedback_name ="Posted By: " + userFeedback.getUsername();


        holder.TvFeedback.setText(feedbacks);
        holder.TvDate.setText(feedback_date);
        holder.TvUser.setText(feedback_name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_key = userFeedback.getId();
                userFeedbacks = userFeedback.getFeedbacks();
                deleteData();
            }
        });
    }

    private void deleteData() {
        AlertDialog.Builder myDialog= new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mView = inflater.inflate(R.layout.delete_feedback, null);

        myDialog.setView(mView);
        final  AlertDialog dialog = myDialog.create();

        final TextView tvFeedback = mView.findViewById(R.id.editFeedback);

        tvFeedback.setText(String.valueOf(userFeedbacks));

        Button btnDel = mView.findViewById(R.id.btnDel);
        Button btnCancel = mView.findViewById(R.id.cancel);

        db = FirebaseFirestore.getInstance();
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Feedback").document(post_key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(),"Deleted successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext,AdminFeedback.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @Override
    public int getItemCount() {
        return mFeedbacks.size();
    }

    class AdminFeedbackListHolder  extends RecyclerView.ViewHolder{

        TextView TvFeedback,TvDate,TvUser;

        public AdminFeedbackListHolder (@NonNull View itemView) {
            super(itemView);
            TvFeedback = itemView.findViewById(R.id.Tvfeedback);
            TvDate = itemView.findViewById(R.id.TvFeedbackDate);
            TvUser=itemView.findViewById(R.id.TvUsername);
        }
    }
}
