package com.example.viva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Button btn_register;
    private EditText editEmail, editPassword,editUsername, editPhoneNo;
    private boolean valid = true;
    private CheckBox isUserBox, isAdminBox;
    private String name, phone, email, password,userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        btn_register=findViewById(R.id.registeruser);
        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        editUsername = findViewById(R.id.editTextUserName);
        editPhoneNo = findViewById(R.id.editTextPhone);
        isAdminBox=findViewById(R.id.admin_checkbox);
        isUserBox=findViewById(R.id.user_checkbox);

        isAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isUserBox.setChecked(false);
                }
            }
        });

        isUserBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAdminBox.setChecked(false);
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(isUserBox.isChecked() || isAdminBox.isChecked())){
                    Toast.makeText(Register.this,"Select the account type!", Toast.LENGTH_LONG).show();
                    return;
                }

                registeruser();
            }
        });
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
            editPassword.setError("Password too short, must at least 6 character");
            editPassword.requestFocus();
            return;
        }

        if(valid){
            fAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(Register.this,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("Username", name);
                            userInfo.put("Email", email);
                            userInfo.put("PhoneNo", phone);
                            userInfo.put("History", "0");

                            if (isAdminBox.isChecked()){
                                userInfo.put("isAdmin","1");
                            }
                            if (isUserBox.isChecked()){
                                userInfo.put("isUser","1");
                            }

                            documentReference.set(userInfo);

                            if (isAdminBox.isChecked()){
                                startActivity(new Intent(getApplicationContext(),Admin.class));
                                finish();
                            }
                            if (isUserBox.isChecked()){
                                startActivity(new Intent(getApplicationContext(),User.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}