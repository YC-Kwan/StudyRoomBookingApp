package com.example.viva;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registerr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registerr extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button btn_register;
    private EditText editEmail, editPassword,editUsername, editPhoneNo;
    private boolean valid = true;
    private String name, phone, email, password,userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_registerr, container, false);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btn_register=v.findViewById(R.id.registeruser);
        editEmail = v.findViewById(R.id.editTextEmail);
        editPassword = v.findViewById(R.id.editTextPassword);
        editUsername = v.findViewById(R.id.editTextUserName);
        editPhoneNo = v.findViewById(R.id.editTextPhone);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                registeruser();
            }
        });

        return v;
    }

    private void registeruser(){
        name = editUsername.getText().toString().trim();
        phone = editPhoneNo.getText().toString().trim();
        email = editEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editUsername.setError("Enter a name");
            editUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Enter a email");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Invalid email format");
            editEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone) ) {
            editPhoneNo.setError("Enter your phone number");
            editPhoneNo.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Enter the password");
            editPassword.requestFocus();
            return;
        }
        if (password.length() < 8) {
            editPassword.setError("Password too short, must at least 8 character");
            editPassword.requestFocus();
            return;
        }

        if(valid){
            fAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(getContext(),"User has been registered successfully!", Toast.LENGTH_LONG).show();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Username", name);
                            userInfo.put("Email", email);
                            userInfo.put("PhoneNo", phone);
                            userInfo.put("History", "0");
                            userInfo.put("UserType","User");


                            documentReference.set(userInfo);
                            startActivity(new Intent(getActivity(),User.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Registerr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registerr.
     */
    // TODO: Rename and change types and number of parameters
    public static Registerr newInstance(String param1, String param2) {
        Registerr fragment = new Registerr();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

}