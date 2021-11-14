package com.example.viva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomAvailable extends AppCompatActivity {

    private TextView tvDate;
    private String dateCombine, userID;
    private ArrayList<Room> mRoom;
    private RecyclerView recyclerView;
    private BookingRoomAdapter bookingRoomAdapter;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_available);

        tvDate = findViewById(R.id.tvDate);
        recyclerView = findViewById(R.id.room_recyclerView);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        //Get the data from the datePickerDialog in the previous page
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        int day = intent.getIntExtra("Day", 0);
        int month = intent.getIntExtra("Month", 0);
        int year = intent.getIntExtra("Year", 0);

        //Combine the day, month, year.
        dateCombine = day + " " + month + " " + year;

        //set the text to date that choose from the previous page.
        tvDate.setText(date);

        //After user select the date and the date will store in database
        DocumentReference calendar_checking = db.collection("Calendar Checking").document(dateCombine)
                .collection("Study Room").document("Study Room Check");
        Map<String, Object> check_Calendar = new HashMap<>();
        check_Calendar.put("Status", "Yes");
        calendar_checking.set(check_Calendar);

        //Create Calendar for user and store the userID in to it if have any booking
        DocumentReference documentReference = db.collection("Calendar").document(dateCombine);
        Map<String, Object> calendar = new HashMap<>();
        calendar.put("Status", "Yes");
        documentReference.set(calendar);

        //Set the Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the ArrayLIst that will contain the data
        mRoom = new ArrayList<>();

        //After finish get all the data, the mRoom object will put into the adapter which to set into the recyclerView
        bookingRoomAdapter = new BookingRoomAdapter(this, mRoom, date, dateCombine);
        recyclerView.setAdapter(bookingRoomAdapter);

        getRoom();

    }

    //Display the study room
    private void getRoom(){
        db.collection("AddRoom").orderBy("Created time", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                mRoom.clear();

                for (int i=0; i<queryDocumentSnapshots.size();i++){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);

                    if(documentSnapshot.exists()) {

                        String id = documentSnapshot.getId();
                        String roomNo = documentSnapshot.getString("Room Name");
                        String maxPerson = documentSnapshot.getString("Maximum number");
                        String rules = documentSnapshot.getString("Rules");

                        Room  room = new Room (id, roomNo,maxPerson,rules);

                        mRoom.add(room);
                    }
                }
                bookingRoomAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

}