package com.example.viva;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingRoomAdapter extends RecyclerView.Adapter<BookingRoomAdapter.BookingRoomViewHolder> {
    private Context context;
    private List<Room> mRoom;
    private String date, dateCombine, userID;
    private Boolean checkCalendar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public BookingRoomAdapter(Context c, List<Room> room, String d, String dCombine) {
        context = c;
        mRoom = room;
        date = d;
        dateCombine = dCombine;
    }

    @NonNull
    @Override
    public BookingRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.room_adapter, parent, false);
        return new BookingRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRoomAdapter.BookingRoomViewHolder holder, int position) {
        holder.tv_roomName.setText(mRoom.get(position).getHallNo());
        holder.tv_roomMax.setText(mRoom.get(position).getMaxPerson());
        holder.tv_roomRules.setText(mRoom.get(position).getRules());

        //When the user click on any study room
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = firebaseAuth.getCurrentUser().getUid();

                //It will check the study room has been created or not in the database
                CollectionReference collectCalendar = db.collection("Calendar Checking").document(dateCombine)
                        .collection("Study Room");
                collectCalendar.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            String roomName = documentSnapshots.getId();

                            //set the calendar to false means didnt have any timeslot create
                            checkCalendar = false;

                            //if the room name from the database is equals to the study room that select by the user
                            //and it will set the calendar to true means already created
                            if (roomName.equals(mRoom.get(position).getHallNo())){
                                checkCalendar = true;
                                break;
                            }
                        } //End for loop
                        //if not then create a new time slot
                        createNewCalendar(checkCalendar, position);
                    }
                })      //Get the data and pass to the next page.
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(context, BookingRoom_details.class);
                                    intent.putExtra("RoomName", mRoom.get(position).getHallNo());
                                    intent.putExtra("Date", date);
                                    intent.putExtra("DateCombine", dateCombine);
                                    context.startActivity(intent);
                                }
                            }
                        });
            }
        });
    }

    public class BookingRoomViewHolder extends RecyclerView.ViewHolder{
        TextView tv_roomName,tv_roomMax,tv_roomRules;
        CardView mainLayout;

        public BookingRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_roomName = itemView.findViewById(R.id.user_roomNumber);
            tv_roomMax = itemView.findViewById(R.id.user_maxPpl);
            tv_roomRules = itemView.findViewById(R.id.user_roomRules);
            mainLayout = itemView.findViewById(R.id.user_room_mainLayout);
        }
    }

    @Override
    public int getItemCount() {
        return mRoom.size();
    }

    //Create the time slot for the room if user click on any room.
    public void createNewCalendar(Boolean check, int position) {
        if (!check) {
            //Create booking time slots based on the study room that select by the user
            DocumentReference documentReference = db.collection("Calendar").document(dateCombine)
                    .collection("Study Room").document(mRoom.get(position).getHallNo());
            Map<String, Object> timeStatus = new HashMap<>();
            timeStatus.put("10AM - 11AM", "");
            timeStatus.put("11AM - 12PM", "");
            timeStatus.put("12PM - 1 PM", "");
            timeStatus.put("1 PM - 2 PM", "");
            timeStatus.put("2 PM - 3 PM", "");
            timeStatus.put("3 PM - 4 PM", "");
            timeStatus.put("4 PM - 5 PM", "");
            timeStatus.put("5 PM - 6 PM", "");
            timeStatus.put("6 PM - 7 PM", "");
            timeStatus.put("7 PM - 8 PM", "");
            documentReference.set(timeStatus);

            //Set status to yes and create the room of the name when the user select on the specific room.
            DocumentReference calendar_checking = db.collection("Calendar Checking")
                    .document(dateCombine).collection("Study Room").document(mRoom.get(position).getHallNo());
            Map<String, Object> checkCalendar = new HashMap<>();
            checkCalendar.put("Status", "Yes");
            calendar_checking.set(checkCalendar);

        }
    }


}
