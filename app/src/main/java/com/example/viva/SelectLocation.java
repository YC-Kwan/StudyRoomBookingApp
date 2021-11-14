package com.example.viva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectLocation extends AppCompatActivity {

    private CardView L1,L2,L3,L4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        L1 = findViewById(R.id.CardLocation1);
        L2 = findViewById(R.id.CardLocation2);
        L3 = findViewById(R.id.CardLocation3);
        L4 = findViewById(R.id.CardLocation4);


        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLocation.this, MapsActivity.class);
                intent.putExtra("Location",1);
                startActivity(intent);
            }
        });

        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLocation.this, MapsActivity.class);
                intent.putExtra("Location",2);
                startActivity(intent);
            }
        });

        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLocation.this, MapsActivity.class);
                intent.putExtra("Location",3);
                startActivity(intent);
            }
        });

        L4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectLocation.this, MapsActivity.class);
                intent.putExtra("Location",4);
                startActivity(intent);
            }
        });
    }
}