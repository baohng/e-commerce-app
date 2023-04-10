package com.example.ebn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebn.R;
import com.example.ebn.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextFullNameReg, editTextPhoneNumReg;
    Button buttonReg;
    TextView loginNow;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editTextFullNameReg = findViewById(R.id.editTextFullNameReg);
        editTextPhoneNumReg = findViewById(R.id.editTextPhoneNumReg);
        editTextEmail = findViewById(R.id.editTextEmailReg);
        editTextPassword = findViewById(R.id.editTextPasswordReg);
        buttonReg = findViewById(R.id.buttonReg);
        loginNow = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.progressBarReg);
        mAuth = FirebaseAuth.getInstance();

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, fullname, phoneNum, descript,gender, occupation, dob;
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                fullname = editTextFullNameReg.getText().toString().trim();
                phoneNum = editTextPhoneNumReg.getText().toString().trim();
                descript = "";
                gender = "";
                occupation = "";

                Date date = new Date();
                Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                dob = dt.format(newDate);


                if (fullname.isEmpty()) {
                    editTextFullNameReg.setError("Full name is required!");
                    editTextFullNameReg.requestFocus();
                    return;
                }
                if (phoneNum.isEmpty()) {
                    editTextPhoneNumReg.setError("Phone number is required!");
                    editTextPhoneNumReg.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Please provide valid email!");
                    editTextEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    editTextPassword.setError("Min password length should be 6 characters!");
                    editTextPassword.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    User user = new User(fullname, phoneNum, email, descript, gender, occupation, dob);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Account created.",
                                                            Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                } //redirect to login layout
                                                else {
                                                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again.",
                                                            Toast.LENGTH_LONG).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Failed to register! Try again.",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}