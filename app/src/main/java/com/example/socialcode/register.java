package com.example.socialcode;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText name,college,email,password,repassword,codeforces,hackerrank;
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
        repassword = findViewById(R.id.reg_repass);
        codeforces = findViewById(R.id.reg_codeforces);
        hackerrank = findViewById(R.id.reg_hackerrank);

        register = findViewById(R.id.reg_register);

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
        String Name = name.getText().toString().trim();
        String College = college.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Reassword = repassword.getText().toString().trim();
        String Codeforces = codeforces.getText().toString().trim();
        String Hackerrank = hackerrank.getText().toString().trim();

        boolean valid = check(Name,College,Email,Password,Reassword,Codeforces,Hackerrank);

        if(valid){
            final userInfo info = new userInfo(Name,College,Email,Codeforces,Hackerrank);
            final friendInfo finfo = new friendInfo();
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        myRef.child(mAuth.getCurrentUser().getUid()).child("Info").setValue(info);
                        myRef.child(mAuth.getCurrentUser().getUid()).child("Friends").child("deafultFriend").setValue(finfo);
                        Toast.makeText(getApplicationContext(), "Registered Successfully..", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {

                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_LONG).show();
                        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(getApplicationContext(), "Password is too weak", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
        else
            return;
    }

    private boolean check(String Name,String College,String Email,String Password,String Repassword,String Codeforces,String Hackerrank){
        if(Name.isEmpty()){
            name.setError("Please Enter Your Name");
            name.requestFocus();
            return false;
        }
        if(College.isEmpty()){
            college.setError("Please Enter Your College");
            college.requestFocus();
            return false;
        }
        if(Email.isEmpty()&&!(Patterns.EMAIL_ADDRESS.matcher(Email).matches())){
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return false;
        }
        if(Password.isEmpty()){
            password.setError("Please Enter Your Password");
            password.requestFocus();
            return false;
        }
        if(!Password.equals(Repassword)){
            repassword.setError("Password didn't match. Try again.");
            repassword.requestFocus();
            return false;
        }
        if(Codeforces.isEmpty()){
            codeforces.setError("Please Enter Your Codeforces Handle");
            codeforces.requestFocus();
            return false;
        }
        if(Hackerrank.isEmpty()){
            hackerrank.setError("Please Enter Your HackerRank Handle");
            hackerrank.requestFocus();
            return false;
        }

        return true;
    }

}
