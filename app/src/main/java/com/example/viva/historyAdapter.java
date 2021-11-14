package com.example.viva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viva.R;
import com.example.viva.Room;

import java.util.ArrayList;

public class historyAdapter extends  RecyclerView.Adapter<historyAdapter.historyViewHolder> {
    private Context context;
    private ArrayList<UserRoom> mRoom;

    public historyAdapter(Context c, ArrayList<UserRoom> room){
        context = c;
        mRoom= room;
    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.history_adapter, parent, false);
        return new historyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historyViewHolder holder, int position) {
        holder.tvDate.setText(mRoom.get(position).getDate());
        holder.tvRoom.setText(mRoom.get(position).getRoomName());
        holder.tvPrice.setText("RM " + mRoom.get(position).getPrice());
        holder.tvTime.setText(mRoom.get(position).getTime() + "");
    }

    @Override
    public int getItemCount() {
        return mRoom.size();
    }

    public class historyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoom, tvPrice, tvTime, tvDate;

        public historyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.textViewDate);
            tvRoom = itemView.findViewById(R.id.history_room);
            tvPrice = itemView.findViewById(R.id.history_price);
            tvTime = itemView.findViewById(R.id.history_time);

        }

    }
}
