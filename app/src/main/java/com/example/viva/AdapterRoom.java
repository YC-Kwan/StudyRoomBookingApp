package com.example.viva;

import android.app.Activity;
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

import com.example.viva.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdapterRoom extends RecyclerView.Adapter<AdapterRoom.PosterListHolder> {
    List<Room> rooms;
    Activity activity3;
    private Context mContext;
    private String mRoomName = "",mMaxPpl = "",mRoomRules= "";
    private String post_key = "";
    FirebaseFirestore db;


    public AdapterRoom(List<Room> rooms, Activity activity3) {
        this.rooms = rooms;
        this.activity3= activity3;
    }

    @NonNull
    @Override
    public AdapterRoom.PosterListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posteradapter, parent, false);
        return new PosterListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRoom.PosterListHolder holder, int position) {
        final Room Rooms = rooms.get(position);

        final String roomNo ="Room Name: " + Rooms.getHallNo();
        final String maxPerson ="Max Person: " + Rooms.getMaxPerson();
        final String rules ="Rules: " + Rooms.getRules();

        holder.roomName.setText(roomNo);
        holder.maxPerson.setText(maxPerson);
        holder.roomRules.setText(rules);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_key = Rooms.getId();
                mRoomName = Rooms.getHallNo();
                mMaxPpl = Rooms.getMaxPerson();
                mRoomRules = Rooms.getRules();
                updateData();
            }
        });
    }

    private void updateData() {
        AlertDialog.Builder myDialog= new AlertDialog.Builder(activity3);
        LayoutInflater inflater = LayoutInflater.from(activity3);
        View mView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(mView);
        final  AlertDialog dialog = myDialog.create();

        final EditText mRoom = mView.findViewById(R.id.update_roomName);
        final EditText mPpl= mView.findViewById(R.id.update_maxPerson);
        final  EditText mRules= mView.findViewById(R.id.update_rules);

        mRoom.setText(String.valueOf(mRoomName));
        mPpl.setText(String.valueOf(mMaxPpl));
        mRules.setText(String.valueOf(mRoomRules));

        Button btnDel = mView.findViewById(R.id.btnDelete);
        Button btnUpdate = mView.findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRoomName = mRoom.getText().toString();
                mMaxPpl = mPpl.getText().toString();
                mRoomRules = mRules.getText().toString();

                Room room= new Room(post_key,mRoomName,mMaxPpl,mRoomRules);
                db.collection("AddRoom").document(post_key)
                        .update(
                        "Room Name",room.getHallNo(),
                        "Maximum number", room.getMaxPerson(),
                        "Rules",room.getRules()
                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(),"Updated successfully!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(activity3,AdminRoom.class);
                        activity3.startActivity(intent);

                    }
                });
                dialog.dismiss();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("AddRoom").document(post_key).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(view.getContext(),"Deleted successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity3,AdminRoom.class);
                            activity3.startActivity(intent);
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class PosterListHolder extends RecyclerView.ViewHolder{

        TextView roomName,maxPerson,roomRules;

        public PosterListHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.roomNumber);
            maxPerson = itemView.findViewById(R.id.maxPpl);
            roomRules = itemView.findViewById(R.id.roomRules);

        }
    }
}

