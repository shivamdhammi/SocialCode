package com.example.socialcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {

    private EditText email,password;
    private Button login,forgot,noaccount;
    private boolean log;
    AVLoadingIndicatorView avi;
    private FirebaseAuth auth;
    String Email,Password;
    public String ret="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_login);
        forgot = findViewById(R.id.login_forgotpassword);
        noaccount = findViewById(R.id.login_noaccount);
        avi = findViewById(R.id.login_avi);

        auth= FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
//                avi.show();
                loginUser();
            }
        });

        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginUser(){
        avi.smoothToShow();
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if(Email.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Enter a valid Email Address");
            email.requestFocus();
            return;
        }
        if(Password.isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(Password.length()<8){
            password.setError("Minimum Password Length is 8.");
            password.requestFocus();
            return;
        }


        auth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences sharedPref=getSharedPreferences("MyData", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPref.edit();
                            editor.putString("Email",Email);
                            editor.putString("Password",Password);
                            // editor.putString("State",state);
                            editor.commit();

                            Intent intent = new Intent(getApplicationContext(),profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            avi.smoothToHide();
                            Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();

//                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private class fetchdata extends AsyncTask<Void,Void,String> {
        String result;

        public fetchdata() {
            super();
            Log.d("log---fetchdata()",ret+"");
        }

        @Override
        protected String doInBackground(Void... voids) {
//            Toast.makeText(getApplicationContext(),"background",Toast.LENGTH_LONG).show();

            return ret;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("log---onPreExecute",ret+"");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("log---onPostExecute",ret+"");


        }


    }
}
