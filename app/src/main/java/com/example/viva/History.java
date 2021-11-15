package com.example.viva;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private TextView tvNoHistoryData;
    private ArrayList<UserRoom> mRoom;
    private ArrayList<String> spinnerDate = new ArrayList<>();
    private RecyclerView recyclerView;
    private historyAdapter historyAdapter;
    private Spinner spinner;
    private int count;
    private String userId;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvNoHistoryData = findViewById(R.id.tvNoHistoryData);
        recyclerView = findViewById(R.id.history_recyclerView);
        spinner = findViewById(R.id.spinner);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mRoom = new ArrayList<>();
        historyAdapter  = new historyAdapter(History.this, mRoom);
        recyclerView.setAdapter(historyAdapter);


        //add a string of text into the spinner date to call user select a date to view the booking history
        spinnerDate.add("Select a date to view the booking history");

        //Get date from the Check History Collection that book by the user
        CollectionReference collectionReference = db.collection("Check History").document(userId)
                .collection("History");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    String date = documentSnapshot.getId().trim();

                    //Add the date into the spinner.
                    addSpinnerDate(date);
                }
            }
        });

        //Add the date to ArrayList call spinnerDate, and set it into ArrayAdapter
        //so the spinner will show the date to let user choose from.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerDate);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //if the item that selected in the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner.getSelectedItem().toString().equals("Select a date to view the booking history")){

                    //clear the recycler view
                    for (int i=0; i<count; i++){
                        mRoom.remove(0);
                        historyAdapter.notifyItemRemoved(0);
                    }
                    count = 0; //nothing will be display
                }

                else{
                    //if the use select a date then first it clear the recycler view
                    for (int i=0; i<count; i++){
                        mRoom.remove(0);
                        historyAdapter.notifyItemRemoved(0);
                    }

                    count = 0; //nothing will be display

                    //Display the history from the User Collection
                    DocumentReference documentReference = db.collection("Users").document(userId);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            //get the history number from the history collection and pass the string to integer number
                            String count = documentSnapshot.getString("History");
                            int getCount = Integer.parseInt(count);

                            //using for loop to get the total of the history number and display each of the the history
                            for (int i=--getCount; i>=0; i--){
                                DocumentReference documentReference_1 = db.collection("Users").document(userId)
                                        .collection("History").document("history " + i);
                                documentReference_1.addSnapshotListener(History.this, new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                        //get the date from the history
                                        String room_calendar_date = value.getString("Calendar Date");

                                        //check if the date select from the spinner is equal to the database then display the booking history
                                        if (room_calendar_date.equals(spinnerDate.get(position))){
                                            addCount(); //increase the counter to display how many items

                                            String study_room = value.getString("Study Room");
                                            String room_date = value.getString("Date");
                                            String room_hour = value.getString("Hour");
                                            String room_price = value.getString("Price");
                                            String room_time = "";

                                            int price = Integer.parseInt(room_price);
                                            int hour_count = Integer.parseInt(room_hour);

                                            for (int x=1; x<=hour_count; x++){
                                                room_time += value.getString("Time " + x) + "\n";
                                            }

                                            UserRoom userRoom = new UserRoom(study_room, room_time, room_date, room_calendar_date,price, hour_count);

                                            mRoom.add(userRoom);
                                        }
                                        historyAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void addCount(){
        count++;
    }


    //add date into spinner
    public void addSpinnerDate(String date){
        spinnerDate.add(date);
    }

}