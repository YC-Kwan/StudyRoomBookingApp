package com.example.viva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class User extends AppCompatActivity {

    private ImageView img_logout;
    private CardView card_roomAvaiable,card_history,card_profile,card_feedback;
    private FloatingActionButton fab;
    private TextView welcome;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        img_logout=findViewById(R.id.logout);
        card_roomAvaiable=findViewById(R.id.room_available);
        card_history=findViewById(R.id.card_history);
        card_profile=findViewById(R.id.card_profile);
        card_feedback=findViewById(R.id.card_feedback);
        welcome=findViewById(R.id.welcome);
        fab=findViewById(R.id.fab);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        user = firebaseAuth.getCurrentUser();

        //create the date and get each category and store into the variobles
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DocumentReference documentReference = db.collection("Users").document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.getString("Username");

                welcome.setText(String.valueOf(userName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(User.this, "Error " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Account.class));
                finish();
            }
        });

        //if user click on the selet room option and it will show a datePickerDialog to let user pick the date.
        card_roomAvaiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(User.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        //Store the data in string
                        String date = dayOfMonth + "/" + month + "/" + year;

                        //take the data to the next page
                        Intent intent = new Intent(User.this, RoomAvailable.class);
                        intent.putExtra("Day", dayOfMonth);
                        intent.putExtra("Month", month);
                        intent.putExtra("Year", year);
                        intent.putExtra("Date", date);
                        startActivity(intent);
                    }
                },year,month,day
                );
                //Disable past date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                //Show date picker dialog
                datePickerDialog.show();
            }
        });

        card_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),History.class));
            }
        });

        card_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UserFeedback.class);
                startActivity(i);
            }
        });

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(User.this,SelectLocation.class);
                startActivity(a);
            }
        });

    }
}