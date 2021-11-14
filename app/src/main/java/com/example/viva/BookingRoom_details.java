package com.example.viva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viva.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingRoom_details extends AppCompatActivity {

    private String roomName, date, dateCombine, userID;

    private String time_10 = "", time_11 = "", time_12 = "",
            time_1 = "", time_2 = "", time_3= "",
            time_4 = "", time_5 = "", time_6 = "",time_7 = "",pax;

    private int price = 0, hour = 0, historyNum,totalPrice=0;

    private int count10AM = 0, count11AM = 0, count12PM = 0, count1PM = 0, count2PM = 0, count3PM = 0, count4PM = 0,
            count5PM = 0, count6PM = 0, count7PM = 0;

    private TextView tvRoomName, tvRoomDate, tvPrice;

    private Button btn_10_11,btn_11_12,btn_12_1,btn_1_2,btn_2_3,btn_3_4,btn_4_5,btn_5_6,btn_6_7,btn_7_8;


    private Button btnBook;
    FirebaseFirestore db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingroom_details);

        tvRoomName = findViewById(R.id.tvRoomName);
        tvRoomDate = findViewById(R.id.tvDate);
        tvPrice = findViewById(R.id.tvPrice);

        btn_10_11=findViewById(R.id.btn_10_11);
        btn_11_12=findViewById(R.id. btn_11_12);
        btn_12_1=findViewById(R.id.btn_12_1);
        btn_1_2=findViewById(R.id.btn_1_2);
        btn_2_3=findViewById(R.id.btn_2_3);
        btn_3_4=findViewById(R.id.btn_3_4);
        btn_4_5=findViewById(R.id.btn_4_5);
        btn_5_6=findViewById(R.id.btn_5_6);
        btn_6_7=findViewById(R.id.btn_6_7);
        btn_7_8=findViewById(R.id.btn_7_8);


        btnBook = findViewById(R.id.btnBook);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        getData();
        setData();

        //get the history number from the database and pass the history number from string to int number.
        DocumentReference history_1 = db.collection("Users").document(userID);
        history_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String n = documentSnapshot.getString("History");

                historyNum = Integer.parseInt(n);
            }
        });


        //Get the date and room number from the Calendar Checking collection that selected by the user and display the getTimebooking function.
        DocumentReference documentReference = db.collection("Calendar Checking").document(dateCombine)
                .collection("Study Room").document(roomName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                //if the data get successfully
                if (task.isSuccessful()){

                    getTimeBooking();

                } else {
                    Toast.makeText(BookingRoom_details.this, "Data get unsuccessful", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_10_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //The counter we set early as 0
                //Calculate that the counter is 0 or 1
                count10AM = (count10AM+1) % 2;

                //If the counter is 1, the button will set to black color and text will set to white color .
                if (count10AM == 1){
                    btn_10_11.setBackgroundColor(Color.BLACK);
                    btn_10_11.setTextColor(Color.WHITE);
                    price+=10;  //the price will increase by 10
                    hour+=1;    //the hours will increase by 1
                    time_10 = userID; //the time slots variable will temporary store the userID.
                }

                //else means 0, the button will set to white color and text will set to black color.
                else{
                    btn_10_11.setBackgroundColor(Color.WHITE);
                    btn_10_11.setTextColor(Color.BLACK);
                    price-=10; //the price will decrease by 10
                    hour-=1; //the hours will decrease by 1
                    time_10 = ""; //the time slots variable will remove the userID.
                }

                //set the price text to the price value and call setBtnBookEnable function.
                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();
            }
        });

        btn_11_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count11AM = (count11AM+1) % 2;

                if (count11AM == 1){
                    btn_11_12.setBackgroundColor(Color.BLACK);
                    btn_11_12.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_11 = userID;
                }

                else{
                    btn_11_12.setBackgroundColor(Color.WHITE);
                    btn_11_12.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_11 = "";
                }
                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_12_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count12PM = (count12PM+1) % 2;

                if (count12PM == 1){
                    btn_12_1.setBackgroundColor(Color.BLACK);
                    btn_12_1.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_12 = userID;
                }

                else{
                    btn_12_1.setBackgroundColor(Color.WHITE);
                    btn_12_1.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_12 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count1PM = (count1PM+1) % 2;

                if (count1PM == 1){
                    btn_1_2.setBackgroundColor(Color.BLACK);
                    btn_1_2.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_1 = userID;
                }

                else{
                    btn_1_2.setBackgroundColor(Color.WHITE);
                    btn_1_2.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_1 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2PM = (count2PM+1) % 2;

                if (count2PM == 1){
                    btn_2_3.setBackgroundColor(Color.BLACK);
                    btn_2_3.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_2 = userID;
                }

                else{
                    btn_2_3.setBackgroundColor(Color.WHITE);
                    btn_2_3.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_2 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count3PM = (count3PM+1) % 2;

                if (count3PM == 1){
                    btn_3_4.setBackgroundColor(Color.BLACK);
                    btn_3_4.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_3 = userID;
                }

                else{
                    btn_3_4.setBackgroundColor(Color.WHITE);
                    btn_3_4.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_3 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_4_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count4PM = (count4PM+1) % 2;

                if (count4PM == 1){
                    btn_4_5.setBackgroundColor(Color.BLACK);
                    btn_4_5.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_4 = userID;
                }

                else{
                    btn_4_5.setBackgroundColor(Color.WHITE);
                    btn_4_5.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_4 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_5_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count5PM = (count5PM+1) % 2;

                if (count5PM == 1){
                    btn_5_6.setBackgroundColor(Color.BLACK);
                    btn_5_6.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_5 = userID;
                }

                else{
                    btn_5_6.setBackgroundColor(Color.WHITE);
                    btn_5_6.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_5 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_6_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count6PM = (count6PM+1) % 2;

                if (count6PM == 1){
                    btn_6_7.setBackgroundColor(Color.BLACK);
                    btn_6_7.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_6 = userID;
                }

                else{
                    btn_6_7.setBackgroundColor(Color.WHITE);
                    btn_6_7.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_6 = "";
                }

                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });

        btn_7_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count7PM = (count7PM+1) % 2;

                if (count7PM == 1){
                    btn_7_8.setBackgroundColor(Color.BLACK);
                    btn_7_8.setTextColor(Color.WHITE);
                    price+=10;
                    hour+=1;
                    time_7 = userID;
                }

                else{
                    btn_7_8.setBackgroundColor(Color.WHITE);
                    btn_7_8.setTextColor(Color.BLACK);
                    price-=10;
                    hour-=1;
                    time_7 = "";
                }
                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

            }
        });


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update the history number
                DocumentReference history = db.collection("Users").document(userID);
                history.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String history_number = documentSnapshot.getString("History");
                        historyNum= Integer.parseInt(history_number);

                        //increase the history by 1 in the Users collection
                        historyNum++;
                        addHistoryNum(historyNum + "");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookingRoom_details.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                //Update the time booking slots to hold userID
                DocumentReference Update = db.collection("Calendar").document(dateCombine)
                        .collection("Study Room").document(roomName);

                ArrayList<String> getTime = new ArrayList<>();

                //if the time not equals to empty means ady booked by the user
                //Update the time to string time so the user ID will assign to the string time
                //the string time will store in  getTime arrayList
                if (!time_10.equals("")){
                    Update.update("10AM - 11AM", time_10);
                    getTime.add("10AM - 11AM");
                }

                if (!time_11.equals("")){
                    Update.update("11AM - 12PM", time_11);
                    getTime.add("11AM - 12PM");
                }

                if (!time_12.equals("")){
                    Update.update("12PM - 1 PM", time_12);
                    getTime.add("12PM - 1 PM");
                }

                if (!time_1.equals("")){
                    Update.update("1 PM - 2 PM", time_1);
                    getTime.add("1 PM - 2 PM");
                }

                if (!time_2.equals("")){
                    Update.update("2 PM - 3 PM", time_2);
                    getTime.add("2 PM - 3 PM");
                }

                if (!time_3.equals("")){
                    Update.update("3 PM - 4 PM", time_3);
                    getTime.add("3 PM - 4 PM");
                }

                if (!time_4.equals("")){
                    Update.update("4 PM - 5 PM", time_4);
                    getTime.add("4 PM - 5 PM");
                }

                if (!time_5.equals("")){
                    Update.update("5 PM - 6 PM", time_5);
                    getTime.add("5 PM - 6 PM");
                }

                if (!time_6.equals("")){
                    Update.update("6 PM - 7 PM", time_6);
                    getTime.add("6 PM - 7 PM");
                }

                if (!time_7.equals("")){
                    Update.update("7 PM - 8 PM", time_7);
                    getTime.add("7 PM - 8 PM");
                }


                //Clear all userID from the booking time.
                time_10 = "";
                time_11 = "";
                time_12 = "";
                time_1  = "";
                time_2  = "";
                time_3  = "";
                time_4  = "";
                time_5  = "";
                time_6  = "";
                time_7  = "";

                //call the getTimeBooking function to display the button condition
                getTimeBooking();


                //Store the date booking into check history collection
                //Set the status to yes if have any booking on that date
                DocumentReference storeCheckHistory = db.collection("Check History").document(userID)
                        .collection("History").document(dateCombine);
                Map<String, Object> status = new HashMap<>();
                status.put("Status", "Yes");
                storeCheckHistory.set(status);

                //Store the time booking information into user history
                DocumentReference storeHistory = db.collection("Users").document(userID)
                        .collection("History").document("history " + historyNum);
                Map<String, Object> historyData = new HashMap<>();
                historyData.put("Study Room", roomName);
                historyData.put("Date", date);
                historyData.put("Calendar Date", dateCombine);
                historyData.put("Price", price + "");
                historyData.put("Hour", hour + "");

                //Get the time from the array list
                for (int i=0; i<getTime.size(); i++){
                    int count = i;

                    //store the time into the database
                    historyData.put("Time " + ++count, getTime.get(i));

                }
                storeHistory.set(historyData);

                //reset the arraylist
                getTime.clear();

                //set the price to 0
                price = 0;
                hour  = 0;

                //set the text price into the price value and call setBtnBookEnable function
                tvPrice.setText("RM " + price + ".00" );
                setBtnBookEnable();

                Toast.makeText(BookingRoom_details.this, "Book Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),User.class));
            }
        });


    }

    //Get the data that the user select the study room from previous page
    public void getData(){
        roomName = getIntent().getStringExtra("RoomName");
        date = getIntent().getStringExtra("Date");
        dateCombine = getIntent().getStringExtra("DateCombine");
    }

    //set the data that get from the previous page into the TextView.
    public void setData(){
        tvRoomName.setText("Room: "+ roomName);
        tvRoomDate.setText(date);
    }

    //Get the time slots data from the database to find the record location in the database and display the record out.
    public void getTimeBooking(){
        DocumentReference documentReference_1 = db.collection("Calendar").document(dateCombine)
                .collection("Study Room").document(roomName);

        documentReference_1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                //Set the time to null
                String time = null;

                //get the time slot string from the database
                //call the setBookingEnable function to set the button condition.
                time = documentSnapshot.getString("10AM - 11AM");
                setBookingEnable(time, btn_10_11);

                time = documentSnapshot.getString("11AM - 12PM");
                setBookingEnable(time, btn_11_12);

                time = documentSnapshot.getString("12PM - 1 PM");
                setBookingEnable(time, btn_12_1);

                time = documentSnapshot.getString("1 PM - 2 PM");
                setBookingEnable(time, btn_1_2);

                time = documentSnapshot.getString("2 PM - 3 PM");
                setBookingEnable(time, btn_2_3);

                time = documentSnapshot.getString("3 PM - 4 PM");
                setBookingEnable(time, btn_3_4);

                time = documentSnapshot.getString("4 PM - 5 PM");
                setBookingEnable(time, btn_4_5);

                time = documentSnapshot.getString("5 PM - 6 PM");
                setBookingEnable(time, btn_5_6);

                time = documentSnapshot.getString("6 PM - 7 PM");
                setBookingEnable(time, btn_6_7);

                time = documentSnapshot.getString("7 PM - 8 PM");
                setBookingEnable(time, btn_7_8);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BookingRoom_details.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //get the history number from the User collection and update the number
    private void addHistoryNum(String historyCount){
        DocumentReference documentReference = db.collection("Users").document(userID);
        documentReference.update("History", historyCount);
    }


    public void setBookingEnable(String time, Button btn){
        //if the time not equals to empty means the time booked by other users and the button will set to unable.
        if (!time.equals("")){
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.RED);
            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
        }

        //if the time equals to userID means the time booked by own users and the button will also set to unable and the button, text color will change too.
        if (time.equals(userID)){
            btn.setEnabled(false);
            btn.setBackgroundColor(R.drawable.custom_background_grey);
            btn.setTextColor(Color.parseColor("#FFFFFFFF"));
        }

    }

    public void setBtnBookEnable(){
        //if the price equals to 0 and the button will set to unable.
        if (price == 0){
            btnBook.setEnabled(false);
            btnBook.setBackgroundColor(Color.parseColor("#D3D3D3"));
            btnBook.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
        //if the price is not equals to 0 and the button will set to enable.
        else{
            btnBook.setEnabled(true);
            btnBook.setBackgroundColor(Color.parseColor("#0099ff"));
            btnBook.setTextColor(Color.parseColor("#FFFFFFFF"));
        }
    }
}

