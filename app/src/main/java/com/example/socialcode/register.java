package com.example.socialcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText name,college,email,password,codeforces,hackerrank;
    private Button register;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth,firebaseAuth;
    private FirebaseAuth mAuthListener;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.reg_name);
        college = findViewById(R.id.reg_college);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_pass);
        codeforces = findViewById(R.id.reg_codeforces);
        hackerrank = findViewById(R.id.reg_hackerrank);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void addUser(){

    }
}
