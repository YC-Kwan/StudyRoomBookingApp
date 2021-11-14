package com.example.viva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    private ImageView img_logout;
    private CardView card_admin_room,card_location,card_profile,card_adminFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        img_logout=findViewById(R.id.logout);
        card_admin_room=findViewById(R.id.admin_room);
        card_location=findViewById(R.id.card_location);
        card_profile=findViewById(R.id.card_profile);
        card_adminFeedback= findViewById(R.id.card_adminFeedback);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Account.class));
                finish();
            }
        });

        card_admin_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminRoom.class));
            }
        });

        card_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin.this,SelectLocation.class);
                startActivity(i);
            }
        });

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Profile.class));
            }
        });

        card_adminFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminFeedback.class));
            }
        });
    }

}