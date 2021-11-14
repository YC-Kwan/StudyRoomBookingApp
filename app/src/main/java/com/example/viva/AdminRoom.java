package com.example.viva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminRoom extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private FloatingActionButton fab;
    private AdapterRoom adapterRoom;
    List<Room> rooms;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        rooms = new ArrayList<>();
        adapterRoom  = new AdapterRoom(rooms, AdminRoom.this);
        recyclerView.setAdapter(adapterRoom);

        getRoom();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRooms();
            }
        });

    }

    private void getRoom(){
        fStore.collection("AddRoom").orderBy("Created time", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                rooms.clear();

                for (int i=0; i<queryDocumentSnapshots.size();i++){
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(i);

                    if(documentSnapshot.exists()) {

                        String id = documentSnapshot.getId();
                        String roomNo = documentSnapshot.getString("Room Name");
                        String maxPerson = documentSnapshot.getString("Maximum number");
                        String rules = documentSnapshot.getString("Rules");

                        Room  room = new Room (id, roomNo,maxPerson,rules);

                        rooms.add(room);
                    }
                }

                adapterRoom.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void addRooms(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout, null);
        myDialog.setView(myView);

        final  AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText roomNames = myView.findViewById(R.id.roomName);
        final EditText maxppl = myView.findViewById(R.id.maxPerson);
        final EditText rules = myView.findViewById(R.id.rules);
        final Button cancel = myView.findViewById(R.id.cancel);
        final  Button save = myView.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String RoomName = roomNames.getText().toString();
                String MaxPpl = maxppl.getText().toString();
                String Rules = rules.getText().toString();

                if(RoomName.isEmpty()){
                    roomNames.setError("Room Name is Required");
                    roomNames.requestFocus();
                    return;
                }
                if(MaxPpl.isEmpty()){
                    maxppl.setError("Max person is required");
                    maxppl.requestFocus();
                    return;
                }
                if(Rules.isEmpty()){
                    rules.setError("Rules is required");
                    rules.requestFocus();
                    return;
                }
                else {
                    FirebaseUser user = fAuth.getCurrentUser();
                    DocumentReference df= fStore.collection("AddRoom").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Map<String, Object> AddRooms = new HashMap<>();

                    AddRooms.put("Room Name",roomNames.getText().toString());
                    AddRooms.put("Maximum number",maxppl.getText().toString());
                    AddRooms.put("Created time", Calendar.getInstance().getTime().toString());
                    AddRooms.put("Rules",rules.getText().toString());

                    fStore.collection("AddRoom")
                            .add(AddRooms)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(AdminRoom.this,"Room has been added successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),AdminRoom.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}